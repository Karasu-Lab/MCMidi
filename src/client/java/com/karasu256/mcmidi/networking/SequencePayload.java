package com.karasu256.mcmidi.networking;

import com.karasu256.mcmidi.Constants;
import com.karasu256.mcmidi.MCMidi;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record SequencePayload(NbtCompound nbt, byte[] bytes) implements CustomPayload {
    public static final Identifier IDENTIFIER = MCMidi.id("midi_packet");
    public static final CustomPayload.Id<SequencePayload> ID = new CustomPayload.Id<>(IDENTIFIER);

    public static final PacketCodec<PacketByteBuf, SequencePayload> CODEC = PacketCodec.of(
            (value, buf) -> {
                buf.writeNbt(value.nbt);
                buf.writeByteArray(value.bytes);
            },
            buf -> new SequencePayload(buf.readNbt(), buf.readByteArray())
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public enum MidiPlayerState {
        LOADING("loading"),
        PLAYING("playing"),
        PAUSING("pausing"),
        CHANGE_SOUNDFONT("change_soundfont"),
        STOPPING("stopping");

        private final String name;

        MidiPlayerState(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
