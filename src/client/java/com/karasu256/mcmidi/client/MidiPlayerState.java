package com.karasu256.mcmidi.client;

import com.karasu256.mcmidi.Constants;
import com.karasu256.mcmidi.api.FileManager;
import com.karasu256.mcmidi.api.MidiFileType;
import com.karasu256.mcmidi.api.SoundFontFileType;
import com.karasu256.mcmidi.api.midi.IMidiEngine;
import com.karasu256.mcmidi.api.midi.JavaMidiEngine;
import org.jetbrains.annotations.Nullable;

public class MidiPlayerState {
    private static final MidiPlayerState INSTANCE = new MidiPlayerState();

    private final FileManager<MidiFileType> midiManager = new FileManager<>(new MidiFileType());
    private final FileManager<SoundFontFileType> soundFontManager = new FileManager<>(new SoundFontFileType());
    private @Nullable IMidiEngine currentEngine;

    private MidiPlayerState() {
    }

    public static MidiPlayerState getInstance() {
        return INSTANCE;
    }

    public FileManager<MidiFileType> getMidiManager() {
        return midiManager;
    }

    public FileManager<SoundFontFileType> getSoundFontManager() {
        return soundFontManager;
    }

    @Nullable
    public IMidiEngine getCurrentEngine() {
        return currentEngine;
    }

    public void setCurrentEngine(@Nullable IMidiEngine engine) {
        this.currentEngine = engine;
    }

    public void playMidi(byte[] midiData) {
        stopCurrent();
        try {
            JavaMidiEngine engine = new JavaMidiEngine(midiData);
            this.currentEngine = engine;
            engine.play();
        } catch (Exception e) {
            Constants.LOGGER.error("Failed to play MIDI", e);
            this.currentEngine = null;
        }
    }

    public void stopCurrent() {
        if (currentEngine != null) {
            currentEngine.stop();
        }
    }

    public void pauseCurrent() {
        if (currentEngine != null) {
            currentEngine.pause();
        }
    }

    public void clearAll() {
        if (currentEngine != null) {
            currentEngine.stop();
            currentEngine.clear();
        }
        currentEngine = null;
    }
}
