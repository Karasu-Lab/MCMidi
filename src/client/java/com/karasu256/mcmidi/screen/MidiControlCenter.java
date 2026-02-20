package com.karasu256.mcmidi.screen;

import com.karasu256.mcmidi.Constants;
import com.karasu256.mcmidi.api.midi.ExtendedMidi;
import com.karasu256.mcmidi.client.MidiPlayerState;
import com.karasu256.mcmidi.impl.IMidiPlayer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MidiControlCenter extends Screen {
    private static final Map<Integer, MidiNote> midiNoteMap = new HashMap<>();
    private static final Map<Integer, Instrument> instrumentMap = new HashMap<>();
    private final Screen parent;
    private Instrument[] instruments;
    private int maxInstrumentNameWidth = 0;

    public MidiControlCenter() {
        this(MinecraftClient.getInstance().currentScreen);
        midiNoteMap.clear();
    }

    public MidiControlCenter(Screen parent) {
        this(Text.translatable("mcmidi.midi_control_center"), parent);
        midiNoteMap.clear();
    }

    protected MidiControlCenter(Text title, Screen parent) {
        super(title);
        this.parent = parent;
    }

    private void loadAllInstruments() {
        try {
            IMidiPlayer player = MidiPlayerState.getInstance().getCurrentPlayer();
            if (player instanceof ExtendedMidi midi) {
                Synthesizer synthesizer = midi.getSynthesizer();
                if (synthesizer != null) {
                    this.instruments = synthesizer.getDefaultSoundbank().getInstruments();
                    if (this.instruments != null) {
                        for (int i = 0; i < this.instruments.length && i < 16; i++) {
                            if (this.instruments[i] != null) {
                                instrumentMap.put(i, this.instruments[i]);
                                String channelFormatted = String.format("Ch%d: %s", i, this.instruments[i].getName());
                                int width = this.textRenderer != null ? this.textRenderer.getWidth(channelFormatted)
                                        : channelFormatted.length() * 6;
                                if (width > maxInstrumentNameWidth) {
                                    maxInstrumentNameWidth = width;
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Constants.LOGGER.error("Failed to load instruments", e);
        }
    }

    public void onReceive(MidiMessage message) {
        IMidiPlayer player = MidiPlayerState.getInstance().getCurrentPlayer();
        if (!(player instanceof ExtendedMidi midi)) return;

        this.instruments = midi.getSynthesizer().getDefaultSoundbank().getInstruments();

        if (message instanceof ShortMessage shortMessage) {
            midiNoteMap.put(shortMessage.getChannel(), new MidiNote(shortMessage));
            int channel = shortMessage.getChannel();
            if (instruments != null && channel < instruments.length && instruments[channel] != null) {
                Instrument instrument = instruments[channel];
                instrumentMap.put(channel, instrument);
                String channelFormatted = String.format("Ch%d: %s", channel, instrument.getName());
                int width = this.textRenderer != null ? this.textRenderer.getWidth(channelFormatted)
                        : channelFormatted.length() * 6;
                if (width > maxInstrumentNameWidth) {
                    maxInstrumentNameWidth = width;
                }
            }
        }
    }

    @Override
    protected void init() {
        loadAllInstruments();
        super.init();

        addDrawableChild(
                ButtonWidget.builder(Text.translatable("mcmidi.midi_control_center.open_soundfont_files"), button -> {
                    if (this.client != null) {
                        this.client.setScreen(new SoundFontManagerScreen(this));
                    }
                }).dimensions((this.width / 2) - 205, this.height - 25, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT).build());

        addDrawableChild(
                ButtonWidget.builder(Text.translatable("mcmidi.midi_control_center.open_midi_files"), button -> {
                    if (this.client != null) {
                        this.client.setScreen(new MidiChooseScreen(this));
                    }
                }).dimensions((this.width / 2) - 110, this.height - 25, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT).build());

        addDrawableChild(
                ButtonWidget.builder(Text.translatable("mcmidi.midi_control_center.open_sound_controller"), button -> {
                    if (this.client != null) {
                        this.client.setScreen(new SoundControllerScreen(this));
                    }
                }).dimensions((this.width / 2) - 15, this.height - 25, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT).build());

        addDrawableChild(ButtonWidget.builder(Text.translatable("mcmidi.midi_control_center.close"), button -> {
            this.close();
        }).dimensions((this.width / 2) + 80, this.height - 25, 110, Constants.BUTTON_HEIGHT).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 6, Constants.TITLE_COLOR);

        IMidiPlayer player = MidiPlayerState.getInstance().getCurrentPlayer();
        if (player instanceof ExtendedMidi midi) {
            context.drawCenteredTextWithShadow(this.textRenderer, midi.getPlayingPath(), this.width / 2, 16, Constants.TITLE_COLOR);
        } else {
            context.drawCenteredTextWithShadow(this.textRenderer, Text.translatable("mcmidi.text.no_midi"),
                    this.width / 2, 16, Constants.TITLE_COLOR);
        }

        AtomicInteger offsetY = new AtomicInteger(30);
        Text channelText = Text.translatable("mcmidi.midi.channel");
        int channelTextWidth = this.textRenderer.getWidth(channelText);
        Text statusText = Text.translatable("mcmidi.midi.status");
        int statusTextWidth = this.textRenderer.getWidth(statusText);
        Text data1Text = Text.translatable("mcmidi.midi.data1");
        int data1TextWidth = this.textRenderer.getWidth(data1Text);
        Text data2Text = Text.translatable("mcmidi.midi.data2");
        int data2TextWidth = this.textRenderer.getWidth(data2Text);

        String pos = "0";
        String bpm = "";

        if (player instanceof ExtendedMidi midi) {
            pos = String.format("%.2f", (float) midi.getPosition() / 1000);
            bpm = String.format("%.2f", midi.getBPM());
        }

        float leftMarginRatio = 0.15f;
        float columnSpacingRatio = 0.05f;
        int leftMargin = (int) (this.width * leftMarginRatio);
        int columnSpacing = (int) (this.width * columnSpacingRatio);
        int channelColumnWidth = Math.max(maxInstrumentNameWidth, channelTextWidth);

        int column1X = leftMargin;
        int column2X = column1X + channelColumnWidth + columnSpacing;
        int column3X = column2X + statusTextWidth + columnSpacing;
        int column4X = column3X + data1TextWidth + columnSpacing;
        int infoX = column4X + data2TextWidth + columnSpacing;

        Text postext = Text.literal("Position: " + pos);
        Text bpmText = Text.literal("BPM: " + bpm);

        context.drawText(this.textRenderer, channelText, column1X, offsetY.get(), Constants.TITLE_COLOR, true);
        context.drawText(this.textRenderer, statusText, column2X, offsetY.get(), Constants.TITLE_COLOR, true);
        context.drawText(this.textRenderer, data1Text, column3X, offsetY.get(), Constants.TITLE_COLOR, true);
        context.drawText(this.textRenderer, data2Text, column4X, offsetY.get(), Constants.TITLE_COLOR, true);
        context.drawText(this.textRenderer, postext, infoX, offsetY.get(), Constants.TITLE_COLOR, true);
        context.drawText(this.textRenderer, bpmText, infoX, offsetY.get() + 10, Constants.TITLE_COLOR, true);
        offsetY.set(offsetY.get() + 10);

        if (!instrumentMap.isEmpty()) {
            try {
                instrumentMap.forEach((integer, instrument) -> {
                    if (instrument != null) {
                        String channelFormatted = String.format("Ch%d: %s", integer, instrument.getName());
                        MidiNote midiNote = midiNoteMap.get(integer);
                        String statusFormatted = "-";
                        Text data1Styled = Text.literal("-");
                        Text data2Styled = Text.literal("-");

                        if (midiNote != null) {
                            statusFormatted = String.format("%02d", midiNote.getStatus());
                            if (midiNote.getCommand() == ShortMessage.NOTE_ON) {
                                int key = midiNote.getData1();
                                int octave = (key / 12) - 1;
                                int note = key % 12;
                                String noteName = Constants.NOTE_NAMES[note];
                                int velocity = midiNote.getData2();

                                data1Styled = Text.literal(noteName + octave)
                                        .setStyle(Style.EMPTY.withColor(Constants.getNoteColor(noteName).getRGB()));
                                data2Styled = Text.literal(String.valueOf(velocity));
                            } else if (midiNote.getCommand() == ShortMessage.NOTE_OFF) {
                                data1Styled = Text.literal("OFF").setStyle(Style.EMPTY.withColor(Colors.RED));
                                data2Styled = Text.literal("0");
                            }
                        }

                        context.drawText(MidiControlCenter.this.textRenderer, channelFormatted, column1X,
                                offsetY.get(), Colors.WHITE, true);
                        context.drawText(MidiControlCenter.this.textRenderer, statusFormatted, column2X,
                                offsetY.get(), Colors.WHITE, true);
                        context.drawText(MidiControlCenter.this.textRenderer, data1Styled, column3X, offsetY.get(),
                                Colors.WHITE, true);
                        context.drawText(MidiControlCenter.this.textRenderer, data2Styled, column4X, offsetY.get(),
                                Colors.WHITE, true);
                        offsetY.set(offsetY.get() + 10);
                    }
                });
            } catch (ConcurrentModificationException ignored) {
            }
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void close() {
        IMidiPlayer player = MidiPlayerState.getInstance().getCurrentPlayer();
        if (this.parent != null && player != null) {
            player.stop();
        }
        super.close();
    }

    private record MidiNote(ShortMessage shortMessage) {
        public int getChannel() {
            return shortMessage.getChannel();
        }

        public int getStatus() {
            return shortMessage.getStatus();
        }

        public int getCommand() {
            return shortMessage.getCommand();
        }

        public int getData1() {
            return shortMessage.getData1();
        }

        public int getData2() {
            return shortMessage.getData2();
        }
    }
}
