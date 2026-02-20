package com.karasu256.mcmidi.screen.widget;

import com.karasu256.mcmidi.api.midi.ExtendedMidi;
import com.karasu256.mcmidi.client.MidiPlayerState;
import com.karasu256.mcmidi.impl.IMidiPlayer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;

public class PlaybackControlWidget {
    private final SeekBarWidget seekBar;
    private final PlayPauseButtonWidget playPauseButton;
    private final StopButtonWidget stopButton;
    private final OpenSoundFontButtonWidget openSoundFontButton;
    private final OpenMidiButtonWidget openMidiButton;
    private final DirectionalLayoutWidget layout;

    public PlaybackControlWidget(Screen parentScreen) {
        this.layout = DirectionalLayoutWidget.horizontal().spacing(4);

        this.seekBar = new SeekBarWidget(0, 0, 200, 20);
        this.playPauseButton = new PlayPauseButtonWidget();
        this.stopButton = new StopButtonWidget();
        this.openSoundFontButton = new OpenSoundFontButtonWidget(parentScreen);
        this.openMidiButton = new OpenMidiButtonWidget(parentScreen);

        this.layout.add(this.seekBar);
        this.layout.add(this.playPauseButton);
        this.layout.add(this.stopButton);
        this.layout.add(this.openSoundFontButton);
        this.layout.add(this.openMidiButton);

        refreshState();
    }

    public DirectionalLayoutWidget getLayout() {
        return this.layout;
    }

    public void tick() {
        IMidiPlayer player = MidiPlayerState.getInstance().getCurrentPlayer();
        if (player instanceof ExtendedMidi midi) {
            long length = midi.getLength();
            if (length > 0 && !seekBar.isDragging()) {
                double progress = (double) midi.getPosition() / length;
                seekBar.setProgress(progress);
            }
        }
        refreshState();
    }

    public void refreshState() {
        this.playPauseButton.refreshState();
        this.stopButton.refreshState();
    }

}
