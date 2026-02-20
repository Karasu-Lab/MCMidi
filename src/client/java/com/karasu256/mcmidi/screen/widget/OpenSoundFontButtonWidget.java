package com.karasu256.mcmidi.screen.widget;

import com.karasu256.mcmidi.screen.SoundFontManagerScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class OpenSoundFontButtonWidget extends AbstractCustomWidget {
    private static final Text LABEL = Text.translatable("mcmidi.midi_control_center.open_soundfont_files");

    public OpenSoundFontButtonWidget(Screen parentScreen) {
        super(0, 0, 70, 20, LABEL, button -> {
            MinecraftClient.getInstance().setScreen(new SoundFontManagerScreen(parentScreen, ConfigManager.getInstance()));
        });
    }
}
