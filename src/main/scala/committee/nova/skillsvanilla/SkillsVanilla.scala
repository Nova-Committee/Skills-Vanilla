package committee.nova.skillsvanilla

import committee.nova.skillsvanilla.compat.CompatTools
import committee.nova.skillsvanilla.event.handler.{FMLClientEventHandler, FMLEventHandler, ForgeClientEventHandler, ForgeEventHandler}
import committee.nova.skillsvanilla.network.handler.NetworkHandler
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event.{FMLPostInitializationEvent, FMLPreInitializationEvent}
import net.minecraftforge.fml.common.{FMLCommonHandler, Mod}
import net.minecraftforge.fml.relauncher.Side

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
    CompatTools.init()
  }
}
