package com.karasu256.mcmidi.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModLangProviders {
    public static void addProviders(FabricDataGenerator.Pack pack) {
        pack.addProvider(EnglishLang::new);
        pack.addProvider(JapaneseLangProvider::new);
    }

    public static class JapaneseLangProvider extends FabricLanguageProvider {
        protected JapaneseLangProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
            super(dataOutput, "ja_jp", registryLookup);
        }

        @Override
        public void generateTranslations(RegistryWrapper.WrapperLookup registryLookup, TranslationBuilder translationBuilder) {
        }
    }

    public static class EnglishLang extends FabricLanguageProvider {
        protected EnglishLang(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
            super(dataOutput, "en_us", registryLookup);
        }

        @Override
        public void generateTranslations(RegistryWrapper.WrapperLookup registryLookup, TranslationBuilder translationBuilder) {
            translationBuilder.add("text.autoconfig.mcmidi.title", "MCMidi");
            translationBuilder.add("mcmidi.options.title", "MCMidi Options");
            translationBuilder.add("text.autoconfig.mcmidi.option.midiVolume", "MIDI Volume");
            translationBuilder.add("text.mcmidi.opensoundfontdirectory", "Open SoundFont Directory");
            translationBuilder.add("mcmidi.midi_control_center", "Midi Control Center");
            translationBuilder.add("mcmidi.midi_control_center.open_midi_files", "MIDI");
            translationBuilder.add("text.mcmidi.openmididirectory", "Open MIDI Directory");
            translationBuilder.add("mcmidi.midi_control_center.open_soundfont_files", "SoundFonts");
            translationBuilder.add("mcmidi.midi_control_center.close", "Close");
            translationBuilder.add("mcmidi.midi_control_center.open_sound_controller", "Sound Controller");
            translationBuilder.add("mcmidi.midi_sound_controller", "MIDI Sound Controller");
            translationBuilder.add("mcmidi.midi.channel", "Channel");
            translationBuilder.add("mcmidi.midi.status", "Status");
            translationBuilder.add("mcmidi.midi.data1", "Octave");
            translationBuilder.add("mcmidi.midi.data2", "Velocity");
            translationBuilder.add("category.mcmidi.keybinds", "MCMidi");
            translationBuilder.add("key.midi.open", "Open Midi Control Center");
            translationBuilder.add("mcmidi.text.no_midi", "No MIDI is playing");
            translationBuilder.add("mcmidi.options.default_soundfont", "Default");
            translationBuilder.add("mcmidi.sound_controller.play", "Play");
            translationBuilder.add("mcmidi.sound_controller.pause", "Pause");
            translationBuilder.add("mcmidi.sound_controller.stop", "Stop");
            translationBuilder.add("mcmidi.sound_controller.now_playing", "Now Playing:");
            translationBuilder.add("mcmidi.sound_controller.state.playing", "Playing");
            translationBuilder.add("mcmidi.sound_controller.state.paused", "Paused");
            translationBuilder.add("mcmidi.sound_controller.no_midi", "No MIDI loaded");
            translationBuilder.add("command.mcmidi.play.success", "Playing: %s");
            translationBuilder.add("command.mcmidi.stop.success", "Stopped playback");
            translationBuilder.add("command.mcmidi.error.no_midi", "No MIDI is currently playing");
            translationBuilder.add("command.mcmidi.error.file_not_found", "File not found: %s");
            translationBuilder.add("command.mcmidi.config.get.success", "%s = %s");
            translationBuilder.add("command.mcmidi.config.set.success", "Set %s to %s");
            translationBuilder.add("command.mcmidi.error.generic", "Error: %s");
            translationBuilder.add("command.mcmidi.error.unknown_field", "Unknown field: %s");
        }
    }
}
