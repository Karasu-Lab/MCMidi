package com.karasu256.mcmidi.mixin.client;

import com.karasu256.mcmidi.api.midi.IMidiEngine;
import com.karasu256.mcmidi.client.MidiPlayerState;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class ScreenMixin {
    @Unique
    private IMidiEngine mcmidi$current;
    @Unique
    private long mcmidi$position = 0;

    @Shadow
    public abstract boolean shouldPause();

    @Inject(method = "init(Lnet/minecraft/client/MinecraftClient;II)V", at = @At("RETURN"))
    public void init(CallbackInfo ci) {
        if (shouldPause()) {
            this.mcmidi$current = MidiPlayerState.getInstance().getCurrentEngine();

            if (this.mcmidi$current != null) {
                this.mcmidi$position = this.mcmidi$current.getPosition();
                this.mcmidi$current.pause();
                this.mcmidi$current.setPosition(this.mcmidi$position);
            }
        }
    }

    @Inject(method = "close", at = @At("RETURN"))
    public void close(CallbackInfo ci) {
        if (shouldPause()) {
            if (this.mcmidi$current != null) {
                this.mcmidi$current.setPosition(this.mcmidi$position);
                this.mcmidi$current.play();
            }
        }
    }
}
