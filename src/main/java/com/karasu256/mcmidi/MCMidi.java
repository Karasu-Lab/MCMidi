package com.karasu256.mcmidi;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class MCMidi implements ModInitializer {

    public static Identifier id(String path) {
        return Identifier.of(Constants.MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        // Client-only mod: all logic is in MCMidiClient
    }
}
