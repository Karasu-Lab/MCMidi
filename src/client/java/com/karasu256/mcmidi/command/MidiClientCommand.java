package com.karasu256.mcmidi.command;

import com.karasu256.karasunikilib.command.AbstractCommand;
import com.karasu256.mcmidi.Constants;
import com.karasu256.mcmidi.client.MidiPlayerState;
import com.karasu256.mcmidi.impl.IResourceManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class MidiClientCommand extends AbstractCommand {

    public MidiClientCommand() {
        addSubCommand(new PlaySubCommand());
        addSubCommand(new StopSubCommand());
    }

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        AbstractCommand.register(dispatcher, new MidiClientCommand());
    }

    @Override
    public String getName() {
        return "midi";
    }

    public static class PlaySubCommand extends AbstractCommand {
        @Override
        public String getName() {
            return "play";
        }

        @Override
        public ArgumentBuilder<FabricClientCommandSource, ?> getArgumentBuilder() {
            return ClientCommandManager.literal(getName())
                    .then(ClientCommandManager.argument("file", StringArgumentType.greedyString())
                            .suggests((ctx, builder) -> {
                                IResourceManager manager = MidiPlayerState.getInstance().getMidiManager();
                                List<String> files = manager.listLocalFiles();
                                for (String file : files) {
                                    builder.suggest(file);
                                }
                                return builder.buildFuture();
                            })
                            .executes(ctx -> {
                                execute(ctx);
                                return 1;
                            })
                    );
        }

        @Override
        public <T> void execute(CommandContext<T> context) {
            if (!(context.getSource() instanceof FabricClientCommandSource source)) return;

            String filename = StringArgumentType.getString(context, "file");
            MidiPlayerState state = MidiPlayerState.getInstance();
            IResourceManager manager = state.getMidiManager();

            try {
                if (!manager.canLoad(filename)) {
                    source.sendError(Text.translatable("command.mcmidi.error.file_not_found", filename)
                            .copy().formatted(Formatting.RED));
                    return;
                }

                byte[] data = manager.loadData(filename);
                state.playMidi(data);
                source.sendFeedback(Text.translatable("command.mcmidi.play.success", filename)
                        .copy().formatted(Formatting.GREEN));
            } catch (Exception e) {
                source.sendError(Text.translatable("command.mcmidi.error.generic", e.getMessage())
                        .copy().formatted(Formatting.RED));
            }
        }
    }

    public static class StopSubCommand extends AbstractCommand {
        @Override
        public String getName() {
            return "stop";
        }

        @Override
        public ArgumentBuilder<FabricClientCommandSource, ?> getArgumentBuilder() {
            return ClientCommandManager.literal(getName())
                    .executes(ctx -> {
                        execute(ctx);
                        return 1;
                    });
        }

        @Override
        public <T> void execute(CommandContext<T> context) {
            if (!(context.getSource() instanceof FabricClientCommandSource source)) return;

            MidiPlayerState state = MidiPlayerState.getInstance();
            if (state.getCurrentPlayer() == null) {
                source.sendError(Text.translatable("command.mcmidi.error.no_midi")
                        .copy().formatted(Formatting.RED));
                return;
            }

            state.stopCurrent();
            source.sendFeedback(Text.translatable("command.mcmidi.stop.success")
                    .copy().formatted(Formatting.GREEN));
        }
    }
}
