package committee.nova.skillsvanilla.implicits

import committee.nova.skillful.implicits.Implicits.EntityPlayerImplicit
import committee.nova.skillsvanilla.modifiers.VanillaModifiers
import committee.nova.skillsvanilla.registries.VanillaSkills.EQUESTRIANISM
import committee.nova.skillsvanilla.util.Utilities
import net.minecraft.entity.passive.AbstractHorse
import net.minecraft.entity.player.{EntityPlayer, EntityPlayerMP}
import net.minecraft.entity.{Entity, EntityLiving, SharedMonsterAttributes}
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.play.server.SPacketSoundEffect
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.{SoundCategory, SoundEvent}

import java.util.{List => JList}
import scala.collection.JavaConverters.collectionAsScalaIterableConverter

object Implicits {
  implicit class PlayerImplicit(val player: EntityPlayer) {
    def getStealthEffect: Int = {
      if (!player.isSneaking || player.world.getWorldTime % 20 != 0) return 0
      val mobs = getMobsWithIn(16.0)
      var freeMob = false
      var isSeen = false
      var targeted = false
      mobs.asScala.filter(l => Utilities.isEnemy(l)).foreach(m => {
        val target = m.getAttackTarget
        val seen = m.canEntityBeSeen(player)
        if (seen) {
          if (target == null) freeMob = true
          if (target == player) targeted = true
          isSeen = true
        }
      })
      if (freeMob && !targeted) 2 else if (isSeen) 1 else 0
    }

    def getTargeting(r: Double): Int = getMobsWithIn(r).asScala.count(m => player == m.getAttackTarget)

    def getMobsWithIn(r: Double): JList[EntityLiving] = player.world.getEntitiesWithinAABB(classOf[EntityLiving], new AxisAlignedBB(player.posX - r, player.posY - r, player.posZ - r, player.posX + r, player.posY + r, player.posZ + r))

    def playPacketSound(sound: SoundEvent): Unit = player match {
      case p: EntityPlayerMP => p.connection.sendPacket(new SPacketSoundEffect(sound, SoundCategory.PLAYERS, p.posX, p.posY, p.posZ, 1.0F, 1.0F))
      case _ =>
    }
  }

  implicit class AbstractHorseImplicit(val horse: AbstractHorse) {
    def updateAttrs(entity: Entity, shouldRenew: Boolean): Unit = {
      val speed = horse.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED)
      speed.removeModifier(VanillaModifiers.EQUESTRIANISM_HORSE_SPEED_AMPLIFIER_UUID)
      val jumpStrength = horse.getEntityAttribute(AbstractHorse.JUMP_STRENGTH)
      jumpStrength.removeModifier(VanillaModifiers.EQUESTRIANISM_HORSE_JUMP_STRENGTH_AMPLIFIER_UUID)
      val armorToughness = horse.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS)
      armorToughness.removeModifier(VanillaModifiers.EQUESTRIANISM_HORSE_ARMOR_TOUGHNESS_AMPLIFIER_UUID)
      if (!shouldRenew) return
      entity match {
        case p: EntityPlayerMP =>
          val equestrianism = p.getSkillStat(EQUESTRIANISM).getCurrentLevel
          speed.applyModifier(VanillaModifiers.getEquestrianismHorseSpeedAmplifier(equestrianism))
          jumpStrength.applyModifier(VanillaModifiers.getEquestrianismHorseJumpStrengthAmplifier(equestrianism))
          armorToughness.applyModifier(VanillaModifiers.getEquestrianismHorseArmorToughnessAmplifier(equestrianism))
        case _ =>
      }
    }
  }

  implicit class ItemStackImplicit(val stack: ItemStack) {
    def getOrCreateTag: NBTTagCompound = {
      if (!stack.hasTagCompound) stack.setTagCompound(new NBTTagCompound)
      stack.getTagCompound
    }
  }
}
