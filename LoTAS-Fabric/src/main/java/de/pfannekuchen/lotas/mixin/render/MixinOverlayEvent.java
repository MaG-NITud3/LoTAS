package de.pfannekuchen.lotas.mixin.render;

import java.awt.Color;
import java.time.Duration;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import de.pfannekuchen.lotas.core.utils.EventUtils.Timer;
import de.pfannekuchen.lotas.gui.HudSettings;
import de.pfannekuchen.lotas.mods.TickrateChangerMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

@Mixin(Gui.class)
public class MixinOverlayEvent {

	
	@Inject(at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/client/gui/Gui;renderEffects()V"), method = "render")
	public void injectrender(float tickDelta, CallbackInfo ci) {
		HudSettings.drawOverlay();
		if (Timer.ticks != -1) {
			GuiComponent.fill(0, 0, 75, ConfigUtils.getBoolean("ui", "hideRTATimer") ? 13 : 24, new Color(0, 0, 0, 175).getRGB());
			Duration dur = Duration.ofMillis(Timer.ticks * 50);
			if (Timer.running)
				TickrateChangerMod.rta = Duration.ofMillis(System.currentTimeMillis() - Timer.startTime.toMillis());
			Minecraft.getInstance().font.drawShadow(Timer.getDuration(dur), 1, 3, 0xFFFFFFFF);
			if (!ConfigUtils.getBoolean("ui", "hideRTATimer")) Minecraft.getInstance().font.drawShadow("RTA: " + Timer.getDuration(TickrateChangerMod.rta), 1, 15, 0xFFFFFFFF);
		}
		if (ConfigUtils.getBoolean("tools", "showTickIndicator") && TickrateChangerMod.tickrate <= 5F && TickrateChangerMod.show) {
			Minecraft.getInstance().getTextureManager().bind(streaming);
			GuiComponent.blit(Minecraft.getInstance().window.getGuiScaledWidth() - 17, 1, 0, 0, 16, 16, 16, 64);
		}
		if (ConfigUtils.getBoolean("tools", "showSpeedometer")) {
			double distTraveledLastTickX = Minecraft.getInstance().player.x - Minecraft.getInstance().player.xo;
			double distTraveledLastTickZ = Minecraft.getInstance().player.z - Minecraft.getInstance().player.zo;
			String message = String.format("%.2f", Mth.sqrt((distTraveledLastTickX * distTraveledLastTickX + distTraveledLastTickZ * distTraveledLastTickZ)) / 0.05F) + " blocks/sec";
			int width = Minecraft.getInstance().font.width(message);
			GuiComponent.fill(4, 4, 4 + width + 2 * 2, 4 + Minecraft.getInstance().font.lineHeight + 2 + 2 - 1, 0xAA000000);
			Minecraft.getInstance().font.drawShadow(message, 6, 6, 14737632);
		}
	}

	private ResourceLocation streaming = new ResourceLocation("textures/gui/stream_indicator.png");

}
