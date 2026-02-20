package com.karasu256.mcmidi.api;

import com.karasu256.mcmidi.config.ConfigManager;
import com.karasu256.mcmidi.impl.IFileType;

public class MidiFileType implements IFileType {
    private final IConfigManager configManager;

    public MidiFileType(IConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public String getDirectory() {
        return configManager.getConfig().general.midiDirectory;
    }

    @Override
    public String[] getExtensions() {
        return new String[]{".midi", ".mid"};
    }
}
