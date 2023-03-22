package committee.nova.skillsvanilla.item.impl

import committee.nova.skillful.implicits.Implicits.EntityPlayerImplicit
import committee.nova.skillsvanilla.SkillsVanilla
import committee.nova.skillsvanilla.implicits.Implicits.{ItemStackImplicit, PlayerImplicit}
import committee.nova.skillsvanilla.item.api.IItemTickable
import committee.nova.skillsvanilla.item.tab.TabInit
import committee.nova.skillsvanilla.registries.VanillaSkills
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.{EntityPlayer, EntityPlayerMP}
import net.minecraft.init.SoundEvents
import net.minecraft.item.{IItemPropertyGetter, Item, ItemStack}
import net.minecraft.util.text.{Style, TextComponentString, TextComponentTranslation, TextFormatting}
import net.minecraft.util.{ActionResult, EnumActionResult, EnumHand, ResourceLocation}
import net.minecraft.world.World

import java.lang.{Double => JD}
import java.text.MessageFormat
import java.util

class ItemRSTD extends Item with IItemTickable {
  setCreativeTab(TabInit.TRAINING)
  setTranslationKey(SkillsVanilla.MODID + "." + "rstd")
  setRegistryName(SkillsVanilla.MODID + ":" + "rstd")
  setMaxStackSize(1)
  addPropertyOverride(new ResourceLocation(SkillsVanilla.MODID, "status"), new IItemPropertyGetter {
    override def apply(stack: ItemStack, worldIn: World, entityIn: EntityLivingBase): Float = {
      val tag = stack.getOrCreateTag
      if (!tag.getBoolean("activated")) return 0.0F
      if (tag.getInteger("timeLeft") > 0) 1.0F else 2.0F
    }
  })

  override def tick(stack: ItemStack, player: EntityPlayerMP): Unit = {
    val tag = stack.getOrCreateTag
    if (!tag.getBoolean("activated")) return
    val time = tag.getInteger("timeLeft")
    if (time > -50) {
      if (time == 1) player.playPacketSound(SoundEvents.BLOCK_NOTE_BELL)
      tag.setInteger("timeLeft", time - 1)
    }
    else {
      player.playPacketSound(SoundEvents.BLOCK_STONE_BUTTON_CLICK_OFF)
      player.sendMessage(new TextComponentTranslation("msg.skillsvanilla.rstd.timeout")
        .setStyle(new Style().setColor(TextFormatting.DARK_RED)))
      tag.setInteger("timeLeft", 0)
      tag.setBoolean("activated", false)
    }
  }

  override def onItemRightClick(worldIn: World, playerIn: EntityPlayer, handIn: EnumHand): ActionResult[ItemStack] = {
    playerIn match {
      case player: EntityPlayerMP =>
        val stack = player.getHeldItem(handIn)
        val tag = stack.getOrCreateTag
        if (!tag.getBoolean("activated")) {
          tag.setInteger("timeLeft", 40 + player.getRNG.nextInt(161))
          tag.setBoolean("activated", true)
          player.playPacketSound(SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON)
        } else {
          val time = tag.getInteger("timeLeft")
          if (time > 0) {
            tag.setInteger("timeLeft", 0)
            tag.setBoolean("activated", false)
            player.playPacketSound(SoundEvents.BLOCK_STONE_BUTTON_CLICK_OFF)
          } else {
            val action = time.abs
            tag.setInteger("timeLeft", 0)
            tag.setBoolean("activated", false)
            player.playPacketSound(SoundEvents.BLOCK_STONE_BUTTON_CLICK_OFF)
            val xp = Math.pow(1.28, (50 - action) / 5.0).toInt
            player.getSkillStat(VanillaSkills.AGILITY).addXp(player, xp)
            player.sendMessage(new TextComponentString(
              MessageFormat.format(new TextComponentTranslation("msg.skillsvanilla.rstd.success")
                .setStyle(new Style().setColor(TextFormatting.GOLD)).getFormattedText,
                String.format("%.2f", JD.valueOf(action / 20.0)),
                xp.toString
              )).setStyle(new Style().setColor(TextFormatting.GOLD)))
          }
        }
        new ActionResult(EnumActionResult.SUCCESS, stack)
      case _ => new ActionResult(EnumActionResult.PASS, playerIn.getHeldItem(handIn))
    }
  }

  override def addInformation(stack: ItemStack, worldIn: World, tooltip: util.List[String], flagIn: ITooltipFlag): Unit = {
    tooltip.add(new TextComponentTranslation("tooltip.skillsvanilla.rstd")
      .setStyle(new Style().setColor(TextFormatting.DARK_GREEN)).getFormattedText)
  }

  override def shouldCauseReequipAnimation(oldStack: ItemStack, newStack: ItemStack, slotChanged: Boolean): Boolean = slotChanged
}
