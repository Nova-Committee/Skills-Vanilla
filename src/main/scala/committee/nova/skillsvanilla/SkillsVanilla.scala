package committee.nova.skillsvanilla

import committee.nova.skillsvanilla.event.handler.{FMLClientEventHandler, FMLEventHandler, ForgeClientEventHandler, ForgeEventHandler}
import committee.nova.skillsvanilla.network.handler.NetworkHandler
import committee.nova.skillsvanilla.util.Utilities
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event.{FMLPostInitializationEvent, FMLPreInitializationEvent}
import net.minecraftforge.fml.common.{FMLCommonHandler, Mod}
import net.minecraftforge.fml.relauncher.Side

import scala.util.Try

@Mod(modid = SkillsVanilla.MODID, useMetadata = true, modLanguage = "scala", dependencies = "required-after:skillful")
object SkillsVanilla {
  final val MODID = "skillsvanilla"

  @EventHandler def preInit(e: FMLPreInitializationEvent): Unit = {
    FMLEventHandler.init()
    ForgeEventHandler.init()
    if (FMLCommonHandler.instance().getSide == Side.CLIENT) {
      FMLClientEventHandler.init()
      ForgeClientEventHandler.init()
    }
    NetworkHandler.init(e)
  }

  @EventHandler def postInit(e: FMLPostInitializationEvent): Unit = {
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
  }
}
