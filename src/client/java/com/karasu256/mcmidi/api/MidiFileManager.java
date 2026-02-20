package com.karasu256.mcmidi.api;

import com.karasu256.mcmidi.Constants;

public class MidiFileManager extends AbstractResourceManager {
    @Override
    public String getDirectory() {
        return Constants.MIDI_DIRECTORY;
    }

    @Override
    public String[] getExtensions() {
        return Constants.MIDI_EXTENSIONS;
    }
}
