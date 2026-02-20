package com.karasu256.mcmidi.api;

import com.karasu256.mcmidi.config.ConfigManager;
import com.karasu256.mcmidi.impl.IFileType;

public class SoundFontFileType implements IFileType {
    private final IConfigManager configManager;

    public SoundFontFileType(IConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public String getDirectory() {
        return configManager.getConfig().general.soundFontDirectory;
    }

    @Override
    public String[] getExtensions() {
        return new String[]{".sf2"};
    }
}
