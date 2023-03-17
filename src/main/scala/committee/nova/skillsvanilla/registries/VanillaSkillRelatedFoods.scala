package committee.nova.skillsvanilla.registries

import committee.nova.skillful.impl.related.SkillRelatedFood
import net.minecraft.init.Items
import net.minecraft.item.ItemFood

import scala.util.Random

object VanillaSkillRelatedFoods {
  val rand = new Random()
  val APPLE = new SkillRelatedFood(Items.APPLE.asInstanceOf[ItemFood], (_, _) => Map(
    VanillaSkills.AGILITY -> rand.nextInt(3),
    VanillaSkills.CONSTITUTION -> rand.nextInt(2)
  ))
  val MUSHROOM_STEW = new SkillRelatedFood(Items.MUSHROOM_STEW.asInstanceOf[ItemFood], (_, _) => Map(
    VanillaSkills.STRENGTH -> rand.nextInt(2),
    VanillaSkills.CONSTITUTION -> rand.nextInt(3)
  ))
  val BREAD = new SkillRelatedFood(Items.BREAD.asInstanceOf[ItemFood], (_, _) => Map(
    VanillaSkills.WILL -> rand.nextInt(2)
  ))
  val RAW_PORKCHOP = new SkillRelatedFood(Items.PORKCHOP.asInstanceOf[ItemFood], (_, _) => Map(
    VanillaSkills.STRENGTH -> rand.nextInt(5),
    VanillaSkills.WILL -> rand.nextInt(4),
    VanillaSkills.CONSTITUTION -> (rand.nextInt(5) - 1)
  ))
  val COOKED_PORKCHOP = new SkillRelatedFood(Items.COOKED_PORKCHOP.asInstanceOf[ItemFood], (_, _) => Map(
    VanillaSkills.STRENGTH -> rand.nextInt(8),
    VanillaSkills.WILL -> (rand.nextInt(2) - 1),
    VanillaSkills.CONSTITUTION -> rand.nextInt(7)
  ))
  val GOLDEN_APPLE = new SkillRelatedFood(Items.GOLDEN_APPLE.asInstanceOf[ItemFood], (_, i) => {
    val isEnhanced = i.getMetadata == 1
    Map(
      VanillaSkills.STRENGTH -> (1 + rand.nextInt(if (isEnhanced) 20 else 3)),
      VanillaSkills.WILL -> (1 + rand.nextInt(if (isEnhanced) 20 else 3)),
      VanillaSkills.CONSTITUTION -> (2 + rand.nextInt(if (isEnhanced) 20 else 3)),
      VanillaSkills.AGILITY -> (1 + rand.nextInt(if (isEnhanced) 30 else 5)),
      VanillaSkills.LUCK -> rand.nextInt(if (isEnhanced) 20 else 3)
    )
  })
  val RAW_FISH = new SkillRelatedFood(Items.FISH.asInstanceOf[ItemFood], (_, i) =>
    i.getMetadata match {
      case 0 => Map(
        VanillaSkills.WILL -> rand.nextInt(3),
        VanillaSkills.CONSTITUTION -> (rand.nextInt(2) - 1),
        VanillaSkills.STRENGTH -> (rand.nextInt(3) - 1)
      )
      case 1 => Map(
        VanillaSkills.WILL -> rand.nextInt(3),
        VanillaSkills.CONSTITUTION -> (rand.nextInt(3) - 1),
        VanillaSkills.STRENGTH -> (rand.nextInt(2) - 1)
      )
      case _ => Map(
        VanillaSkills.WILL -> rand.nextInt(2),
        VanillaSkills.CONSTITUTION -> (rand.nextInt(2) - 1),
        VanillaSkills.STRENGTH -> (rand.nextInt(2) - 1)
      )
    }
  )
  val COOKED_FISH = new SkillRelatedFood(Items.COOKED_FISH.asInstanceOf[ItemFood], (_, i) => i.getMetadata match {
    case 0 => Map(
      VanillaSkills.WILL -> rand.nextInt(2),
      VanillaSkills.CONSTITUTION -> rand.nextInt(5),
      VanillaSkills.STRENGTH -> rand.nextInt(6)
    )
    case _ => Map(
      VanillaSkills.WILL -> rand.nextInt(2),
      VanillaSkills.CONSTITUTION -> rand.nextInt(6),
      VanillaSkills.STRENGTH -> rand.nextInt(5)
    )
  })
  val COOKIE = new SkillRelatedFood(Items.COOKIE.asInstanceOf[ItemFood], (_, _) => Map(
    VanillaSkills.WILL -> (rand.nextInt(2) - 1),
    VanillaSkills.STRENGTH -> (rand.nextInt(3) - 1),
    VanillaSkills.CONSTITUTION -> (rand.nextInt(3) - 1)
  ))
  val MELON = new SkillRelatedFood(Items.MELON.asInstanceOf[ItemFood], (_, _) => Map(
    VanillaSkills.WILL -> (rand.nextInt(2) - 1)
  ))
  val RAW_BEEF = new SkillRelatedFood(Items.BEEF.asInstanceOf[ItemFood], (_, _) => Map(
    VanillaSkills.STRENGTH -> rand.nextInt(5),
    VanillaSkills.WILL -> rand.nextInt(3),
    VanillaSkills.CONSTITUTION -> (rand.nextInt(5) - 1)
  ))
  val COOKED_BEEF = new SkillRelatedFood(Items.COOKED_BEEF.asInstanceOf[ItemFood], (_, _) => Map(
    VanillaSkills.STRENGTH -> rand.nextInt(8),
    VanillaSkills.WILL -> (rand.nextInt(2) - 1),
    VanillaSkills.CONSTITUTION -> rand.nextInt(7)
  ))
  val RAW_CHICKEN = new SkillRelatedFood(Items.CHICKEN.asInstanceOf[ItemFood], (_, _) => Map(
    VanillaSkills.STRENGTH -> (rand.nextInt(3) - 1),
    VanillaSkills.WILL -> rand.nextInt(5),
    VanillaSkills.CONSTITUTION -> (rand.nextInt(3) - 1)
  ))
  val COOKED_CHICKEN = new SkillRelatedFood(Items.COOKED_CHICKEN.asInstanceOf[ItemFood], (_, _) => Map(
    VanillaSkills.STRENGTH -> rand.nextInt(3),
    VanillaSkills.WILL -> (rand.nextInt(3) - 1),
    VanillaSkills.CONSTITUTION -> rand.nextInt(3)
  ))
  val ROTTEN_FLESH = new SkillRelatedFood(Items.ROTTEN_FLESH.asInstanceOf[ItemFood], (_, _) => Map(
    VanillaSkills.WILL -> rand.nextInt(10),
    VanillaSkills.STRENGTH -> (rand.nextInt(3) - 2),
    VanillaSkills.CONSTITUTION -> (rand.nextInt(3) - 2)
  ))
  val SPIDER_EYE = new SkillRelatedFood(Items.SPIDER_EYE.asInstanceOf[ItemFood], (_, _) => Map(
    VanillaSkills.WILL -> rand.nextInt(10),
    VanillaSkills.STRENGTH -> (rand.nextInt(4) - 3),
    VanillaSkills.CONSTITUTION -> (rand.nextInt(4) - 3),
    VanillaSkills.PERCEPTION -> rand.nextInt(10)
  ))
  val CARROT = new SkillRelatedFood(Items.CARROT.asInstanceOf[ItemFood], (_, _) => Map(
    VanillaSkills.CONSTITUTION -> rand.nextInt(2),
    VanillaSkills.PERCEPTION -> rand.nextInt(5)
  ))
  val GOLDEN_CARROT = new SkillRelatedFood(Items.GOLDEN_CARROT.asInstanceOf[ItemFood], (_, _) => Map(
    VanillaSkills.CONSTITUTION -> rand.nextInt(3),
    VanillaSkills.PERCEPTION -> rand.nextInt(8),
    VanillaSkills.LUCK -> rand.nextInt(3)
  ))
  val POTATO = new SkillRelatedFood(Items.POTATO.asInstanceOf[ItemFood], (_, _) => Map(
    VanillaSkills.WILL -> rand.nextInt(3),
    VanillaSkills.CONSTITUTION -> (rand.nextInt(2) - 1),
    VanillaSkills.STRENGTH -> (rand.nextInt(2) - 1)
  ))
  val BAKED_POTATO = new SkillRelatedFood(Items.BAKED_POTATO.asInstanceOf[ItemFood], (_, _) => Map(
    VanillaSkills.WILL -> (rand.nextInt(2) - 1),
    VanillaSkills.CONSTITUTION -> rand.nextInt(2),
    VanillaSkills.STRENGTH -> rand.nextInt(2)
  ))
  val POISONOUS_POTATO = new SkillRelatedFood(Items.POISONOUS_POTATO.asInstanceOf[ItemFood], (_, _) => Map(
    VanillaSkills.WILL -> rand.nextInt(6),
    VanillaSkills.CONSTITUTION -> (rand.nextInt(2) - 2),
    VanillaSkills.STRENGTH -> (rand.nextInt(2) - 2)
  ))
  val PUMPKIN_PIE = new SkillRelatedFood(Items.PUMPKIN_PIE.asInstanceOf[ItemFood], (_, _) => Map(
    VanillaSkills.WILL -> (rand.nextInt(2) - 1),
    VanillaSkills.CONSTITUTION -> (rand.nextInt(3) + 1)
  ))
  val RAW_RABBIT = new SkillRelatedFood(Items.RABBIT.asInstanceOf[ItemFood], (_, _) => Map(
    VanillaSkills.WILL -> rand.nextInt(5),
    VanillaSkills.CONSTITUTION -> (rand.nextInt(3) - 1),
    VanillaSkills.STRENGTH -> (rand.nextInt(3) - 1),
    VanillaSkills.AGILITY -> rand.nextInt(2)
  ))
  val COOKED_RABBIT = new SkillRelatedFood(Items.COOKED_RABBIT.asInstanceOf[ItemFood], (_, _) => Map(
    VanillaSkills.WILL -> rand.nextInt(2),
    VanillaSkills.CONSTITUTION -> rand.nextInt(3),
    VanillaSkills.STRENGTH -> rand.nextInt(3),
    VanillaSkills.AGILITY -> rand.nextInt(2)
  ))
  val RABBIT_STEW = new SkillRelatedFood(Items.RABBIT_STEW.asInstanceOf[ItemFood], (_, _) => Map(
    VanillaSkills.WILL -> (rand.nextInt(2) - 1),
    VanillaSkills.CONSTITUTION -> rand.nextInt(12),
    VanillaSkills.STRENGTH -> rand.nextInt(7),
    VanillaSkills.AGILITY -> rand.nextInt(2)
  ))
  val RAW_MUTTON = new SkillRelatedFood(Items.MUTTON.asInstanceOf[ItemFood], (_, _) => Map(
    VanillaSkills.WILL -> rand.nextInt(5),
    VanillaSkills.STRENGTH -> (rand.nextInt(4) - 1),
    VanillaSkills.CONSTITUTION -> (rand.nextInt(5) - 1)
  ))
  val COOKED_MUTTON = new SkillRelatedFood(Items.COOKED_MUTTON.asInstanceOf[ItemFood], (_, _) => Map(
    VanillaSkills.WILL -> (rand.nextInt(3) - 1),
    VanillaSkills.STRENGTH -> rand.nextInt(5),
    VanillaSkills.CONSTITUTION -> rand.nextInt(8)
  ))
  val BEETROOT = new SkillRelatedFood(Items.BEETROOT.asInstanceOf[ItemFood], (_, _) => Map(
    VanillaSkills.CONSTITUTION -> rand.nextInt(2)
  ))

  val BEETROOT_SOUP = new SkillRelatedFood(Items.BEETROOT_SOUP.asInstanceOf[ItemFood], (_, _) => Map(
    VanillaSkills.CONSTITUTION -> rand.nextInt(7)
  ))
}
