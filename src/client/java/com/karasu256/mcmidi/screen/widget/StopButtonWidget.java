package com.karasu256.mcmidi.screen.widget;

import com.karasu256.mcmidi.client.MidiPlayerState;
import com.karasu256.mcmidi.impl.IMidiPlayer;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class StopButtonWidget extends ButtonWidget {
    private static final Text STOP_TEXT = Text.literal("⏹");

    public StopButtonWidget() {
        super(0, 0, 20, 20, STOP_TEXT, button -> {
            MidiPlayerState.getInstance().stopCurrent();
            ((StopButtonWidget) button).refreshState();
        }, DEFAULT_NARRATION_SUPPLIER);
    }

    public void refreshState() {
        IMidiPlayer player = MidiPlayerState.getInstance().getCurrentPlayer();
        this.active = player != null;
    }
}
