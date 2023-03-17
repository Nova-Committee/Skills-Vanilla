package committee.nova.skillsvanilla.implicits

import net.minecraft.entity.monster.EntityMob
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.math.AxisAlignedBB

import java.util.{List => JList}
import scala.collection.JavaConverters.collectionAsScalaIterableConverter

object Implicits {
  implicit class PlayerImplicit(val player: EntityPlayer) {
    def getStealthEffect: Int = {
      if (!player.isSneaking || player.world.getWorldTime % 20 != 0) return 0
      val mobs = getMobsWithIn(16.0)
      var freeMob = false
      var isSeen = false
      var targeted = false
      mobs.asScala.foreach(m => {
        val target = m.getAttackTarget
        val seen = m.canEntityBeSeen(player)
        if (seen) {
          if (target == null) freeMob = true
          if (target == player) targeted = true
          isSeen = true
        }
      })
      if (freeMob && !targeted) 2 else if (isSeen) 1 else 0
    }

    def getTargeting(r: Double): Int = getMobsWithIn(r).asScala.count(m => player == m.getAttackTarget)

    def getMobsWithIn(r: Double): JList[EntityMob] = player.world.getEntitiesWithinAABB(classOf[EntityMob], new AxisAlignedBB(player.posX - r, player.posY - r, player.posZ - r, player.posX + r, player.posY + r, player.posZ + r))
  }
}
