package com.karasu256.mcmidi.impl;

public interface IMidiPlayer {
    void load(byte[] midiData);

    void play();

    void stop();

    void pause();

    long getPosition();

    void setPosition(long position);

    long getLength();

    boolean isPlaying();

    void setSoundFont(byte[] soundFontData);

    void setLoopCount(int count);

    void clear();
}
