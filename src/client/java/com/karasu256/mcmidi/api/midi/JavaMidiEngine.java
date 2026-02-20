package com.karasu256.mcmidi.api.midi;

import com.karasu256.mcmidi.Constants;
import com.karasu256.mcmidi.config.ConfigManager;
import com.karasu256.mcmidi.config.ModConfig;

import javax.sound.midi.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class JavaMidiEngine extends AbstractMidiEngine {
    private final byte[] bytes;
    private final Sequencer sequencer;
    private final Sequence sequence;
    private final Synthesizer synthesizer;
    private final IConfigManager configManager;
    private long position;

    public JavaMidiEngine(byte[] bytes, IConfigManager configManager) throws Exception {
        this.bytes = bytes;
        this.configManager = configManager;
        this.sequencer = MidiSystem.getSequencer();
        this.sequence = MidiSystem.getSequence(new ByteArrayInputStream(this.bytes));
        this.synthesizer = MidiSystem.getSynthesizer();
    }

    @Override
    public void loadSequence(byte[] data) throws Exception {
    }

    @Override
    public void play() {
        try {
            if (!this.sequencer.isOpen()) {
                this.sequencer.setSequence(this.sequence);
                this.synthesizer.open();
                this.sequencer.open();

                ModConfig config = configManager.getConfig();
                setSoundFontFromPath(config.general.soundFontPath);

                MyReceiver myReceiver = new MyReceiver(this.synthesizer.getReceiver());
                myReceiver.setListener(new Receiver() {
                    @Override
                    public void send(MidiMessage message, long timeStamp) {
                        notifyListeners(message, timeStamp);
                    }

                    @Override
                    public void close() {
                    }
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
    public long getPosition() {
        if (this.sequencer.isOpen()) {
            return this.sequencer.getMicrosecondPosition();
        }
        return 0;
    }

    @Override
    public void setPosition(long microseconds) {
        this.position = microseconds;
        this.sequencer.setMicrosecondPosition(this.position);
    }

    @Override
    public long getLength() {
        return this.sequencer.getMicrosecondLength();
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
    public void setSoundFontFromPath(String path) {
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

    @Override
    public byte[] getSequenceBytes() {
        return this.bytes;
    }

    @Override
    public float getBPM() {
        return this.sequencer.getTempoInBPM();
    }

    public Synthesizer getSynthesizer() {
        return this.synthesizer;
    }

    public void setStartTick(long tick) {
        this.sequencer.setLoopStartPoint(tick);
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
