package com.karasu256.mcmidi.screen.widget;

import com.karasu256.karasunikilib.screen.widget.IControlWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;

import java.util.ArrayList;
import java.util.List;

public class PlaybackControlWidget implements IControlWidget {
    private final List<IControlWidget> children = new ArrayList<>();
    private final DirectionalLayoutWidget layout;

    public PlaybackControlWidget(Screen parentScreen) {
        this.layout = DirectionalLayoutWidget.horizontal().spacing(4);

        SeekBarWidget seekBar = new SeekBarWidget(0, 0, 200, 20);
        PlayPauseButtonWidget playPauseButton = new PlayPauseButtonWidget();
        StopButtonWidget stopButton = new StopButtonWidget();
        OpenSoundFontButtonWidget openSoundFontButton = new OpenSoundFontButtonWidget(parentScreen);
        OpenButtonWidget openMidiButton = new OpenButtonWidget(parentScreen);

        this.layout.add(seekBar);
        this.layout.add(playPauseButton);
        this.layout.add(stopButton);
        this.layout.add(openSoundFontButton);
        this.layout.add(openMidiButton);

        this.children.add(seekBar);
        this.children.add(playPauseButton);
        this.children.add(stopButton);
        this.children.add(openSoundFontButton);
        this.children.add(openMidiButton);

        refresh();
    }

    public DirectionalLayoutWidget getLayout() {
        return this.layout;
    }

    @Override
    public void tick() {
        for (IControlWidget child : children) {
            child.tick();
        }
    }

    @Override
    public void refresh() {
        for (IControlWidget child : children) {
            child.refresh();
        }
    }
}
