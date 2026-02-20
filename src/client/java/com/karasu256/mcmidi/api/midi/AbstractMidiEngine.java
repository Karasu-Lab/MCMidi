package com.karasu256.mcmidi.api.midi;

import org.jetbrains.annotations.Nullable;

import javax.sound.midi.MidiMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public abstract class AbstractMidiEngine implements IMidiEngine {
    protected final List<BiConsumer<MidiMessage, Long>> listeners = new ArrayList<>();
    protected @Nullable String displayName;

    @Override
    @Nullable
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public void setDisplayName(@Nullable String name) {
        this.displayName = name;
    }

    @Override
    public void registerListener(BiConsumer<MidiMessage, Long> listener) {
        this.listeners.add(listener);
    }

    @Override
    public void unregisterListener(BiConsumer<MidiMessage, Long> listener) {
        this.listeners.remove(listener);
    }

    protected void notifyListeners(MidiMessage message, long timeStamp) {
        for (BiConsumer<MidiMessage, Long> listener : listeners) {
            listener.accept(message, timeStamp);
        }
    }
}
