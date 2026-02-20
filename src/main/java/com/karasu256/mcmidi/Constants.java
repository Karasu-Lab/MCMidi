package com.karasu256.mcmidi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

public final class Constants {
    public static final String MOD_ID = "mcmidi";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final String DEFAULT_MIDI_DIRECTORY = "midi/musics";
    public static final String DEFAULT_SOUNDFONT_DIRECTORY = "midi/soundfonts";

    public static final String[] MIDI_EXTENSIONS = {".midi", ".mid"};
    public static final String SOUNDFONT_EXTENSION = ".sf2";

    public static final int BUTTON_WIDTH = 90;
    public static final int BUTTON_HEIGHT = 20;
    public static final int FOOTER_HEIGHT = 53;
    public static final int HEADER_HEIGHT = 33;
    public static final int LIST_ITEM_HEIGHT = 18;
    public static final int TITLE_COLOR = 0xFFFFFF;
    public static final int DOUBLE_CLICK_THRESHOLD_MS = 250;

    public static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};

    public static final Color COLOR_C = new Color(255, 0, 0);
    public static final Color COLOR_C_SHARP = new Color(255, 165, 0);
    public static final Color COLOR_D = new Color(255, 255, 0);
    public static final Color COLOR_D_SHARP = new Color(0, 255, 0);
    public static final Color COLOR_E = new Color(0, 0, 255);
    public static final Color COLOR_F = new Color(75, 0, 130);
    public static final Color COLOR_F_SHARP = new Color(238, 130, 238);
    public static final Color COLOR_G = new Color(255, 192, 203);
    public static final Color COLOR_G_SHARP = new Color(255, 255, 255);
    public static final Color COLOR_A = new Color(211, 211, 211);
    public static final Color COLOR_A_SHARP = new Color(128, 128, 128);
    public static final Color COLOR_B = new Color(0, 0, 0);

    public static Color getNoteColor(String noteName) {
        return switch (noteName) {
            case "C" -> COLOR_C;
            case "C#" -> COLOR_C_SHARP;
            case "D" -> COLOR_D;
            case "D#" -> COLOR_D_SHARP;
            case "E" -> COLOR_E;
            case "F" -> COLOR_F;
            case "F#" -> COLOR_F_SHARP;
            case "G" -> COLOR_G;
            case "G#" -> COLOR_G_SHARP;
            case "A" -> COLOR_A;
            case "A#" -> COLOR_A_SHARP;
            case "B" -> COLOR_B;
            default -> Color.WHITE;
        };
    }

    private Constants() {}
}
