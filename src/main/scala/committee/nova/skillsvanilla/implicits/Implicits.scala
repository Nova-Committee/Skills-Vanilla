package committee.nova.skillsvanilla.implicits

import committee.nova.skillsvanilla.util.Utilities
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.player.{EntityPlayer, EntityPlayerMP}
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

  implicit class ItemStackImplicit(val stack: ItemStack) {
    def getOrCreateTag: NBTTagCompound = {
      if (!stack.hasTagCompound) stack.setTagCompound(new NBTTagCompound)
      stack.getTagCompound
    }
  }
}
