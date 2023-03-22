package committee.nova.skillsvanilla.item.init

import committee.nova.skillsvanilla.item.impl.ItemTrainingSword
import net.minecraft.item.Item
import net.minecraftforge.event.RegistryEvent

object ItemInit {
  val trainingSword = new ItemTrainingSword

  def init(e: RegistryEvent.Register[Item]): Unit = e.getRegistry.registerAll(trainingSword)
}
