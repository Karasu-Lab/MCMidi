package com.karasu256.mcmidi.screen;

import com.karasu256.mcmidi.Constants;
import com.karasu256.mcmidi.api.midi.IMidiEngine;
import com.karasu256.mcmidi.client.MidiPlayerState;
import com.karasu256.mcmidi.impl.IMidiScreen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class SoundControllerScreen extends Screen implements IMidiScreen {
    private final Screen parent;
    private ButtonWidget playButton;
    private ButtonWidget pauseButton;
    private ButtonWidget stopButton;

    public SoundControllerScreen(Screen parent) {
        this(Text.translatable("mcmidi.midi_sound_controller"), parent);
    }

    protected SoundControllerScreen(Text title, Screen parent) {
        super(title);
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();

        this.playButton = this.addDrawableChild(ButtonWidget.builder(
                Text.translatable("mcmidi.sound_controller.play"),
                button -> {
                    IMidiEngine current = MidiPlayerState.getInstance().getCurrentEngine();
                    if (current != null) {
                        current.play();
                    }
                }).dimensions((this.width / 2) - 110, this.height - 30, 70, Constants.BUTTON_HEIGHT).build());

        this.pauseButton = this.addDrawableChild(ButtonWidget.builder(
                Text.translatable("mcmidi.sound_controller.pause"),
                button -> {
                    MidiPlayerState.getInstance().pauseCurrent();
                }).dimensions((this.width / 2) - 35, this.height - 30, 70, Constants.BUTTON_HEIGHT).build());

        this.stopButton = this.addDrawableChild(ButtonWidget.builder(
                Text.translatable("mcmidi.sound_controller.stop"),
                button -> {
                    MidiPlayerState.getInstance().stopCurrent();
                }).dimensions((this.width / 2) + 40, this.height - 30, 70, Constants.BUTTON_HEIGHT).build());

        this.addDrawableChild(ButtonWidget.builder(
                Text.translatable("gui.back"),
                button -> this.close()).dimensions((this.width / 2) - 100, this.height - 60, 200, Constants.BUTTON_HEIGHT).build());

        this.updateButtonStates();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 6, Constants.TITLE_COLOR);

        IMidiEngine current = MidiPlayerState.getInstance().getCurrentEngine();
        if (current != null) {
            Text infoText = Text.translatable("mcmidi.sound_controller.now_playing");
            context.drawText(this.textRenderer, infoText, (this.width / 2) - this.textRenderer.getWidth(infoText) / 2,
                    30, Constants.TITLE_COLOR, true);

            String name = current.getDisplayName();
            Text midiName = name != null ? Text.literal(name) : Text.translatable("mcmidi.text.no_midi");
            context.drawText(this.textRenderer, midiName, (this.width / 2) - this.textRenderer.getWidth(midiName) / 2,
                    45, Constants.TITLE_COLOR, true);

            String stateText = current.isPlaying() ? "mcmidi.sound_controller.state.playing"
                    : "mcmidi.sound_controller.state.paused";
            Text state = Text.translatable(stateText);
            context.drawText(this.textRenderer, state, (this.width / 2) - this.textRenderer.getWidth(state) / 2, 60,
                    Constants.TITLE_COLOR, true);

            Text bpmText = Text.literal("BPM: " + current.getBPM());
            context.drawText(this.textRenderer, bpmText, (this.width / 2) - this.textRenderer.getWidth(bpmText) / 2, 75,
                    Constants.TITLE_COLOR, true);
        } else {
            Text noMidiText = Text.translatable("mcmidi.sound_controller.no_midi");
            context.drawText(this.textRenderer, noMidiText,
                    (this.width / 2) - this.textRenderer.getWidth(noMidiText) / 2, 50, Constants.TITLE_COLOR, true);
        }

        this.updateButtonStates();
    }

    private void updateButtonStates() {
        IMidiEngine current = MidiPlayerState.getInstance().getCurrentEngine();
        boolean hasMidi = current != null;
        boolean isPlaying = hasMidi && current.isPlaying();

        this.playButton.active = hasMidi && !isPlaying;
        this.pauseButton.active = hasMidi && isPlaying;
        this.stopButton.active = hasMidi;
    }

    @Override
    public void playCurrent() {
        IMidiEngine current = MidiPlayerState.getInstance().getCurrentEngine();
        if (current != null && !current.isPlaying()) {
            current.play();
        }
    }

    @Override
    public void close() {
        this.playCurrent();
        if (parent != null) {
            client.setScreen(parent);
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
