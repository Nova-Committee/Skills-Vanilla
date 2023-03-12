package committee.nova.skillsvanilla.event.handler

import committee.nova.skillful.implicits.Implicits.EntityPlayerImplicit
import committee.nova.skillful.storage.SkillfulStorage.{SkillRegisterEvent, SkillRelatedFoodRegisterEvent}
import committee.nova.skillsvanilla.registries.VanillaSkillRelatedFoods.APPLE
import committee.nova.skillsvanilla.registries.VanillaSkills._
import net.minecraft.block.material.Material
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.init.SoundEvents
import net.minecraft.item.{ItemAxe, ItemPickaxe}
import net.minecraft.util.{EntityDamageSource, EntityDamageSourceIndirect}
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.{LivingDeathEvent, LivingHealEvent, LivingHurtEvent}
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed
import net.minecraftforge.event.world.BlockEvent.BreakEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

import scala.util.Random


object ForgeEventHandler {
  def init(): Unit = MinecraftForge.EVENT_BUS.register(new ForgeEventHandler)
}

class ForgeEventHandler {
  @SubscribeEvent
  def onSkillRegister(event: SkillRegisterEvent): Unit = event.addSkills(
    STRENGTH, CONSTITUTION, WILL, AGILITY,
    LUCK, TACTICS, BLOCK, ARCHERY, THROWING,
    MINING, LOGGING, EXCAVATING, SWIMMING
  )

  @SubscribeEvent
  def onFoodRegister(event: SkillRelatedFoodRegisterEvent): Unit = event.addSkillRelatedFoods(
    APPLE
  )

  @SubscribeEvent
  def onDamageModifier(event: LivingHurtEvent): Unit = {
    var attacker: EntityPlayerMP = null
    var indirectDmg: EntityDamageSourceIndirect = null
    var isHurtByEntity = false
    var fire = 0
    val dmg = event.getSource
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
    event.getEntityLiving match {
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
    } else attacker.getSkillStat(TACTICS).addXp(attacker, 5 + (victim.getMaxHealth / 20).toInt)
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
}
