package com.karasu256.mcmidi.client;

import com.karasu256.mcmidi.Constants;
import com.karasu256.mcmidi.api.MidiFileManager;
import com.karasu256.mcmidi.api.SoundFontFileManager;
import com.karasu256.mcmidi.api.midi.ExtendedMidi;
import com.karasu256.mcmidi.impl.IMidiPlayer;
import com.karasu256.mcmidi.impl.IResourceManager;
import org.jetbrains.annotations.Nullable;

public class MidiPlayerState {
    private static final MidiPlayerState INSTANCE = new MidiPlayerState();

    private final IResourceManager midiManager = new MidiFileManager();
    private final IResourceManager soundFontManager = new SoundFontFileManager();
    private @Nullable IMidiPlayer currentPlayer;

    private MidiPlayerState() {}

    public static MidiPlayerState getInstance() {
        return INSTANCE;
    }

    public IResourceManager getMidiManager() {
        return midiManager;
    }

    public IResourceManager getSoundFontManager() {
        return soundFontManager;
    }

    @Nullable
    public IMidiPlayer getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(@Nullable IMidiPlayer player) {
        this.currentPlayer = player;
    }

    public void playMidi(byte[] midiData) {
        stopCurrent();
        try {
            ExtendedMidi midi = new ExtendedMidi(midiData);
            this.currentPlayer = midi;
            midi.play();
        } catch (Exception e) {
            Constants.LOGGER.error("Failed to play MIDI", e);
            this.currentPlayer = null;
        }
    }

    public void stopCurrent() {
        if (currentPlayer != null) {
            currentPlayer.stop();
        }
    }

    public void pauseCurrent() {
        if (currentPlayer != null) {
            currentPlayer.pause();
        }
    }

    public void clearAll() {
        if (currentPlayer != null) {
            currentPlayer.stop();
            currentPlayer.clear();
        }
        currentPlayer = null;
    }
}
