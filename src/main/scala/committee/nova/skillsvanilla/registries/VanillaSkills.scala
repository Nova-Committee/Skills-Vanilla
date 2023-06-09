package committee.nova.skillsvanilla.registries

import committee.nova.skillful.api.skill.{IApplyAttributeModifiers, IXPChangesAfterSleep}
import committee.nova.skillful.impl.skill.instance.SkillInstance
import committee.nova.skillful.impl.skill.{AttributeInfluencingSkill, Skill, SkillBuilder}
import committee.nova.skillsvanilla.SkillsVanilla
import committee.nova.skillsvanilla.implicits.Implicits.AbstractHorseImplicit
import committee.nova.skillsvanilla.modifiers.VanillaModifiers
import net.minecraft.entity.ai.attributes.{AttributeModifier, IAttribute}
import net.minecraft.entity.passive.AbstractHorse
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.entity.{EntityLivingBase, SharedMonsterAttributes}
import net.minecraft.util.ResourceLocation
import net.minecraft.world.BossInfo

object VanillaSkills {
  final val T200: Int => Int = i => i * 200

  final val STRENGTH = new Skill(new ResourceLocation(SkillsVanilla.MODID, "strength"), 100, BossInfo.Color.GREEN, T200)
  final val CONSTITUTION = new Constitution
  final val WILL = new Skill(new ResourceLocation(SkillsVanilla.MODID, "will"), 100, BossInfo.Color.GREEN, T200)
  final val AGILITY = new Agility
  final val PERCEPTION = new Perception
  final val LUCK = new Luck
  final val TACTICS = new AttributeInfluencingSkill(new ResourceLocation(SkillsVanilla.MODID, "tactics"), 100, BossInfo.Color.YELLOW, T200,
    SharedMonsterAttributes.ATTACK_SPEED, (_, s) => VanillaModifiers.getTacticsAttackSpeedBoostModifier(s.getCurrentLevel))
  final val BLOCK = new AttributeInfluencingSkill(new ResourceLocation(SkillsVanilla.MODID, "block"), 100, BossInfo.Color.YELLOW, T200,
    SharedMonsterAttributes.KNOCKBACK_RESISTANCE, (_, s) => VanillaModifiers.getBlockKnockbackResistanceAmplifier(s.getCurrentLevel))
  final val ARCHERY = new Skill(new ResourceLocation(SkillsVanilla.MODID, "archery"), 100, BossInfo.Color.YELLOW, T200)
  final val THROWING = new Skill(new ResourceLocation(SkillsVanilla.MODID, "throwing"), 100, BossInfo.Color.YELLOW, T200)
  final val MINING = new Skill(new ResourceLocation(SkillsVanilla.MODID, "mining"), 100, BossInfo.Color.YELLOW, T200)
  final val LOGGING = new Skill(new ResourceLocation(SkillsVanilla.MODID, "logging"), 100, BossInfo.Color.YELLOW, T200)
  final val EXCAVATING = new Skill(new ResourceLocation(SkillsVanilla.MODID, "excavating"), 100, BossInfo.Color.YELLOW, T200)
  final val SWIMMING = new AttributeInfluencingSkill(new ResourceLocation(SkillsVanilla.MODID, "swimming"), 100, BossInfo.Color.YELLOW, T200,
    EntityLivingBase.SWIM_SPEED, (_, s) => VanillaModifiers.getSwimmingSpeedBoostModifier(s.getCurrentLevel))
  final val EQUESTRIANISM = SkillBuilder.create(new ResourceLocation(SkillsVanilla.MODID, "equestrianism"))
    .setMaxLevel(100).setColor(BossInfo.Color.YELLOW).setLevelRequiredXP(T200)
    .actOnLevelChange((p, _, _) => p.getRidingEntity match {
      case h: AbstractHorse => h.updateAttrs(p, shouldRenew = true)
      case _ =>
    }).build
  final val ANATOMY = new Skill(new ResourceLocation(SkillsVanilla.MODID, "anatomy"), 100, BossInfo.Color.PURPLE, T200)
  final val ENCHANTING = new Skill(new ResourceLocation(SkillsVanilla.MODID, "enchanting"), 100, BossInfo.Color.PURPLE, T200)
  final val STEALTH = new Skill(new ResourceLocation(SkillsVanilla.MODID, "stealth"), 100, BossInfo.Color.PURPLE, T200)

  class Constitution extends Skill(new ResourceLocation(SkillsVanilla.MODID, "constitution"), 100, BossInfo.Color.GREEN, T200)
    with IXPChangesAfterSleep with IApplyAttributeModifiers {
    override def change(player: EntityPlayerMP, instance: SkillInstance): Int = {
      val rand = player.getRNG
      if (rand.nextBoolean) rand.nextInt(10) else 0
    }

    override def getAttributeModifiers(player: EntityPlayerMP, skillInstance: SkillInstance): Map[IAttribute, AttributeModifier] = Map(
      SharedMonsterAttributes.MAX_HEALTH -> VanillaModifiers.getConstitutionMaxHealthAmplifier(skillInstance.getCurrentLevel),
      SharedMonsterAttributes.KNOCKBACK_RESISTANCE -> VanillaModifiers.getConstitutionKnockbackResistanceAmplifier(skillInstance.getCurrentLevel)
    )

    override def act(player: EntityPlayerMP, instance: SkillInstance, isUp: Boolean): Unit = {
      val scale = player.getHealth / player.getMaxHealth
      super.act(player, instance, isUp)
      player.setHealth(scale * player.getMaxHealth)
    }
  }

  class Perception extends Skill(new ResourceLocation(SkillsVanilla.MODID, "perception"), 100, BossInfo.Color.GREEN, T200) with IXPChangesAfterSleep {
    override def change(player: EntityPlayerMP, instance: SkillInstance): Int = player.getRNG.nextInt(3)
  }

  class Luck extends AttributeInfluencingSkill(new ResourceLocation(SkillsVanilla.MODID, "luck"), 100, BossInfo.Color.GREEN, T200,
    SharedMonsterAttributes.LUCK, (_, s) => VanillaModifiers.getLuckAmplifier(s.getCurrentLevel)) with IXPChangesAfterSleep {
    override def change(player: EntityPlayerMP, instance: SkillInstance): Int = {
      val rand = player.getRNG
      if (rand.nextBoolean) rand.nextInt(5) else -rand.nextInt(2)
    }
  }

  class Agility extends Skill(new ResourceLocation(SkillsVanilla.MODID, "agility"), 100, BossInfo.Color.GREEN, T200) with IApplyAttributeModifiers {
    override def getAttributeModifiers(player: EntityPlayerMP, skillInstance: SkillInstance): Map[IAttribute, AttributeModifier] = Map(
      SharedMonsterAttributes.MOVEMENT_SPEED -> VanillaModifiers.getAgilityWalkingSpeedBoostModifier(skillInstance.getCurrentLevel),
      SharedMonsterAttributes.ATTACK_SPEED -> VanillaModifiers.getAgilityAttackSpeedBoostModifier(skillInstance.getCurrentLevel)
    )
  }
}
