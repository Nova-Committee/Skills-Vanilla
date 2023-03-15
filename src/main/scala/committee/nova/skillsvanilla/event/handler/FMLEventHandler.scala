package committee.nova.skillsvanilla.event.handler

import committee.nova.skillful.implicits.Implicits.EntityPlayerImplicit
import committee.nova.skillsvanilla.implicits.Implicits.PlayerImplicit
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
    val player = event.player
    if (!player.isInstanceOf[EntityPlayerMP]) return
    val p = player.asInstanceOf[EntityPlayerMP]
    if (event.phase == Phase.START) return
    // Will related exhaustion
    p.addExhaustion(-0.001F * p.getSkillStat(VanillaSkills.WILL).getCurrentLevel max -p.getFoodStats.foodExhaustionLevel)
    if (p.isHiding) p.getSkillStat(VanillaSkills.STEALTH).addXp(p, 1)
  }
}
