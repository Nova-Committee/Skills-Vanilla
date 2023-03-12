package committee.nova.skillsvanilla.event.handler

import committee.nova.skillful.implicits.Implicits.EntityPlayerImplicit
import committee.nova.skillsvanilla.registries.VanillaSkills
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
    if (!event.player.isInstanceOf[EntityPlayerMP]) return
    val p = event.player.asInstanceOf[EntityPlayerMP]
    if (event.phase == Phase.START) return
    // Will related exhaustion
    p.addExhaustion(-0.001F * p.getSkillStat(VanillaSkills.WILL).getCurrentLevel max -p.getFoodStats.foodExhaustionLevel)
  }

  //@SubscribeEvent
  //def onLogin(event: PlayerLoggedInEvent): Unit = {
  //  event.player match {
  //    case e: EntityPlayerMP => {
  //      e.getSkillStat(VanillaSkills.CONSTITUTION).cheat(e)
  //    }
  //  }
  //}
}
