package com.karasu256.mcmidi.impl;

public interface IMidiPlayer {
    void load(byte[] midiData);

    void play();

    void stop();

    void pause();

    void setPosition(long position);

    long getPosition();

    boolean isPlaying();

    void setSoundFont(byte[] soundFontData);

    void setLoopCount(int count);

    void clear();
}
