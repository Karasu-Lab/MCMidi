package com.karasu256.mcmidi.api;

import com.karasu256.mcmidi.Constants;

public class SoundFontFileManager extends AbstractResourceManager {
    @Override
    public String getDirectory() {
        return Constants.SOUNDFONT_DIRECTORY;
    }

    @Override
    public String[] getExtensions() {
        return new String[]{Constants.SOUNDFONT_EXTENSION};
    }
}
