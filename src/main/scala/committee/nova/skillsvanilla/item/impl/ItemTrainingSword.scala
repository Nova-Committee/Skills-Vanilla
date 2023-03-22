package committee.nova.skillsvanilla.item.impl

import committee.nova.skillsvanilla.SkillsVanilla
import committee.nova.skillsvanilla.item.api.IItemForTraining
import committee.nova.skillsvanilla.item.impl.ItemTrainingSword.TRAINING_WOOD
import committee.nova.skillsvanilla.item.tab.TabInit
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.init.Blocks
import net.minecraft.item.Item.ToolMaterial
import net.minecraft.item.{Item, ItemStack, ItemSword}
import net.minecraft.util.text.{Style, TextComponentTranslation, TextFormatting}
import net.minecraft.world.World
import net.minecraftforge.common.util.EnumHelper

import java.util

object ItemTrainingSword {
  private val TRAINING_WOOD: ToolMaterial = EnumHelper.addToolMaterial("training_wood", 0, 1000, .0F, -3.0F, 0)
  TRAINING_WOOD.setRepairItem(new ItemStack(Item.getItemFromBlock(Blocks.PLANKS)))
}

class ItemTrainingSword extends ItemSword(TRAINING_WOOD) with IItemForTraining {
  setCreativeTab(TabInit.TRAINING)
  setTranslationKey(SkillsVanilla.MODID + "." + "training_sword")
  setRegistryName(SkillsVanilla.MODID + ":" + "training_sword")
  setMaxDamage(1000)

  override def getAttackDamage: Float = .0F

  override def addInformation(stack: ItemStack, worldIn: World, tooltip: util.List[String], flagIn: ITooltipFlag): Unit = {
    tooltip.add(new TextComponentTranslation("tooltip.skillsvanilla.training_sword")
      .setStyle(new Style().setColor(TextFormatting.DARK_GREEN)).getFormattedText)
  }
}
