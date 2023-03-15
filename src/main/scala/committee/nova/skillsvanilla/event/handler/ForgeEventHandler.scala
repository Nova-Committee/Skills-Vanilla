package committee.nova.skillsvanilla.event.handler

import committee.nova.skillful.implicits.Implicits.EntityPlayerImplicit
import committee.nova.skillful.storage.SkillfulStorage.{SkillRegisterEvent, SkillRelatedFoodRegisterEvent}
import committee.nova.skillsvanilla.registries.VanillaSkillRelatedFoods._
import committee.nova.skillsvanilla.registries.VanillaSkills._
import net.minecraft.block.material.Material
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.{EntityPlayer, EntityPlayerMP}
import net.minecraft.entity.projectile.EntityArrow
import net.minecraft.entity.projectile.EntityArrow.PickupStatus
import net.minecraft.init.{Items, SoundEvents}
import net.minecraft.inventory.ContainerEnchantment
import net.minecraft.item.{ItemAxe, ItemPickaxe, ItemStack}
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.{EntityDamageSource, EntityDamageSourceIndirect}
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.enchanting.EnchantmentLevelSetEvent
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.event.entity.living.{LivingDeathEvent, LivingHealEvent, LivingHurtEvent, LootingLevelEvent}
import net.minecraftforge.event.entity.player.PlayerEvent.{BreakSpeed, Visibility}
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent
import net.minecraftforge.event.world.BlockEvent.BreakEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

import scala.collection.JavaConverters.collectionAsScalaIterableConverter
import scala.util.Random


object ForgeEventHandler {
  def init(): Unit = MinecraftForge.EVENT_BUS.register(new ForgeEventHandler)
}

class ForgeEventHandler {
  @SubscribeEvent
  def onSkillRegister(event: SkillRegisterEvent): Unit = event.addSkills(
    STRENGTH, CONSTITUTION, WILL, AGILITY, LUCK,
    TACTICS, BLOCK, ARCHERY, THROWING, MINING,
    LOGGING, EXCAVATING, SWIMMING, ANATOMY, ENCHANTING,
    STEALTH
  )

  @SubscribeEvent
  def onFoodRegister(event: SkillRelatedFoodRegisterEvent): Unit = event.addSkillRelatedFoods(
    APPLE, MUSHROOM_STEW, BREAD, RAW_PORKCHOP, COOKED_PORKCHOP,
    GOLDEN_APPLE, RAW_FISH, COOKED_FISH, COOKIE, MELON,
    RAW_BEEF, COOKED_BEEF, RAW_CHICKEN, COOKED_CHICKEN, ROTTEN_FLESH,
    SPIDER_EYE, CARROT, GOLDEN_CARROT, POTATO, BAKED_POTATO,
    POISONOUS_POTATO, PUMPKIN_PIE, RAW_RABBIT, COOKED_RABBIT, RABBIT_STEW,
    RAW_MUTTON, COOKED_MUTTON, BEETROOT, BEETROOT_SOUP
  )

  @SubscribeEvent
  def onDamageModifier(event: LivingHurtEvent): Unit = {
    var attacker: EntityPlayerMP = null
    var indirectDmg: EntityDamageSourceIndirect = null
    var isHurtByEntity = false
    var fire = 0
    val dmg = event.getSource
    val world = event.getEntityLiving.world
    dmg match {
      case e: EntityDamageSource =>
        e.getTrueSource match {
          case p: EntityPlayerMP => attacker = p
          case _ =>
        }
        e match {
          case i: EntityDamageSourceIndirect => indirectDmg = i
          case _ =>
        }
        isHurtByEntity = true
      case o => o.damageType match {
        case "inFire" => fire = 2
        case "onFire" => fire = 1
        case _ =>
      }
    }
    var antiDodge: Float = event.getAmount
    val victim = event.getEntityLiving
    if (attacker != null) {
      if (indirectDmg != null) {
        indirectDmg.damageType match {
          case "thrown" =>
            val throwing = attacker.getSkillStat(THROWING)
            antiDodge *= (1F + throwing.getCurrentLevel * 0.001F)
            event.setAmount(event.getAmount + throwing.getCurrentLevel * 0.01F)
            throwing.addXp(attacker, 1)
          case "arrow" =>
            val archery = attacker.getSkillStat(ARCHERY)
            if (archery.getCurrentLevel >= 20 && !victim.isInstanceOf[EntityPlayer])
              world.spawnEntity(new EntityItem(world, victim.posX, victim.posY + 1.0, victim.posZ, new ItemStack(Items.ARROW, 1)))
            antiDodge *= (1F + archery.getCurrentLevel * 0.05F)
            event.setAmount(event.getAmount * (1.0F + archery.getCurrentLevel * 0.02F))
            archery.addXp(attacker, 1)
          case _ =>
        }
      } else {
        val tactics = attacker.getSkillStat(TACTICS)
        antiDodge *= (1F + tactics.getCurrentLevel * 0.1F)
        event.setAmount(event.getAmount * (1.0F + tactics.getCurrentLevel * 0.02F))
        tactics.addXp(attacker, 1)
      }
      val strength = attacker.getSkillStat(STRENGTH)
      event.setAmount(event.getAmount * (1.0F + strength.getCurrentLevel * 0.02F))
      strength.addXp(attacker, 2)
    }
    victim match {
      case p: EntityPlayerMP =>
        val strength = p.getSkillStat(STRENGTH)
        val agility = p.getSkillStat(AGILITY)
        val dodgePossibility = agility.getCurrentLevel * strength.getCurrentLevel * 0.01F
        if (dodgePossibility * Random.nextFloat() > antiDodge) {
          event.setCanceled(true)
          p.playSound(SoundEvents.ENTITY_SNOWBALL_THROW, 0.5F, 1.0F)
          return
        }
        val canBlock = !dmg.isUnblockable
        val block = p.getSkillStat(BLOCK)
        val will = p.getSkillStat(WILL)
        event.setAmount(event.getAmount / (1.0F + (if (canBlock) block.getCurrentLevel else 1) * will.getCurrentLevel * strength.getCurrentLevel * 0.00005F))
        if (isHurtByEntity) {
          strength.addXp(p, 1)
          if (canBlock) block.addXp(p, 2)
        }
        will.addXp(p, 1 + fire)
        val constitution = p.getSkillStat(CONSTITUTION)
        constitution.addXp(p, 1)
      case _ =>
    }
  }

