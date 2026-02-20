package com.karasu256.mcmidi.screen.widget;

import com.karasu256.mcmidi.api.midi.ExtendedMidi;
import com.karasu256.mcmidi.client.MidiPlayerState;
import com.karasu256.mcmidi.impl.IMidiPlayer;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

public class SeekBarWidget extends SliderWidget {
    private boolean userDragging;

    public SeekBarWidget(int x, int y, int width, int height) {
        super(x, y, width, height, Text.empty(), 0.0);
    }

    @Override
    protected void updateMessage() {
        IMidiPlayer player = MidiPlayerState.getInstance().getCurrentPlayer();
        if (player instanceof ExtendedMidi midi) {
            long posSec = midi.getPosition() / 1_000_000;
            long lenSec = midi.getLength() / 1_000_000;
            this.setMessage(Text.literal(String.format("%d:%02d / %d:%02d",
                    posSec / 60, posSec % 60, lenSec / 60, lenSec % 60)));
        } else {
            this.setMessage(Text.literal("0:00 / 0:00"));
        }
    }

    @Override
    protected void applyValue() {
        IMidiPlayer player = MidiPlayerState.getInstance().getCurrentPlayer();
        if (player instanceof ExtendedMidi midi) {
            long length = midi.getLength();
            long newPos = (long) (this.value * length);
            midi.setPosition(newPos);
        }
    }

    public void setProgress(double progress) {
        this.value = Math.max(0.0, Math.min(1.0, progress));
        this.updateMessage();
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.userDragging = true;
        super.onClick(mouseX, mouseY);
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        this.userDragging = false;
        super.onRelease(mouseX, mouseY);
    }

    public boolean isDragging() {
        return this.userDragging;
    }
}