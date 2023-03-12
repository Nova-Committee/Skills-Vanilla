package committee.nova.skillsvanilla.network.message

import io.netty.buffer.ByteBuf
import net.minecraftforge.fml.common.network.simpleimpl.IMessage

class SwimmingSyncMessage extends IMessage {
  override def fromBytes(buf: ByteBuf): Unit = {}

  override def toBytes(buf: ByteBuf): Unit = {}
}
