package committee.nova.skillsvanilla.util

import net.minecraft.entity.EntityLiving
import net.minecraft.entity.boss.EntityDragon
import net.minecraft.entity.monster.{EntityGhast, EntityMob, EntityShulker, EntitySlime}

object Utilities {
  def isEnemy(entity: EntityLiving): Boolean = {
    entity match {
      case _: EntityMob => true
      case _: EntitySlime => true
      case _: EntityDragon => true
      case _: EntityGhast => true
      case _: EntityShulker => true
      case _ => false
    }
  }
}
