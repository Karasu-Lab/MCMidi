package com.karasu256.mcmidi.config;

import com.karasu256.mcmidi.Constants;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = Constants.MOD_ID)
public class ModConfig implements ConfigData {
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public General general = new General();

    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public Sound sound = new Sound();

    public static class General {
        public String soundFontPath = "";
        public String midiDirectory = Constants.DEFAULT_MIDI_DIRECTORY;
        public String soundFontDirectory = Constants.DEFAULT_SOUNDFONT_DIRECTORY;
    }

    public static class Sound {
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int midiVolume = 100;
    }
}
