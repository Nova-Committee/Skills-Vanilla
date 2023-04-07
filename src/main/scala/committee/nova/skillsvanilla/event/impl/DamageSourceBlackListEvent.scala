package committee.nova.skillsvanilla.event.impl


import committee.nova.skillsvanilla.event.impl.DamageSourceBlackListEvent.{addDmgSrcClsToBlackList, addDmgSrcFuncToBlackList}
import net.minecraft.util.DamageSource
import net.minecraftforge.fml.common.eventhandler.Event

import java.util.function.Predicate
import scala.collection.mutable
import scala.util.Try

object DamageSourceBlackListEvent {
  private val blacklist: mutable.HashSet[Class[_]] = new mutable.HashSet[Class[_]]()
  private val blackListFunc: mutable.HashSet[DamageSource => Boolean] = new mutable.HashSet[DamageSource => Boolean]()

  private def addDmgSrcClsToBlackList(className: String): Boolean = Try(Class.forName(className)).toOption match {
    case Some(c) => blacklist.add(c)
    case None => false
  }

  private def addDmgSrcFuncToBlackList(fun: DamageSource => Boolean): Boolean = blackListFunc.add(fun)

  private def addDmgSrcFuncToBlackList(fun: Predicate[DamageSource]): Boolean = blackListFunc.add(d => fun.test(d))

  def isInBlackList(src: DamageSource): Boolean = {
    blacklist.foreach(c => if (c.isAssignableFrom(src.getClass)) return true)
    blackListFunc.foreach(f => if (f.apply(src)) return true)
    false
  }
}

class DamageSourceBlackListEvent extends Event {
  def addDamageSourceClassToBlackList(className: String): Boolean = addDmgSrcClsToBlackList(className)

  def addDamageSourceFunctionToBlackList(fun: DamageSource => Boolean): Boolean = addDmgSrcFuncToBlackList(fun)

  def addDamageSourceFunctionToBlackList(fun: Predicate[DamageSource]): Boolean = addDmgSrcFuncToBlackList(fun)
}
