package com.karasu256.mcmidi.screen.widget;

import com.karasu256.mcmidi.client.MidiPlayerState;
import com.karasu256.mcmidi.impl.IMidiPlayer;
import net.minecraft.text.Text;

public class PlayPauseButtonWidget extends AbstractCustomWidget {
    private static final Text PLAY_TEXT = Text.literal("▶");
    private static final Text PAUSE_TEXT = Text.literal("⏸");

    public PlayPauseButtonWidget() {
        super(0, 0, 20, 20, PLAY_TEXT, button -> {
            IMidiPlayer player = MidiPlayerState.getInstance().getCurrentPlayer();
            if (player == null) return;
            if (player.isPlaying()) {
                player.pause();
            } else {
                player.play();
            }
            ((PlayPauseButtonWidget) button).refresh();
        });
    }

    @Override
    public void refresh() {
        IMidiPlayer player = MidiPlayerState.getInstance().getCurrentPlayer();
        boolean hasMidi = player != null;
        boolean isPlaying = hasMidi && player.isPlaying();

        this.active = hasMidi;
        this.setMessage(isPlaying ? PAUSE_TEXT : PLAY_TEXT);
    }
}
