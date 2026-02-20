package com.karasu256.mcmidi.screen.widget;

import com.karasu256.mcmidi.api.midi.IMidiEngine;
import com.karasu256.mcmidi.client.MidiPlayerState;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

public class SeekBarWidget extends SliderWidget implements IControlWidget {
    private boolean userDragging;

    public SeekBarWidget(int x, int y, int width, int height) {
        super(x, y, width, height, Text.empty(), 0.0);
    }

    @Override
    protected void updateMessage() {
        IMidiEngine engine = MidiPlayerState.getInstance().getCurrentEngine();
        if (engine != null) {
            long posSec = engine.getPosition() / 1_000_000;
            long lenSec = engine.getLength() / 1_000_000;
            this.setMessage(Text.literal(String.format("%d:%02d / %d:%02d",
                    posSec / 60, posSec % 60, lenSec / 60, lenSec % 60)));
        } else {
            this.setMessage(Text.literal("0:00 / 0:00"));
        }
    }

    @Override
    protected void applyValue() {
        IMidiEngine engine = MidiPlayerState.getInstance().getCurrentEngine();
        if (engine != null) {
            long length = engine.getLength();
            long newPos = (long) (this.value * length);
            engine.setPosition(newPos);
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

    @Override
    public void tick() {
        IMidiEngine engine = MidiPlayerState.getInstance().getCurrentEngine();
        if (engine != null) {
            long length = engine.getLength();
            if (length > 0 && !userDragging) {
                setProgress((double) engine.getPosition() / length);
            }
        }
    }

    @Override
    public void refresh() {
    }
}