package committee.nova.skillsvanilla.network.handler

import committee.nova.skillful.implicits.Implicits.EntityPlayerImplicit
import committee.nova.skillsvanilla.network.message.SwimmingSyncMessage
import committee.nova.skillsvanilla.registries.VanillaSkills
import net.minecraftforge.fml.common.network.simpleimpl.{IMessage, IMessageHandler, MessageContext}
import net.minecraftforge.fml.relauncher.Side

import scala.util.Random

object MessageHandler {
  class SwimmingSyncHandler extends IMessageHandler[SwimmingSyncMessage, IMessage] {
    final val rand = new Random()

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
