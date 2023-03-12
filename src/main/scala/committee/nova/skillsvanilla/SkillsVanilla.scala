package committee.nova.skillsvanilla

import committee.nova.skillsvanilla.event.handler.{FMLClientEventHandler, FMLEventHandler, ForgeEventHandler}
import committee.nova.skillsvanilla.network.handler.NetworkHandler
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.{FMLCommonHandler, Mod}
import net.minecraftforge.fml.relauncher.Side

@Mod(modid = SkillsVanilla.MODID, useMetadata = true, modLanguage = "scala", dependencies = "required-after:skillful")
object SkillsVanilla {
  final val MODID = "skillsvanilla"

  @EventHandler def preInit(e: FMLPreInitializationEvent): Unit = {
    FMLEventHandler.init()
    if (FMLCommonHandler.instance().getSide == Side.CLIENT) FMLClientEventHandler.init()
    ForgeEventHandler.init()
    NetworkHandler.init(e)
  }
}
