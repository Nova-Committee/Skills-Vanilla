package committee.nova.skillsvanilla.event.handler

import committee.nova.skillsvanilla.network.handler.NetworkHandler
import committee.nova.skillsvanilla.network.message.SwimmingSyncMessage
import net.minecraft.block.material.Material
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.{Phase, PlayerTickEvent}

object FMLClientEventHandler {
  def init(): Unit = FMLCommonHandler.instance().bus().register(new FMLClientEventHandler)
}

class FMLClientEventHandler {
  @SubscribeEvent
  def onClientPlayerTick(event: PlayerTickEvent): Unit = {
    if (event.phase == Phase.START) return
    val player = event.player
    if (player.isInsideOfMaterial(Material.WATER) && (player.motionX != .0 || player.motionY > .0 || player.motionZ != .0)) NetworkHandler.instance.sendToServer(new SwimmingSyncMessage)
  }
}
