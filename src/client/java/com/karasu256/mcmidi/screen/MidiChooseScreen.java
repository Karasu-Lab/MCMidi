package com.karasu256.mcmidi.screen;

import com.karasu256.mcmidi.Constants;
import com.karasu256.mcmidi.client.MidiPlayerState;
import com.karasu256.mcmidi.impl.IMidiPlayer;
import com.karasu256.mcmidi.impl.IMidiScreen;
import com.karasu256.mcmidi.api.FileManager;
import com.karasu256.mcmidi.api.midi.ExtendedMidi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.input.KeyCodes;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

@Environment(EnvType.CLIENT)
public class MidiChooseScreen extends GameOptionsScreen implements IMidiScreen {
    private static final ExecutorService MIDI_PLAYER_POOL;

    static {
        final ThreadFactory factory = r -> {
            Thread thread = new Thread(r);
            thread.setName("MidiPlayerThread");
            return thread;
        };
        MIDI_PLAYER_POOL = Executors.newSingleThreadExecutor(factory);
    }

    private final FileManager<?> resourceManager;
    private MidiListWidget midiFileListWidget;

    public MidiChooseScreen(Screen parent) {
        super(parent, MinecraftClient.getInstance().options, Text.translatable("mcmidi.options.title"));
        this.layout.setFooterHeight(Constants.FOOTER_HEIGHT);
        this.resourceManager = MidiPlayerState.getInstance().getMidiManager();

        IMidiPlayer current = MidiPlayerState.getInstance().getCurrentPlayer();
        if (current != null) {
            current.stop();
            current.clear();
        }
    }

    protected void initBody() {
        this.midiFileListWidget = this.layout.addBody(new MidiListWidget(this.client));
    }

    protected void initFooter() {
        DirectionalLayoutWidget directionalLayoutWidget = (this.layout.addFooter(DirectionalLayoutWidget.vertical()))
                .spacing(8);
        DirectionalLayoutWidget directionalLayoutWidget2 = directionalLayoutWidget
                .add(DirectionalLayoutWidget.horizontal().spacing(8));
        directionalLayoutWidget2
                .add(ButtonWidget.builder(Text.translatable("text.mcmidi.openmididirectory"), (button) -> {
                    this.resourceManager.openDirectory();
                }).build());
        directionalLayoutWidget2.add(ButtonWidget.builder(ScreenTexts.DONE, (button) -> {
            MIDI_PLAYER_POOL.submit(this::onDone);
        }).build());
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        super.resize(client, width, height);
        this.midiFileListWidget.position(this.width, this.layout);
    }

    @Override
    protected void addOptions() {
    }

    @Override
    public void close() {
        super.close();
    }

    private void onDone() {
        MidiListWidget.MidiEntry selected = this.midiFileListWidget.getSelectedOrNull();

        if (selected != null) {
            try {
                if (!resourceManager.canLoad(selected.path)) {
                    Constants.LOGGER.info("File not found: {}", selected.path);
                    return;
                }

                byte[] data = resourceManager.loadData(selected.path);
                MidiPlayerState state = MidiPlayerState.getInstance();
                state.stopCurrent();

                ExtendedMidi midi = new ExtendedMidi(data);
                midi.setDisplayName(selected.path);
                state.setCurrentPlayer(midi);
                midi.play();
            } catch (Exception e) {
                Constants.LOGGER.error("Failed to play midi file: {}", selected.path);
                Constants.LOGGER.error(e.getMessage());
            }
        }
        this.close();
    }

    @Override
    public void playCurrent() {
    }

    @Override
    public boolean shouldPause() {
        return true;
    }

    private class MidiListWidget extends AlwaysSelectedEntryListWidget<MidiListWidget.MidiEntry> {
        public MidiListWidget(MinecraftClient minecraftClient) {
            super(minecraftClient, MidiChooseScreen.this.width,
                    MidiChooseScreen.this.height - Constants.HEADER_HEIGHT - Constants.FOOTER_HEIGHT,
                    Constants.HEADER_HEIGHT, Constants.LIST_ITEM_HEIGHT);

            List<String> paths = resourceManager.listLocalFiles();
            for (String path : paths) {
                addEntry(new MidiEntry(path));
            }

            if (this.getSelectedOrNull() != null) {
                this.centerScrollOn(this.getSelectedOrNull());
            }
        }

        public int getRowWidth() {
            return super.getRowWidth() + 50;
        }

        public class MidiEntry extends AlwaysSelectedEntryListWidget.Entry<MidiEntry> {
            private final String path;
            private long clickTime;

            public MidiEntry(String path) {
                this.path = path;
            }

            @Override
            public Text getNarration() {
                return Text.translatable("narrator.select", this.path);
            }

            @Override
            public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight,
                               int mouseX, int mouseY, boolean hovered, float tickDelta) {
                TextRenderer textRenderer = MidiChooseScreen.this.textRenderer;
                Text midifilepath = Text.literal(this.path);
                int width = MidiListWidget.this.width / 2;
                int height = y + entryHeight / 2;
                context.drawCenteredTextWithShadow(textRenderer, midifilepath, width, height - 9 / 2, -1);
            }

            @Override
            public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
                if (KeyCodes.isToggle(keyCode)) {
                    this.onPressed();
                    MidiChooseScreen.this.onDone();
                    return true;
                } else {
                    return super.keyPressed(keyCode, scanCode, modifiers);
                }
            }

            private void onPressed() {
                MidiListWidget.this.setSelected(this);
            }

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int button) {
                this.onPressed();
                if (Util.getMeasuringTimeMs() - this.clickTime < Constants.DOUBLE_CLICK_THRESHOLD_MS) {
                    MidiChooseScreen.this.onDone();
                }
                this.clickTime = Util.getMeasuringTimeMs();
                return super.mouseClicked(mouseX, mouseY, button);
            }
        }
    }
}
