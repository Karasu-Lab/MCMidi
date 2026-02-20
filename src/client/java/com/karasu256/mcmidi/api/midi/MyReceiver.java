package com.karasu256.mcmidi.api.midi;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;

public class MyReceiver implements Receiver {
    private final Receiver original;
    private Receiver listener;

    public MyReceiver(Receiver original) {
        this.original = original;
    }

    public void setListener(Receiver listener) {
        this.listener = listener;
    }

    @Override
    public void send(MidiMessage message, long timeStamp) {
        original.send(message, timeStamp);
        if (listener != null) {
            listener.send(message, timeStamp);
        }
    }

    @Override
    public void close() {
        original.close();
    }
}
