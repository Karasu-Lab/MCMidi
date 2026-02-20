package com.karasu256.mcmidi.screen.widget;

import net.minecraft.client.gui.tab.Tab;
import net.minecraft.client.gui.tab.TabManager;
import net.minecraft.client.gui.widget.TabNavigationWidget;

public class TabBarWidget extends AbstractTabBar<ITabContent> {
    private final TabNavigationWidget navigation;
    private final TabManager tabManager;

    public TabBarWidget(TabManager tabManager, int width) {
        this.tabManager = tabManager;
        this.navigation = TabNavigationWidget.builder(tabManager, width).build();
    }

    @Override
    public void addTab(ITabContent tab, ITabContent content) {
        super.addTab(tab, content);
    }

    public TabNavigationWidget getNavigation() {
        return navigation;
    }

    @Override
    public void selectTab(int index) {
        super.selectTab(index);
        this.navigation.selectTab(index, false);
    }
}
