package committee.nova.skillsvanilla.network.message

import committee.nova.skillful.implicits.Implicits.EntityPlayerImplicit
import committee.nova.skillsvanilla.registries.VanillaSkills
import io.netty.buffer.ByteBuf
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fml.common.network.ByteBufUtils
import net.minecraftforge.fml.common.network.simpleimpl.{IMessage, IMessageHandler, MessageContext}
import net.minecraftforge.fml.relauncher.Side

import java.util.UUID
import scala.util.{Random, Try}

object SwimmingSyncMessage {
  final val rand = new Random()

  class Handler extends IMessageHandler[SwimmingSyncMessage, IMessage] {
    override def onMessage(message: SwimmingSyncMessage, ctx: MessageContext): IMessage = {
      if (ctx.side != Side.SERVER) return null
      val player = ctx.getServerHandler.player
      if (!Try(UUID.fromString(message.getTag.getString("uuid"))).getOrElse(new UUID(0L, 0L)).equals(player.getUniqueID)) return null
      player.getServerWorld.addScheduledTask(new Runnable {
        override def run(): Unit = if (rand.nextInt(5) == 0) player.getSkillStat(VanillaSkills.SWIMMING).addXp(player, rand.nextInt(2) + 1)
      })
      null
    }
  }
}

class SwimmingSyncMessage extends IMessage {
  private var tag: NBTTagCompound = new NBTTagCompound

  def getTag: NBTTagCompound = tag

  override def fromBytes(buf: ByteBuf): Unit = tag = ByteBufUtils.readTag(buf)

  override def toBytes(buf: ByteBuf): Unit = ByteBufUtils.writeTag(buf, tag)
}
