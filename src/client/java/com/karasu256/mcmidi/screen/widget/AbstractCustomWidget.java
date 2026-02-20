package com.karasu256.mcmidi.screen.widget;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public abstract class AbstractCustomWidget extends ButtonWidget implements IControlWidget {

    protected AbstractCustomWidget(int x, int y, int width, int height, Text message, PressAction onPress) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION_SUPPLIER);
    }

    @Override
    public void tick() {
    }

    @Override
    public void refresh() {
    }
}
