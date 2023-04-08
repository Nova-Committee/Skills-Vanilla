package committee.nova.skillsvanilla.event.handler

import committee.nova.skillful.implicits.Implicits.EntityPlayerImplicit
import committee.nova.skillsvanilla.implicits.Implicits.PlayerImplicit
import committee.nova.skillsvanilla.item.api.IItemTickable
import committee.nova.skillsvanilla.network.handler.NetworkHandler
import committee.nova.skillsvanilla.network.message.SwimmingSyncMessage.rand
import committee.nova.skillsvanilla.network.message.TargetSyncMessage
import committee.nova.skillsvanilla.registries.VanillaSkills
import committee.nova.skillsvanilla.registries.VanillaSkills.{EQUESTRIANISM, PERCEPTION, SWIMMING}
import net.minecraft.entity.passive.AbstractHorse
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.{Phase, PlayerTickEvent}

object FMLEventHandler {
  def init(): Unit = FMLCommonHandler.instance().bus().register(new FMLEventHandler)
}

class FMLEventHandler {
  @SubscribeEvent
  def onPlayerTick(event: PlayerTickEvent): Unit = {
    val player = event.player
    if (!player.isInstanceOf[EntityPlayerMP]) return
    val p = player.asInstanceOf[EntityPlayerMP]
    if (event.phase == Phase.START) return
    // Tickable Items
    val inv = p.inventory
    val size = inv.getSizeInventory
    for (i <- 0 until size) {
      val stack = inv.getStackInSlot(i)
      stack.getItem match {
        case t: IItemTickable => t.tick(stack, p)
        case _ =>
      }
    }
    // Will related exhaustion
    p.addExhaustion(-0.001F * p.getSkillStat(VanillaSkills.WILL).getCurrentLevel max -p.getFoodStats.foodExhaustionLevel)
    // Stealth check
    p.getSkillStat(VanillaSkills.STEALTH).addXp(p, p.getStealthEffect)
    // Swimming related air change
    val air = p.getAir
    val airPercent = air / 300.0
    if ((p.getSkillStat(SWIMMING).getCurrentLevel / 200.0) * (1.0 - airPercent) > p.getRNG.nextDouble()) p.setAir((air + 1) min 300)
    // Targeting check
    val world = p.world
    if (world.getWorldTime % 5 == 0) {
      val perception = p.getSkillStat(PERCEPTION).getCurrentLevel
      if (perception > 10) {
        val msg = new TargetSyncMessage
        val size = p.getTargeting(8.0 + (0.16 * (perception - 20)) max .0)
        msg.setTargeting(if (perception > 15) size else if (size > 0) -1 else 0)
        NetworkHandler.instance.sendTo(msg, p)
      }
    }
    p.getRidingEntity match {
      case h: AbstractHorse => if (rand.nextInt(10) == 0) p.getSkillStat(EQUESTRIANISM).addXp(p, rand.nextInt(2) + 1)
      case _ =>
    }
  }
}
