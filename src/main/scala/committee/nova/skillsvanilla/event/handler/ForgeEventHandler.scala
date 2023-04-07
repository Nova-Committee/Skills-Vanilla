package committee.nova.skillsvanilla.event.handler

import committee.nova.skillful.implicits.Implicits.EntityPlayerImplicit
import committee.nova.skillful.storage.SkillfulStorage.{SkillRegisterEvent, SkillRelatedFoodRegisterEvent}
import committee.nova.skillsvanilla.config.CommonConfig
import committee.nova.skillsvanilla.event.impl.DamageSourceBlackListEvent
import committee.nova.skillsvanilla.item.api.IItemForTraining
import committee.nova.skillsvanilla.item.init.ItemInit
import committee.nova.skillsvanilla.registries.VanillaSkillRelatedFoods._
import committee.nova.skillsvanilla.registries.VanillaSkills._
import committee.nova.skillsvanilla.util.Utilities
import net.minecraft.block.material.Material
import net.minecraft.entity.item.{EntityArmorStand, EntityItem}
import net.minecraft.entity.player.{EntityPlayer, EntityPlayerMP}
import net.minecraft.entity.projectile.EntityArrow
import net.minecraft.entity.projectile.EntityArrow.PickupStatus
import net.minecraft.init.{Items, SoundEvents}
import net.minecraft.inventory.ContainerEnchantment
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.util.math.{AxisAlignedBB, MathHelper}
import net.minecraft.util.{EntityDamageSource, EntityDamageSourceIndirect}
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.event.enchanting.EnchantmentLevelSetEvent
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent
import net.minecraftforge.event.entity.living._
import net.minecraftforge.event.entity.player.PlayerEvent.{BreakSpeed, Visibility}
import net.minecraftforge.event.entity.player.{AttackEntityEvent, ItemFishedEvent, PlayerPickupXpEvent}
import net.minecraftforge.event.world.BlockEvent.BreakEvent
import net.minecraftforge.fml.common.eventhandler.{EventPriority, SubscribeEvent}

import scala.collection.JavaConverters.collectionAsScalaIterableConverter
import scala.util.Random


object ForgeEventHandler {
  def init(): Unit = MinecraftForge.EVENT_BUS.register(new ForgeEventHandler)
}

class ForgeEventHandler {
  @SubscribeEvent
  def onSkillRegister(event: SkillRegisterEvent): Unit = event.addSkills(
    STRENGTH, CONSTITUTION, WILL, AGILITY, LUCK,
    PERCEPTION, TACTICS, BLOCK, ARCHERY, THROWING,
    MINING, LOGGING, EXCAVATING, SWIMMING, ANATOMY,
    ENCHANTING, STEALTH
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
  def onDamageSourceBlackList(event: DamageSourceBlackListEvent): Unit = CommonConfig.getBlackList.foreach(s => event.addDamageSourceClassToBlackList(s))

  @SubscribeEvent
  def onItemRegister(e: RegistryEvent.Register[Item]): Unit = ItemInit.init(e)

  @SubscribeEvent
  def onDamageModifier(event: LivingHurtEvent): Unit = {
    if (event.getAmount < .0F) return
    val dmg = event.getSource
    if (dmg.damageType == "outOfWorld") return
    if (DamageSourceBlackListEvent.isInBlackList(dmg)) return
    var attacker: EntityPlayerMP = null
    var indirectDmg: EntityDamageSourceIndirect = null
    var isHurtByEntity = false
    var fire = 0
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
      val strength = attacker.getSkillStat(STRENGTH)
      if (indirectDmg != null) {
        indirectDmg.damageType match {
          case "thrown" =>
            val throwing = attacker.getSkillStat(THROWING)
            antiDodge *= (1F + throwing.getCurrentLevel * 0.001F)
            event.setAmount(event.getAmount + throwing.getCurrentLevel * 0.01F)
            event.setAmount(event.getAmount * (1.0F + strength.getCurrentLevel * 0.02F))
            val sq = MathHelper.clamp(Math.sqrt(event.getAmount).toInt, 1, 500)
            throwing.addXp(attacker, sq)
            strength.addXp(attacker, 2 * sq)
          case "arrow" =>
            val archery = attacker.getSkillStat(ARCHERY)
            if (archery.getCurrentLevel >= 20 && !victim.isInstanceOf[EntityPlayer])
              world.spawnEntity(new EntityItem(world, victim.posX, victim.posY + 1.0, victim.posZ, new ItemStack(Items.ARROW, 1)))
            antiDodge *= (1F + archery.getCurrentLevel * 0.05F)
            event.setAmount(event.getAmount * (1.0F + archery.getCurrentLevel * 0.02F))
            event.setAmount(event.getAmount * (1.0F + strength.getCurrentLevel * 0.02F))
            val sq = MathHelper.clamp(Math.sqrt(event.getAmount).toInt, 1, 500)
            archery.addXp(attacker, sq)
            strength.addXp(attacker, 2 * sq)
          case _ =>
        }
      } else {
        if (!dmg.isMagicDamage && !dmg.isProjectile) {
          val tactics = attacker.getSkillStat(TACTICS)
          antiDodge *= (1F + tactics.getCurrentLevel * 0.1F)
          event.setAmount(event.getAmount * (1.0F + tactics.getCurrentLevel * 0.02F))
          event.setAmount(event.getAmount * (1.0F + strength.getCurrentLevel * 0.02F))
          val sq = MathHelper.clamp(Math.sqrt(event.getAmount).toInt, 1, 500)
          tactics.addXp(attacker, sq)
          strength.addXp(attacker, 2 * sq)
        }
      }
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
        val sq = MathHelper.clamp(Math.sqrt(event.getAmount).toInt, 1, 500)
        if (isHurtByEntity) {
          strength.addXp(p, sq)
          if (canBlock) block.addXp(p, 2 * sq)
        }
        val constitution = p.getSkillStat(CONSTITUTION)
        will.addXp(p, sq + fire)
        constitution.addXp(p, sq)
      case _ =>
    }
  }

