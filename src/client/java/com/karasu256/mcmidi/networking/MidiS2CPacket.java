package com.karasu256.mcmidi.networking;

import com.karasu256.mcmidi.Constants;
import com.karasu256.mcmidi.api.midi.ExtendedMidi;
import com.karasu256.mcmidi.client.MidiPlayerState;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class MidiS2CPacket {
    private static final ExecutorService MIDI_PLAYER_POOL;

    static {
        final ThreadFactory threadFactory = r -> {
            Thread thread = new Thread(r);
            thread.setName("MidiPlayerThread");
            return thread;
        };
        MIDI_PLAYER_POOL = Executors.newSingleThreadExecutor(threadFactory);
    }

    public static void receive(SequencePayload payload, ClientPlayNetworking.Context context) {
        MIDI_PLAYER_POOL.submit(() -> receiveAsync(payload, context));
    }

    private static void receiveAsync(SequencePayload payload, ClientPlayNetworking.Context context) {
        NbtCompound nbt = payload.nbt();
        String path = nbt.getString("path").orElse("");
        int loopCount = nbt.getInt("loopCount").orElse(0);
        int startTick = nbt.getInt("startTick").orElse(0);

        SequencePayload.MidiPlayerState state = Arrays.stream(SequencePayload.MidiPlayerState.values())
                .filter(s -> s.getName().equals(nbt.getString("state").orElse("")))
                .findFirst()
                .orElse(SequencePayload.MidiPlayerState.STOPPING);

        MidiPlayerState playerState = MidiPlayerState.getInstance();

        if (state.equals(SequencePayload.MidiPlayerState.STOPPING)) {
            playerState.stopCurrent();
            return;
        }

        if (path == null || path.isEmpty()) {
            Constants.LOGGER.info("No path provided in MIDI packet");
            return;
        }

        byte[] bytes = payload.bytes();

        playerState.stopCurrent();
        try {
            ExtendedMidi midi = new ExtendedMidi(bytes);
            midi.setDisplayName(path);
            playerState.setCurrentPlayer(midi);

            if (loopCount > 0) {
                midi.setLoopCount(loopCount);
            }
            if (startTick > 0) {
                midi.setStartTick(startTick);
            }

            midi.play();
            MinecraftClient.getInstance().inGameHud.setRecordPlayingOverlay(Text.literal(path));
        } catch (Exception e) {
            Constants.LOGGER.error("Failed to load MIDI file: {}", path);
            Constants.LOGGER.error(e.getMessage());
            playerState.setCurrentPlayer(null);
        }
    }
}
