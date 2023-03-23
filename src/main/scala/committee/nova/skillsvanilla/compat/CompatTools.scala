package committee.nova.skillsvanilla.compat

import committee.nova.skillsvanilla.util.Utilities

import scala.util.Try

object CompatTools {
  def init(): Unit = {
    Try(Class.forName("mekanism.tools.item.ItemMekanismPaxel")).foreach(c => {
      Utilities.addPickaxe(c)
      Utilities.addAxe(c)
    })
    Try(Class.forName("mekanism.common.item.ItemAtomicDisassembler")).foreach(c => {
      Utilities.addPickaxe(c)
      Utilities.addAxe(c)
    })
    Try(Class.forName("slimeknights.tconstruct.tools.tools.Pickaxe")).foreach(c => Utilities.addPickaxe(c))
    Try(Class.forName("slimeknights.tconstruct.tools.tools.LumberAxe")).foreach(c => Utilities.addAxe(c))
    Try(Class.forName("slimeknights.tconstruct.tools.tools.Mattock")).foreach(c => Utilities.addAxe(c))
    Try(Class.forName("slimeknights.tconstruct.tools.tools.Hatchet")).foreach(c => Utilities.addAxe(c))
    Try(Class.forName("ic2.core.item.tool.ItemDrill")).foreach(c => Utilities.addPickaxe(c))
    Try(Class.forName("ic2.core.item.tool.ItemElectricToolChainsaw")).foreach(c => Utilities.addAxe(c))
    Try(Class.forName("blusunrize.immersiveengineering.common.items.ItemDrill")).foreach(c => Utilities.addPickaxe(c))
    Try(Class.forName("com.buuz135.industrial.item.infinity.ItemInfinityDrill")).foreach(c => Utilities.addPickaxe(c)) // Industrial Foregoing
  }
}
