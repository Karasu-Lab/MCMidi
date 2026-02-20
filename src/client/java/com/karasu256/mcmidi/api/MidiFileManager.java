package com.karasu256.mcmidi.api;

import com.karasu256.mcmidi.config.ConfigManager;

public class MidiFileManager extends AbstractResourceManager {
    @Override
    public String getDirectory() {
        return ConfigManager.getConfig().general.midiDirectory;
    }

    @Override
    public String[] getExtensions() {
        return new String[]{".midi", ".mid"};
    }
}
