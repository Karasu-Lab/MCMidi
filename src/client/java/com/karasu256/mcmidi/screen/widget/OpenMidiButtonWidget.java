package com.karasu256.mcmidi.screen.widget;

import com.karasu256.mcmidi.screen.MidiChooseScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class OpenMidiButtonWidget extends ButtonWidget {
    private static final Text LABEL = Text.translatable("mcmidi.midi_control_center.open_midi_files");

    public OpenMidiButtonWidget(Screen parentScreen) {
        super(0, 0, 70, 20, LABEL, button -> {
            MinecraftClient.getInstance().setScreen(new MidiChooseScreen(parentScreen));
        }, DEFAULT_NARRATION_SUPPLIER);
    }
}
