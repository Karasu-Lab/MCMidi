package com.karasu256.mcmidi.client;

import com.karasu256.mcmidi.command.MidiClientCommand;
import com.karasu256.mcmidi.config.ConfigManager;
import com.karasu256.mcmidi.networking.ModClientNetworking;
import com.karasu256.mcmidi.screen.MidiControlCenterScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class MCMidiClient implements ClientModInitializer {

    private final KeyBinding keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.mcmidi.open",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_COMMA,
            "category.mcmidi.keybinds"));

    @Override
    public void onInitializeClient() {
        ConfigManager.getInstance().getProvider().register();
        ModClientNetworking.registerS2CPackets();

        ClientCommandRegistrationCallback.EVENT.register(MidiClientCommand::register);

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            MidiPlayerState.getInstance().clearAll();
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.wasPressed()) {
                client.setScreen(new MidiControlCenterScreen());
            }
        });
    }
}
