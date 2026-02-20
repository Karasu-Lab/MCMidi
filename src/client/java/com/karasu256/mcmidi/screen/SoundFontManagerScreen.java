package com.karasu256.mcmidi.screen;

import com.karasu256.mcmidi.Constants;
import com.karasu256.mcmidi.api.FileManager;
import com.karasu256.mcmidi.api.midi.IMidiEngine;
import com.karasu256.mcmidi.client.MidiPlayerState;
import com.karasu256.mcmidi.config.IConfigManager;
import com.karasu256.mcmidi.config.ModConfig;
import com.karasu256.mcmidi.impl.IMidiScreen;
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

import java.io.File;
import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class SoundFontManagerScreen extends GameOptionsScreen implements IMidiScreen {
    private final IConfigManager configManager;
    private final ModConfig config;
    private final FileManager<?> resourceManager;
    private SoundFontOptionListWidget soundFontOptionListWidget;

    public SoundFontManagerScreen(Screen parent, IConfigManager configManager) {
        super(parent, MinecraftClient.getInstance().options, Text.translatable("mcmidi.options.title"));
        this.layout.setFooterHeight(Constants.FOOTER_HEIGHT);
        this.configManager = configManager;
        this.config = configManager.getConfig();
        this.resourceManager = MidiPlayerState.getInstance().getSoundFontManager();

        IMidiEngine current = MidiPlayerState.getInstance().getCurrentEngine();
        if (current != null) {
            current.stop();
            current.clear();
        }
    }

    protected void initBody() {
        this.soundFontOptionListWidget = this.layout.addBody(new SoundFontOptionListWidget(this.client));
    }

    protected void initFooter() {
        DirectionalLayoutWidget directionalLayoutWidget = (this.layout.addFooter(DirectionalLayoutWidget.vertical()))
                .spacing(8);
        DirectionalLayoutWidget directionalLayoutWidget2 = directionalLayoutWidget
                .add(DirectionalLayoutWidget.horizontal().spacing(8));
        directionalLayoutWidget2
                .add(ButtonWidget.builder(Text.translatable("text.mcmidi.opensoundfontdirectory"), (button) -> {
                    this.resourceManager.openDirectory();
                }).build());
        directionalLayoutWidget2.add(ButtonWidget.builder(ScreenTexts.DONE, (button) -> {
            this.onDone();
        }).build());
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        super.resize(client, width, height);
        this.soundFontOptionListWidget.position(this.width, this.layout);
    }

    @Override
    protected void addOptions() {
    }

    private void onDone() {
        SoundFontOptionListWidget.SoundFontEntry selected = this.soundFontOptionListWidget.getSelectedOrNull();

        if (selected != null) {
            if (selected.isDefault) {
                config.general.soundFontPath = "";
            } else {
                Optional<File> resolved = resourceManager.resolveFile(selected.soundFontName);
                if (resolved.isPresent()) {
                    config.general.soundFontPath = resolved.get().getAbsolutePath();
                } else {
                    Constants.LOGGER.error("Soundfont file does not exist, using default");
                    config.general.soundFontPath = "";
                }
            }
        }

        configManager.getProvider().save(config);
        this.close();
    }

    @Override
    public void close() {
        super.close();
    }

    @Override
    public void playCurrent() {
    }

    @Override
    public boolean shouldPause() {
        return true;
    }

    private class SoundFontOptionListWidget
            extends AlwaysSelectedEntryListWidget<SoundFontOptionListWidget.SoundFontEntry> {
        public SoundFontOptionListWidget(MinecraftClient minecraftClient) {
            super(minecraftClient, SoundFontManagerScreen.this.width,
                    SoundFontManagerScreen.this.height - Constants.HEADER_HEIGHT - Constants.FOOTER_HEIGHT,
                    Constants.HEADER_HEIGHT, Constants.LIST_ITEM_HEIGHT);

            SoundFontEntry defaultEntry = new SoundFontEntry("", true);
            addEntry(defaultEntry);

            if (SoundFontManagerScreen.this.config.general.soundFontPath == null
                    || SoundFontManagerScreen.this.config.general.soundFontPath.isEmpty()) {
                this.setSelected(defaultEntry);
            }

            List<String> soundfonts = resourceManager.listLocalFiles();
            for (String soundfont : soundfonts) {
                SoundFontEntry entry = new SoundFontEntry(soundfont);
                addEntry(entry);
                if (soundfont.equals(SoundFontManagerScreen.this.config.general.soundFontPath)) {
                    this.setSelected(entry);
                }
            }

            if (this.getSelectedOrNull() != null) {
                this.centerScrollOn(this.getSelectedOrNull());
            }
        }

        public int getRowWidth() {
            return super.getRowWidth() + 50;
        }

        public class SoundFontEntry extends AlwaysSelectedEntryListWidget.Entry<SoundFontEntry> {
            private final String soundFontName;
            private final boolean isDefault;
            private long clickTime;

            public SoundFontEntry(String soundFontName) {
                this(soundFontName, false);
            }

            public SoundFontEntry(String soundFontName, boolean isDefault) {
                this.soundFontName = soundFontName;
                this.isDefault = isDefault;
            }

            @Override
            public Text getNarration() {
                return Text.translatable("narrator.select",
                        isDefault ? Text.translatable("mcmidi.options.default_soundfont") : this.soundFontName);
            }

            @Override
            public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight,
                               int mouseX, int mouseY, boolean hovered, float tickDelta) {
                TextRenderer textRenderer = SoundFontManagerScreen.this.textRenderer;
                Text soundFontDisplay = isDefault ? Text.translatable("mcmidi.options.default_soundfont")
                        : Text.literal(this.soundFontName);
                int width = SoundFontOptionListWidget.this.width / 2;
                int height = y + entryHeight / 2;
                context.drawCenteredTextWithShadow(textRenderer, soundFontDisplay, width, height - 9 / 2, -1);
            }

            @Override
            public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
                if (KeyCodes.isToggle(keyCode)) {
                    this.onPressed();
                    SoundFontManagerScreen.this.onDone();
                    return true;
                } else {
                    return super.keyPressed(keyCode, scanCode, modifiers);
                }
            }

            private void onPressed() {
                SoundFontOptionListWidget.this.setSelected(this);
            }

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int button) {
                this.onPressed();
                if (Util.getMeasuringTimeMs() - this.clickTime < Constants.DOUBLE_CLICK_THRESHOLD_MS) {
                    SoundFontManagerScreen.this.onDone();
                }
                this.clickTime = Util.getMeasuringTimeMs();
                return super.mouseClicked(mouseX, mouseY, button);
            }
        }
    }
}
