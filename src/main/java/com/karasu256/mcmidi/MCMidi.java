package com.karasu256.mcmidi;

import com.karasu256.mcmidi.networking.SequencePayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.util.Identifier;

public class MCMidi implements ModInitializer {

    public static Identifier id(String path) {
        return Identifier.of(Constants.MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playS2C().register(SequencePayload.ID, SequencePayload.CODEC);
    }
}
