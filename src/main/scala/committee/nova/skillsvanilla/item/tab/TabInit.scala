package committee.nova.skillsvanilla.item.tab

import committee.nova.skillsvanilla.SkillsVanilla
import committee.nova.skillsvanilla.item.init.ItemInit
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemStack

object TabInit {
  final val TRAINING = new CreativeTabs(SkillsVanilla.MODID + "." + "training") {
    override def createIcon(): ItemStack = new ItemStack(ItemInit.trainingSword)
  }
}
