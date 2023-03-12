package committee.nova.skillsvanilla.network.handler

import committee.nova.skillsvanilla.SkillsVanilla
import committee.nova.skillsvanilla.network.message.SwimmingSyncMessage
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.fml.common.network.simpleimpl.{IMessage, IMessageHandler, SimpleNetworkWrapper}
import net.minecraftforge.fml.relauncher.Side

object NetworkHandler {
  val instance: SimpleNetworkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(SkillsVanilla.MODID)

  private var nextID: Int = 0

  def init(e: FMLPreInitializationEvent): Unit = registerMessage(classOf[MessageHandler.SwimmingSyncHandler], classOf[SwimmingSyncMessage], Side.SERVER)

  def registerMessage[REQ <: IMessage, REPLY <: IMessage](msgHandler: Class[_ <: IMessageHandler[REQ, REPLY]], requestMsgType: Class[REQ], side: Side): Unit = {
    nextID += 1
    instance.registerMessage(msgHandler, requestMsgType, nextID, side)
  }
}
