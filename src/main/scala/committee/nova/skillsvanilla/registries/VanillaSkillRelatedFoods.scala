package committee.nova.skillsvanilla.registries

import committee.nova.skillful.skills.SkillRelatedFood
import net.minecraft.init.Items
import net.minecraft.item.ItemFood

object VanillaSkillRelatedFoods {
  val APPLE = new SkillRelatedFood(Items.APPLE.asInstanceOf[ItemFood], (p, i) => Map(
    VanillaSkills.AGILITY -> 1
  ))
}