  @SubscribeEvent
  def onKill(event: LivingDeathEvent): Unit = {
    val victim = event.getEntityLiving
    var attacker: EntityPlayerMP = null
    var indirectDmg: EntityDamageSourceIndirect = null
    event.getSource match {
      case e: EntityDamageSource =>
        e.getTrueSource match {
          case p: EntityPlayerMP => attacker = p
          case _ =>
        }
        e match {
          case i: EntityDamageSourceIndirect => indirectDmg = i
          case _ =>
        }
      case _ =>
    }
    if (attacker == null) return
    if (indirectDmg != null) {
      indirectDmg.damageType match {
        case "thrown" =>
          attacker.getSkillStat(THROWING).addXp(attacker, 10 + (victim.getMaxHealth / 10).toInt)
        case "arrow" =>
          attacker.getSkillStat(ARCHERY).addXp(attacker, 5 + (victim.getMaxHealth / 20).toInt)
        case _ =>
      }
    } else {
      attacker.getSkillStat(ANATOMY).addXp(attacker, rand.nextInt(2 + (victim.width * victim.height).toInt))
      attacker.getSkillStat(TACTICS).addXp(attacker, 5 + (victim.getMaxHealth / 20).toInt)
    }
  }

  @SubscribeEvent
  def onEntityJoinWorld(event: EntityJoinWorldEvent): Unit = {
    event.getEntity match {
      case a: EntityArrow => a.shootingEntity match {
        case p: EntityPlayerMP => if (p.getSkillStat(ARCHERY).getCurrentLevel > 20) a.pickupStatus = PickupStatus.ALLOWED
        case _ =>
      }
      case _ =>
    }
  }

  @SubscribeEvent
  def onHeal(event: LivingHealEvent): Unit = {
    event.getEntityLiving match {
      case p: EntityPlayerMP =>
        // Constitution related healing
        event.setAmount(event.getAmount * (1.0F + p.getSkillStat(CONSTITUTION).getCurrentLevel * 0.02F))
      case _ =>
    }
  }

  @SubscribeEvent
  def onBreaking(event: BreakSpeed): Unit = {
    val original = event.getOriginalSpeed
    val p = event.getEntityPlayer
    val item = p.getHeldItemMainhand.getItem
    event.getState.getMaterial match {
      case Material.WOOD => if (item.isInstanceOf[ItemAxe]) event.setNewSpeed(original * (1.0F + p.getSkillStat(LOGGING).getCurrentLevel * 0.05F))
      case Material.ROCK => if (item.isInstanceOf[ItemPickaxe]) event.setNewSpeed(original * (1.0F + p.getSkillStat(MINING).getCurrentLevel * 0.05F))
      case _ => event.setNewSpeed(original * (1.0F + p.getSkillStat(EXCAVATING).getCurrentLevel * 0.02F))
    }
  }

  @SubscribeEvent
  def onBroke(event: BreakEvent): Unit = {
    event.getPlayer match {
      case p: EntityPlayerMP =>
        val item = p.getHeldItemMainhand.getItem
        event.getState.getMaterial match {
          case Material.WOOD => if (item.isInstanceOf[ItemAxe]) p.getSkillStat(LOGGING).addXp(p, 2)
          case Material.ROCK => if (item.isInstanceOf[ItemPickaxe]) p.getSkillStat(MINING).addXp(p, 2)
          case _ => p.getSkillStat(EXCAVATING).addXp(p, 1)
        }
      case _ =>
    }
  }

  @SubscribeEvent
  def onLooting(event: LootingLevelEvent): Unit = event.getDamageSource.getTrueSource match {
    case p: EntityPlayerMP => event.setLootingLevel(event.getLootingLevel + new Random().nextInt(1 + p.getSkillStat(ANATOMY).getCurrentLevel / 10))
    case _ =>
  }

  @SubscribeEvent
  def onEnchantLevel(event: EnchantmentLevelSetEvent): Unit = {
    val player = event.getWorld.getEntitiesWithinAABB(classOf[EntityPlayerMP], new AxisAlignedBB(event.getPos.getX - 4.0, event.getPos.getY - 4.0, event.getPos.getZ - 4.0,
      event.getPos.getX + 4.0, event.getPos.getY + 4.0, event.getPos.getZ + 4.0))
    if (player.isEmpty) return
    var l: Int = 0
    player.asScala.foreach(p => p.openContainer match {
      case _: ContainerEnchantment => l += p.getSkillStat(ENCHANTING).getCurrentLevel / 5
      case _ =>
    })
    event.setLevel(event.getLevel + l)
  }

  @SubscribeEvent
  def onPlayerGainXP(event: PlayerPickupXpEvent): Unit = {
    //Temporary
    event.getEntityPlayer match {
      case p: EntityPlayerMP => p.getSkillStat(ENCHANTING).addXp(p, event.getOrb.getXpValue)
      case _ =>
    }
  }

  @SubscribeEvent
  def onVisibility(event: Visibility): Unit = event.modifyVisibility(1.0 - (event.getEntityPlayer.getSkillStat(STEALTH).getCurrentLevel / 100.0))
}
