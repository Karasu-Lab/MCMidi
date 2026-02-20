package com.karasu256.mcmidi.mixin.client;

import com.karasu256.mcmidi.api.midi.IMidiEngine;
import com.karasu256.mcmidi.client.MidiPlayerState;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Inject(method = "render", at = @At("RETURN"))
    private void render(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        IMidiEngine engine = MidiPlayerState.getInstance().getCurrentEngine();
        if (engine != null) {
            engine.getPosition();
        }
    }
}
