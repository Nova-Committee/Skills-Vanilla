package committee.nova.skillsvanilla.implicits

import net.minecraft.entity.monster.EntityMob
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.math.AxisAlignedBB

import scala.collection.JavaConverters.collectionAsScalaIterableConverter

object Implicits {
  implicit class PlayerImplicit(val player: EntityPlayer) {
    def getStealthEffect: Int = {
      if (!player.isSneaking || player.world.getWorldTime % 20 != 0) return 0
      val mobs = player.world.getEntitiesWithinAABB(classOf[EntityMob], new AxisAlignedBB(player.posX - 16.0, player.posY - 16.0, player.posZ - 16.0, player.posX + 16.0, player.posY + 16.0, player.posZ + 16.0))
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
  }
}