  @SubscribeEvent
  def onKill(event: LivingDeathEvent): Unit = {
    val victim = event.getEntityLiving
    var attacker: EntityPlayerMP = null
    var indirectDmg: EntityDamageSourceIndirect = null
    val src = event.getSource
    src match {
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
    } else if (!src.isMagicDamage && !src.isProjectile) {
      attacker.getSkillStat(ANATOMY).addXp(attacker, rand.nextInt(2 + (victim.width * victim.height).toInt))
      attacker.getSkillStat(TACTICS).addXp(attacker, 5 + (victim.getMaxHealth / 20).toInt)
    }
  }

  @SubscribeEvent
  def onPlayerAttack(event: AttackEntityEvent): Unit = {
    event.getEntityPlayer match {
      case p: EntityPlayerMP => event.getTarget match {
        case stand: EntityArmorStand =>
          val stack = p.getHeldItemMainhand
          if (!stack.getItem.isInstanceOf[IItemForTraining]) return
          val rand = p.getRNG
          p.getSkillStat(STRENGTH).addXp(p, rand.nextInt(2))
          p.getSkillStat(TACTICS).addXp(p, rand.nextInt(2))
          stand.punchCooldown = -1L
          stack.damageItem(1, p)
        case _ =>
      }
      case _ =>
    }
  }

  @SubscribeEvent
  def onJump(event: LivingJumpEvent): Unit = {
    event.getEntityLiving match {
      case p: EntityPlayer =>
        val agility = p.getSkillStat(AGILITY)
        if (agility.isClueless) return
        p.motionY +=.0042F * agility.getCurrentLevel
      case _ =>
    }
  }

  @SubscribeEvent
  def onFall(event: LivingFallEvent): Unit = {
    event.getEntityLiving match {
      case p: EntityPlayer =>
        val agility = p.getSkillStat(AGILITY).getCurrentLevel
        val maxImmune = 3.0F + agility / 20.0F
        event.setDamageMultiplier(if (event.getDistance < maxImmune) 0.0F else 1.0F - 0.009F * agility)
      case _ =>
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
      case Material.WOOD => if (Utilities.isAxe(item)) event.setNewSpeed(original * (1.0F + p.getSkillStat(LOGGING).getCurrentLevel * 0.05F))
      case Material.ROCK => if (Utilities.isPickaxe(item)) event.setNewSpeed(original * (1.0F + p.getSkillStat(MINING).getCurrentLevel * 0.05F))
      case _ => event.setNewSpeed(original * (1.0F + p.getSkillStat(EXCAVATING).getCurrentLevel * 0.02F))
    }
  }

  @SubscribeEvent
  def onBroke(event: BreakEvent): Unit = {
    event.getPlayer match {
      case p: EntityPlayerMP =>
        val item = p.getHeldItemMainhand.getItem
        event.getState.getMaterial match {
          case Material.WOOD => if (Utilities.isAxe(item)) p.getSkillStat(LOGGING).addXp(p, 2)
          case Material.ROCK => if (Utilities.isPickaxe(item)) p.getSkillStat(MINING).addXp(p, 2)
          case _ => p.getSkillStat(EXCAVATING).addXp(p, 1)
        }
      case _ =>
    }
  }

  @SubscribeEvent
  def onLooting(event: LootingLevelEvent): Unit = event.getDamageSource.getTrueSource match {
    case p: EntityPlayerMP => event.setLootingLevel(event.getLootingLevel + p.getRNG.nextInt(1 + p.getSkillStat(ANATOMY).getCurrentLevel / 10))
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

  @SubscribeEvent(priority = EventPriority.LOWEST)
  def onFished(event: ItemFishedEvent): Unit = {
    event.getEntityPlayer match {
      case p: EntityPlayerMP => p.getSkillStat(PERCEPTION).addXp(p, 10)
      case _ =>
    }
  }
}
