package com.karasu256.mcmidi.api;

import com.karasu256.mcmidi.config.ConfigManager;
import com.karasu256.mcmidi.impl.IFileType;

public class MidiFileType implements IFileType {
    @Override
    public String getDirectory() {
        return ConfigManager.getConfig().general.midiDirectory;
    }

    @Override
    public String[] getExtensions() {
        return new String[]{".midi", ".mid"};
    }
}
