package com.karasu256.mcmidi.api.midi;

import org.jetbrains.annotations.Nullable;

import javax.sound.midi.MidiMessage;
import java.util.function.BiConsumer;

public interface IMidiEngine {
    void loadSequence(byte[] data) throws Exception;

    void play();

    void pause();

    void stop();

    long getPosition();

    void setPosition(long microseconds);

    long getLength();

    boolean isPlaying();

    void setSoundFont(byte[] soundFontData);

    void setSoundFontFromPath(String path);

    void setLoopCount(int count);

    void clear();

    @Nullable
    String getDisplayName();

    void setDisplayName(@Nullable String name);

    byte[] getSequenceBytes();

    float getBPM();

    void registerListener(BiConsumer<MidiMessage, Long> listener);

    void unregisterListener(BiConsumer<MidiMessage, Long> listener);
}
