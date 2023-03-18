package committee.nova.skillsvanilla.network.message

import committee.nova.skillsvanilla.SkillsVanilla
import io.netty.buffer.ByteBuf
import net.minecraft.client.Minecraft
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fml.common.network.simpleimpl.{IMessage, IMessageHandler, MessageContext}
import net.minecraftforge.fml.relauncher.Side

object TargetSyncMessage {
  class Handler extends IMessageHandler[TargetSyncMessage, IMessage] {
    override def onMessage(message: TargetSyncMessage, ctx: MessageContext): IMessage = {
      if (ctx.side != Side.CLIENT) return null
      val mc = Minecraft.getMinecraft
      mc.addScheduledTask(new Runnable {
        override def run(): Unit = {
          val player = Minecraft.getMinecraft.player
          val tag = new NBTTagCompound
          tag.setInteger("targeting", message.targeting)
          player.getEntityData.setTag(SkillsVanilla.MODID, tag)
        }
      })
      null
    }
  }
}

class TargetSyncMessage extends IMessage {
  private var targeting: Int = 0

  def setTargeting(t: Int): Unit = targeting = t

  override def fromBytes(buf: ByteBuf): Unit = targeting = buf.readInt()

  override def toBytes(buf: ByteBuf): Unit = buf.writeInt(targeting)
}
