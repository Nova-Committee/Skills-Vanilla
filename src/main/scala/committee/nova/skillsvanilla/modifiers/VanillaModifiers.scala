package committee.nova.skillsvanilla.modifiers

import net.minecraft.entity.ai.attributes.AttributeModifier

import java.util.UUID

object VanillaModifiers {
  val AGILITY_WALKING_SPEED_BOOST_UUID: UUID = UUID.fromString("153A17B7-02CC-2AAB-780C-D9E787A4F0DA")

  def getAgilityWalkingSpeedBoostModifier(lvl: Int): AttributeModifier = new AttributeModifier(AGILITY_WALKING_SPEED_BOOST_UUID, "Agility Walking Speed Boost",
    lvl * 0.02, 2)

  val SWIMMING_SPEED_BOOST_UUID: UUID = UUID.fromString("13E91EAF-421C-7BAD-7241-7B28A361323F")

  def getSwimmingSpeedBoostModifier(lvl: Int): AttributeModifier = new AttributeModifier(SWIMMING_SPEED_BOOST_UUID, "Swimming Speed Booster",
    lvl * 0.02, 2)

  val AGILITY_ATTACK_SPEED_BOOST_UUID: UUID = UUID.fromString("04CC1450-2D01-CF79-9E0E-E0912A8C5378")

  def getAgilityAttackSpeedBoostModifier(lvl: Int): AttributeModifier = new AttributeModifier(AGILITY_ATTACK_SPEED_BOOST_UUID, "Agility Attack Speed Boost",
    lvl * 0.002, 0)

  val TACTICS_ATTACK_SPEED_BOOST_UUID: UUID = UUID.fromString("FEC7AAD0-2100-6ECE-C51F-8E1B4A4ED60C")

  def getTacticsAttackSpeedBoostModifier(lvl: Int): AttributeModifier = new AttributeModifier(TACTICS_ATTACK_SPEED_BOOST_UUID, "Agility Attack Speed Boost",
    lvl * 0.005, 0)

  val LUCK_AMPLIFIER_UUID: UUID = UUID.fromString("A695CAD7-1AC0-8EEB-AC28-A4F327D4CEB3")

  def getLuckAmplifier(lvl: Int): AttributeModifier = new AttributeModifier(LUCK_AMPLIFIER_UUID, "Luck Amplifier",
    lvl, 0)

  val BLOCK_KNOCKBACK_RESISTANCE_AMPLIFIER_UUID: UUID = UUID.fromString("A5C85286-3AD1-272C-B079-21465A80E3EC")

  def getBlockKnockbackResistanceAmplifier(lvl: Int): AttributeModifier = new AttributeModifier(BLOCK_KNOCKBACK_RESISTANCE_AMPLIFIER_UUID, "Block Knockback Amplifier",
    lvl * 0.005, 0)

  val CONSTITUTION_MAX_HEALTH_AMPLIFIER_UUID: UUID = UUID.fromString("52A2CDE7-5D1F-63E2-ABA3-67D898823535")

  def getConstitutionMaxHealthAmplifier(lvl: Int): AttributeModifier = new AttributeModifier(CONSTITUTION_MAX_HEALTH_AMPLIFIER_UUID, "Constitution Max Health Amplifier",
    lvl * 1, 0)

  val CONSTITUTION_KNOCKBACK_RESISTANCE_AMPLIFIER_UUID: UUID = UUID.fromString("2575F7FD-4135-3762-9A02-A15B073B5857")

  def getConstitutionKnockbackResistanceAmplifier(lvl: Int): AttributeModifier = new AttributeModifier(CONSTITUTION_KNOCKBACK_RESISTANCE_AMPLIFIER_UUID, "Constitution Knockback Amplifier",
    lvl * 0.005, 0)
}
