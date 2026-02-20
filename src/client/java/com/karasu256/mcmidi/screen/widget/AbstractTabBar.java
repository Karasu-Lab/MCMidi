package com.karasu256.mcmidi.screen.widget;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTabBar<T extends IControlWidget> implements ITabBar<T> {
    protected final List<TabEntry<T>> tabs = new ArrayList<>();
    protected int selectedIndex = -1;

    @Override
    public void addTab(T tab, ITabContent content) {
        tabs.add(new TabEntry<>(tab, content));
        if (selectedIndex == -1) {
            selectTab(0);
        }
    }

    @Override
    public void selectTab(int index) {
        if (index >= 0 && index < tabs.size()) {
            selectedIndex = index;
            refresh();
        }
    }

    @Override
    public ITabContent getActiveContent() {
        if (selectedIndex >= 0 && selectedIndex < tabs.size()) {
            return tabs.get(selectedIndex).content();
        }
        return null;
    }

    @Override
    public void tick() {
        for (TabEntry<T> entry : tabs) {
            entry.tab().tick();
        }
        ITabContent activeContent = getActiveContent();
        if (activeContent != null) {
            activeContent.tick();
        }
    }

    @Override
    public void refresh() {
        for (TabEntry<T> entry : tabs) {
            entry.tab().refresh();
        }
        ITabContent activeContent = getActiveContent();
        if (activeContent != null) {
            activeContent.refresh();
        }
    }

    protected record TabEntry<T extends IControlWidget>(T tab, ITabContent content) {
    }
}
