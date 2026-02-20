package com.karasu256.mcmidi.screen.widget;

import net.minecraft.client.gui.tab.GridScreenTab;
import net.minecraft.text.Text;

public abstract class AbstractTabContent extends GridScreenTab implements ITabContent {
    protected AbstractTabContent(Text title) {
        super(title);
    }

    @Override
    public void tick() {
    }

    @Override
    public void refresh() {
    }

    @Override
    public ITabContent getContent() {
        return this;
    }
}
