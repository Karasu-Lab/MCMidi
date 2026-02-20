package com.karasu256.mcmidi.screen.widget;

import com.karasu256.mcmidi.api.midi.IMidiEngine;
import com.karasu256.mcmidi.client.MidiPlayerState;
import net.minecraft.text.Text;

public class PlayPauseButtonWidget extends AbstractCustomWidget {
    private static final Text PLAY_TEXT = Text.literal("▶");
    private static final Text PAUSE_TEXT = Text.literal("⏸");

    public PlayPauseButtonWidget() {
        super(0, 0, 20, 20, PLAY_TEXT, button -> {
            IMidiEngine engine = MidiPlayerState.getInstance().getCurrentEngine();
            if (engine == null) return;
            if (engine.isPlaying()) {
                engine.pause();
            } else {
                engine.play();
            }
            ((PlayPauseButtonWidget) button).refresh();
        });
    }

    @Override
    public void refresh() {
        IMidiEngine engine = MidiPlayerState.getInstance().getCurrentEngine();
        boolean hasMidi = engine != null;
        boolean isPlaying = hasMidi && engine.isPlaying();

        this.active = hasMidi;
        this.setMessage(isPlaying ? PAUSE_TEXT : PLAY_TEXT);
    }
}
