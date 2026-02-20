package com.karasu256.mcmidi.screen.ui;

import com.karasu256.mcmidi.Constants;
import com.karasu256.mcmidi.api.midi.IMidiEngine;
import com.karasu256.mcmidi.api.midi.JavaMidiEngine;
import com.karasu256.mcmidi.client.MidiPlayerState;
import com.karasu256.mcmidi.screen.MidiControlCenterScreen;
import com.karasu256.mcmidi.screen.widget.AbstractTabContent;
import com.karasu256.mcmidi.screen.widget.ITabContent;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class DetailTab extends AbstractTabContent {
    private static final Text TITLE = Text.translatable("mcmidi.tab.detail");

    private final MidiControlCenterScreen parentScreen;
    private final Map<Integer, MidiNote> midiNoteMap = new HashMap<>();
    private final Map<Integer, Instrument> instrumentMap = new HashMap<>();
    private Instrument[] instruments;
    private int maxInstrumentNameWidth = 0;

    public DetailTab(MidiControlCenterScreen parentScreen) {
        super(TITLE);
        this.parentScreen = parentScreen;

        DetailRenderWidget renderWidget = new DetailRenderWidget(parentScreen);
        this.grid.add(renderWidget, 0, 0);
    }

    public void onReceive(MidiMessage message, TextRenderer textRenderer) {
        IMidiEngine engine = MidiPlayerState.getInstance().getCurrentEngine();
        if (!(engine instanceof JavaMidiEngine javaEngine)) return;

        this.instruments = javaEngine.getSynthesizer().getDefaultSoundbank().getInstruments();

        if (message instanceof ShortMessage shortMessage) {
            midiNoteMap.put(shortMessage.getChannel(), new MidiNote(shortMessage));
            int channel = shortMessage.getChannel();
            if (instruments != null && channel < instruments.length && instruments[channel] != null) {
                Instrument instrument = instruments[channel];
                instrumentMap.put(channel, instrument);
                String channelFormatted = String.format("Ch%d: %s", channel, instrument.getName());
                int w = textRenderer != null ? textRenderer.getWidth(channelFormatted)
                        : channelFormatted.length() * 6;
                if (w > maxInstrumentNameWidth) {
                    maxInstrumentNameWidth = w;
                }
            }
        }
    }

    private record MidiNote(ShortMessage shortMessage) {
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

    private class DetailRenderWidget extends ClickableWidget {
        public DetailRenderWidget(MidiControlCenterScreen screen) {
            super(0, 0, screen.width, screen.height, Text.empty());
        }

        @Override
        protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
            TextRenderer textRenderer = parentScreen.getTextRenderer();
            IMidiEngine engine = MidiPlayerState.getInstance().getCurrentEngine();

            int baseX = this.getX();
            int baseY = this.getY();
            AtomicInteger offsetY = new AtomicInteger(baseY);

            Text channelText = Text.translatable("mcmidi.midi.channel");
            int channelTextWidth = textRenderer.getWidth(channelText);
            Text statusText = Text.translatable("mcmidi.midi.status");
            int statusTextWidth = textRenderer.getWidth(statusText);
            Text data1Text = Text.translatable("mcmidi.midi.data1");
            int data1TextWidth = textRenderer.getWidth(data1Text);
            Text data2Text = Text.translatable("mcmidi.midi.data2");

            String pos = "0";
            String bpm = "";

            if (engine != null) {
                pos = String.format("%.2f", (float) engine.getPosition() / 1_000_000);
                bpm = String.format("%.2f", engine.getBPM());
            }

            int columnSpacing = 20;
            int channelColumnWidth = Math.max(maxInstrumentNameWidth, channelTextWidth);

            int column1X = baseX;
            int column2X = column1X + channelColumnWidth + columnSpacing;
            int column3X = column2X + statusTextWidth + columnSpacing;
            int column4X = column3X + data1TextWidth + columnSpacing;
            int infoX = column4X + textRenderer.getWidth(data2Text) + columnSpacing;

            Text postext = Text.literal("Position: " + pos);
            Text bpmText = Text.literal("BPM: " + bpm);

            context.drawText(textRenderer, channelText, column1X, offsetY.get(), Constants.TITLE_COLOR, true);
            context.drawText(textRenderer, statusText, column2X, offsetY.get(), Constants.TITLE_COLOR, true);
            context.drawText(textRenderer, data1Text, column3X, offsetY.get(), Constants.TITLE_COLOR, true);
            context.drawText(textRenderer, data2Text, column4X, offsetY.get(), Constants.TITLE_COLOR, true);
            context.drawText(textRenderer, postext, infoX, offsetY.get(), Constants.TITLE_COLOR, true);
            context.drawText(textRenderer, bpmText, infoX, offsetY.get() + 10, Constants.TITLE_COLOR, true);
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

                            context.drawText(textRenderer, channelFormatted, column1X, offsetY.get(), Colors.WHITE, true);
                            context.drawText(textRenderer, statusFormatted, column2X, offsetY.get(), Colors.WHITE, true);
                            context.drawText(textRenderer, data1Styled, column3X, offsetY.get(), Colors.WHITE, true);
                            context.drawText(textRenderer, data2Styled, column4X, offsetY.get(), Colors.WHITE, true);
                            offsetY.set(offsetY.get() + 10);
                        }
                    });
                } catch (ConcurrentModificationException ignored) {
                }
            }
        }

        @Override
        protected void appendClickableNarrations(net.minecraft.client.gui.screen.narration.NarrationMessageBuilder builder) {
        }
    }
}
