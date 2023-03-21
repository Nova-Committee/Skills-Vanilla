package committee.nova.skillsvanilla.util

import net.minecraft.entity.EntityLiving
import net.minecraft.entity.boss.EntityDragon
import net.minecraft.entity.monster.{EntityGhast, EntityMob, EntityShulker, EntitySlime}
import net.minecraft.item.{Item, ItemAxe, ItemPickaxe}

import scala.collection.mutable

object Utilities {
  private val pickaxeClasses: mutable.HashSet[Class[_]] = new mutable.HashSet[Class[_]]()
  private val axeClasses: mutable.HashSet[Class[_]] = new mutable.HashSet[Class[_]]()

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

  def addPickaxe(clz: Class[_]): Boolean = pickaxeClasses.add(clz)

  def addAxe(clz: Class[_]): Boolean = axeClasses.add(clz)

  def isPickaxe(item: Item): Boolean = {
    if (item.isInstanceOf[ItemPickaxe]) return true
    pickaxeClasses.foreach(c => if (c.isAssignableFrom(item.getClass)) return true)
    false
  }

  def isAxe(item: Item): Boolean = {
    if (item.isInstanceOf[ItemAxe]) return true
    axeClasses.foreach(c => if (c.isAssignableFrom(item.getClass)) return true)
    false
  }
}
