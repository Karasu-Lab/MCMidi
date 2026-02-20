package com.karasu256.mcmidi.api.midi;

import com.karasu256.mcmidi.Constants;
import com.karasu256.mcmidi.config.ConfigManager;
import com.karasu256.mcmidi.config.ModConfig;
import com.karasu256.mcmidi.impl.IMidiPlayer;
import com.karasu256.mcmidi.screen.MidiControlCenter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import javax.sound.midi.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class ExtendedMidi implements IMidiPlayer {
    private final byte[] bytes;
    private final Sequencer sequencer;
    private final Sequence sequence;
    private final Synthesizer synthesizer;
    private @Nullable String displayName;
    private long position;

    public ExtendedMidi(byte[] bytes) throws Exception {
        this.bytes = bytes;

        this.sequencer = MidiSystem.getSequencer();
        this.sequence = MidiSystem.getSequence(new ByteArrayInputStream(this.bytes));
        this.synthesizer = MidiSystem.getSynthesizer();
    }

    @Override
    public void load(byte[] midiData) {
        // Data is loaded in constructor for javax.sound.midi compatibility
    }

    @Override
    public void play() {
        try {
            if (!this.sequencer.isOpen()) {
                this.sequencer.setSequence(this.sequence);
                this.synthesizer.open();
                this.sequencer.open();

                ModConfig config = ConfigManager.getConfig();
                loadSoundFontFromPath(config.general.soundFontPath);

                MyReceiver myReceiver = new MyReceiver(this.synthesizer.getReceiver());
                myReceiver.setListener(new Receiver() {
                    @Override
                    public void send(MidiMessage message, long timeStamp) {
                        if (MinecraftClient.getInstance().currentScreen instanceof MidiControlCenter controlCenter) {
                            controlCenter.onReceive(message);
                        }
                    }

                    @Override
                    public void close() {}
                });
                this.sequencer.getTransmitter().setReceiver(myReceiver);
            }

            if (!this.sequencer.isRunning()) {
                this.sequencer.start();
                Constants.LOGGER.info("Playing MIDI: {}", this.displayName);
            }
        } catch (Exception e) {
            Constants.LOGGER.error("Failed to play MIDI: {}", this.displayName);
            Constants.LOGGER.error(e.getMessage());
        }
    }

    @Override
    public void stop() {
        if (this.sequencer.isOpen()) {
            this.sequencer.stop();
            this.sequencer.close();
            this.synthesizer.close();
        }
    }

    @Override
    public void pause() {
        if (this.sequencer.isOpen() && this.sequencer.isRunning()) {
            this.sequencer.stop();
            Constants.LOGGER.info("Pausing MIDI: {}", this.displayName);
        }
    }

    @Override
    public void setPosition(long position) {
        this.position = position;
        this.sequencer.setTickPosition(this.position);
    }

    @Override
    public long getPosition() {
        if (this.sequencer.isOpen()) {
            return this.sequencer.getTickPosition();
        }
        return 0;
    }

    @Override
    public boolean isPlaying() {
        return this.sequencer.isOpen() && this.sequencer.isRunning();
    }

    @Override
    public void setSoundFont(byte[] soundFontData) {
        if (soundFontData == null || soundFontData.length == 0) {
            useDefaultSoundFont();
            return;
        }

        try {
            Soundbank soundbank = MidiSystem.getSoundbank(new ByteArrayInputStream(soundFontData));
            for (Transmitter tm : sequencer.getTransmitters()) {
                tm.close();
            }
            synthesizer.unloadAllInstruments(synthesizer.getDefaultSoundbank());
            synthesizer.loadAllInstruments(soundbank);
            sequencer.getTransmitter().setReceiver(synthesizer.getReceiver());
        } catch (InvalidMidiDataException | IOException | MidiUnavailableException e) {
            Constants.LOGGER.error("Failed to load soundfont from data", e);
            useDefaultSoundFont();
        }
    }

    @Override
    public void setLoopCount(int count) {
        this.sequencer.setLoopCount(count);
    }

    @Override
    public void clear() {
        stop();
        if (this.sequencer.isOpen()) {
            this.sequencer.close();
        }
        if (this.synthesizer.isOpen()) {
            this.synthesizer.close();
        }
    }

    public void setDisplayName(@Nullable String name) {
        this.displayName = name;
    }

    @Nullable
    public String getDisplayName() {
        return displayName;
    }

    public Text getPlayingPath() {
        if (this.displayName == null) {
            return Text.translatable("mcmidi.text.no_midi");
        }
        return Text.literal(this.displayName);
    }

    public void setStartTick(long tick) {
        this.sequencer.setLoopStartPoint(tick);
    }

    public byte[] getBytes() {
        return this.bytes;
    }

    public float getBPM() {
        return this.sequencer.getTempoInBPM();
    }

    public Synthesizer getSynthesizer() {
        return this.synthesizer;
    }

    public void updatePosition() {
        if (this.sequencer.isOpen()) {
            this.position = this.sequencer.getTickPosition();
        }
    }

    private void loadSoundFontFromPath(String path) {
        if (path == null || path.isEmpty()) {
            useDefaultSoundFont();
            return;
        }

        File file = new File(path);
        if (!file.exists() || file.isDirectory()) {
            Constants.LOGGER.error("Soundfont file does not exist, using default");
            useDefaultSoundFont();
            return;
        }

        try {
            for (Transmitter tm : sequencer.getTransmitters()) {
                tm.close();
            }
            synthesizer.unloadAllInstruments(synthesizer.getDefaultSoundbank());
            synthesizer.loadAllInstruments(MidiSystem.getSoundbank(file));
            sequencer.getTransmitter().setReceiver(synthesizer.getReceiver());
        } catch (InvalidMidiDataException | IOException | MidiUnavailableException e) {
            Constants.LOGGER.error("Failed to load soundfont: {}", path);
            useDefaultSoundFont();
        }
    }

    private void useDefaultSoundFont() {
        try {
            for (Transmitter tm : sequencer.getTransmitters()) {
                tm.close();
            }
            sequencer.getTransmitter().setReceiver(synthesizer.getReceiver());
        } catch (MidiUnavailableException e) {
            Constants.LOGGER.error("Failed to load default soundfont", e);
        }
    }
}
