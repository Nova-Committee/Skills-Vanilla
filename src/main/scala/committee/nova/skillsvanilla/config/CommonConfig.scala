package committee.nova.skillsvanilla.config

import net.minecraftforge.common.config.Configuration
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent

object CommonConfig {
  private var cfg: Configuration = _
  private var blackList: Array[String] = Array("committee.nova.neutron.server.player.damageSource.DamageSourceSuicide", "com.example.ExampleDamageSource")

  def init(e: FMLPreInitializationEvent): Unit = {
    cfg = new Configuration(e.getSuggestedConfigurationFile)
    cfg.load()
    blackList = cfg.getStringList("dmgSrcBlackList", Configuration.CATEGORY_GENERAL,
      Array("committee.nova.neutron.server.player.damageSource.DamageSourceSuicide", "com.example.ExampleDamageSource"),
      "DamageSources that not counted in CON / WILL calculation")
    cfg.save()
  }

  def getBlackList: Array[String] = blackList
}
