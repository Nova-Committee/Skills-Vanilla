package committee.nova.skillsvanilla.network.message

import committee.nova.skillful.implicits.Implicits.EntityPlayerImplicit
import committee.nova.skillsvanilla.registries.VanillaSkills
import io.netty.buffer.ByteBuf
import net.minecraftforge.fml.common.network.simpleimpl.{IMessage, IMessageHandler, MessageContext}
import net.minecraftforge.fml.relauncher.Side

import scala.util.Random

object SwimmingSyncMessage {
  final val rand = new Random()

  class Handler extends IMessageHandler[SwimmingSyncMessage, IMessage] {
    override def onMessage(message: SwimmingSyncMessage, ctx: MessageContext): IMessage = {
      if (ctx.side != Side.SERVER) return null
      val player = ctx.getServerHandler.player
      player.getServerWorld.addScheduledTask(new Runnable {
        override def run(): Unit = if (rand.nextInt(5) == 0) player.getSkillStat(VanillaSkills.SWIMMING).addXp(player, rand.nextInt(2) + 1)
      })
      null
    }
  }
}

class SwimmingSyncMessage extends IMessage {
  override def fromBytes(buf: ByteBuf): Unit = {}

  override def toBytes(buf: ByteBuf): Unit = {}
}
