package committee.nova.skillsvanilla.item.api

import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack

trait IItemTickable {
  def tick(stack: ItemStack, player: EntityPlayerMP): Unit
}
