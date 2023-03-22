package committee.nova.skillsvanilla.event.handler

import committee.nova.skillsvanilla.SkillsVanilla
import committee.nova.skillsvanilla.event.handler.ForgeClientEventHandler.texTarget
import committee.nova.skillsvanilla.item.init.ItemInit
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.{Gui, ScaledResolution}
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.TextComponentTranslation
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType
import net.minecraftforge.client.event.{ModelRegistryEvent, RenderGameOverlayEvent}
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object ForgeClientEventHandler {
  def init(): Unit = MinecraftForge.EVENT_BUS.register(new ForgeClientEventHandler)

  val texTarget = new ResourceLocation(SkillsVanilla.MODID, "textures/overlay/indicator.png")
}

class ForgeClientEventHandler {
  @SubscribeEvent
  def onModelRegistry(event: ModelRegistryEvent): Unit = {
    ModelLoader.setCustomModelResourceLocation(ItemInit.trainingSword, 0, new ModelResourceLocation(ItemInit.trainingSword.getRegistryName, "inventory"))
    ModelLoader.setCustomModelResourceLocation(ItemInit.rstd, 0, new ModelResourceLocation(ItemInit.rstd.getRegistryName, "inventory"))
  }

  @SubscribeEvent
  def onRenderCrosshairs(event: RenderGameOverlayEvent.Post): Unit = {
    if (event.getType != ElementType.ALL) return
    val mc = Minecraft.getMinecraft
    val player = mc.player
    val targeting = player.getEntityData.getCompoundTag(SkillsVanilla.MODID).getInteger("targeting")
    if (targeting == 0) return
    val scaled = new ScaledResolution(mc)
    val baseW = scaled.getScaledWidth_double / 2.0 + 75.0
    val baseH = scaled.getScaledHeight_double / 2.0 - 65.0
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F)
    mc.fontRenderer.drawString("!", baseW.toFloat - 12.0F, baseH.toFloat + 18.0F, -39424, false)
    mc.fontRenderer.drawString(new TextComponentTranslation("overlay.skillsvanilla.perception.detected").getFormattedText,
      baseW.toFloat - 30.0F, baseH.toFloat + 28.0F, -39424, false)
    if (targeting > 0) {
      mc.getTextureManager.bindTexture(texTarget)
      Gui.drawModalRectWithCustomSizedTexture((baseW - 10).toInt, (baseH + 5).toInt, 0F, 0F, 16, 16, 16F, 16F)
      mc.fontRenderer.drawString(String.valueOf(targeting), baseW.toFloat, baseH.toFloat, -39424, false)
    }
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F)
  }
}
