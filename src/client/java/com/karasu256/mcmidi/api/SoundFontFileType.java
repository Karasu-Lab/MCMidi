package com.karasu256.mcmidi.api;

import com.karasu256.mcmidi.config.ConfigManager;
import com.karasu256.mcmidi.impl.IFileType;

public class SoundFontFileType implements IFileType {
    @Override
    public String getDirectory() {
        return ConfigManager.getConfig().general.soundFontDirectory;
    }

    @Override
    public String[] getExtensions() {
        return new String[]{".sf2"};
    }
}
