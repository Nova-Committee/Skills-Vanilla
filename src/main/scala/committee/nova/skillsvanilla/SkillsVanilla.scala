package committee.nova.skillsvanilla

import committee.nova.skillsvanilla.compat.CompatTools
import committee.nova.skillsvanilla.config.CommonConfig
import committee.nova.skillsvanilla.event.handler.{FMLClientEventHandler, FMLEventHandler, ForgeClientEventHandler, ForgeEventHandler}
import committee.nova.skillsvanilla.event.impl.DamageSourceBlackListEvent
import committee.nova.skillsvanilla.network.handler.NetworkHandler
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event.{FMLInitializationEvent, FMLPostInitializationEvent, FMLPreInitializationEvent}
import net.minecraftforge.fml.common.{FMLCommonHandler, Mod}
import net.minecraftforge.fml.relauncher.Side

@Mod(modid = SkillsVanilla.MODID, useMetadata = true, modLanguage = "scala", dependencies = "required-after:skillful")
object SkillsVanilla {
  final val MODID = "skillsvanilla"

  @EventHandler def preInit(e: FMLPreInitializationEvent): Unit = {
    CommonConfig.init(e)
    FMLEventHandler.init()
    ForgeEventHandler.init()
    if (FMLCommonHandler.instance().getSide == Side.CLIENT) {
      FMLClientEventHandler.init()
      ForgeClientEventHandler.init()
    }
    NetworkHandler.init(e)
  }

  @EventHandler def init(e: FMLInitializationEvent): Unit = {
    MinecraftForge.EVENT_BUS.post(new DamageSourceBlackListEvent)
  }

  @EventHandler def postInit(e: FMLPostInitializationEvent): Unit = {
    CompatTools.init()
  }
}
