package com.karasu256.mcmidi.api;

import com.karasu256.mcmidi.config.ConfigManager;

public class SoundFontFileManager extends AbstractResourceManager {
    @Override
    public String getDirectory() {
        return ConfigManager.getConfig().general.soundFontDirectory;
    }

    @Override
    public String[] getExtensions() {
        return new String[]{".sf2"};
    }
}
