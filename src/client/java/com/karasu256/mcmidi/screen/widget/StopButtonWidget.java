package com.karasu256.mcmidi.screen.widget;

import com.karasu256.karasunikilib.screen.widget.AbstractCustomWidget;
import com.karasu256.mcmidi.api.midi.IMidiEngine;
import com.karasu256.mcmidi.client.MidiPlayerState;
import net.minecraft.text.Text;

public class StopButtonWidget extends AbstractCustomWidget {
    private static final Text STOP_TEXT = Text.literal("⏹");

    public StopButtonWidget() {
        super(0, 0, 20, 20, STOP_TEXT, button -> {
            MidiPlayerState.getInstance().stopCurrent();
            ((StopButtonWidget) button).refresh();
        });
    }

    @Override
    public void refresh() {
        IMidiEngine engine = MidiPlayerState.getInstance().getCurrentEngine();
        this.active = engine != null;
    }
}
