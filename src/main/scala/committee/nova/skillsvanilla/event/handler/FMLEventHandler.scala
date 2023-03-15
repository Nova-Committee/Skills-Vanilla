package committee.nova.skillsvanilla.event.handler

import committee.nova.skillful.implicits.Implicits.EntityPlayerImplicit
import committee.nova.skillsvanilla.implicits.Implicits.PlayerImplicit
import committee.nova.skillsvanilla.registries.VanillaSkills
import committee.nova.skillsvanilla.registries.VanillaSkills.SWIMMING
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.{Phase, PlayerTickEvent}

import scala.util.Random

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
    p.getSkillStat(VanillaSkills.STEALTH).addXp(p, p.getStealthEffect)
    val air = p.getAir
    val airPercent = air / 300.0
    if ((p.getSkillStat(SWIMMING).getCurrentLevel / 200.0) * (1.0 - airPercent) > new Random().nextDouble()) p.setAir((air + 1) min 300)
  }
}
