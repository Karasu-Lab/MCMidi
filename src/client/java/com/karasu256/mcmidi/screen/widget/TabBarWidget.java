package com.karasu256.mcmidi.screen.widget;

import net.minecraft.client.gui.tab.Tab;
import net.minecraft.client.gui.tab.TabManager;
import net.minecraft.client.gui.widget.TabNavigationWidget;

public class TabBarWidget extends com.karasu256.karasunikilib.screen.widget.AbstractTabBar<com.karasu256.karasunikilib.screen.widget.ITabContent> {
    private TabNavigationWidget navigation;
    private final TabManager tabManager;

    public TabBarWidget(TabManager tabManager, int width) {
        this.tabManager = tabManager;
    }

    public void init(int width, com.karasu256.karasunikilib.screen.widget.ITabContent[] tabContents) {
        TabNavigationWidget.Builder builder = TabNavigationWidget.builder(tabManager, width);
        for (com.karasu256.karasunikilib.screen.widget.ITabContent content : tabContents) {
            if (content instanceof Tab tab) {
                builder.tabs(tab);
            }
        }
        this.navigation = builder.build();
    }

    @Override
    public void addTab(com.karasu256.karasunikilib.screen.widget.ITabContent tab, com.karasu256.karasunikilib.screen.widget.ITabContent content) {
        super.addTab(tab, content);
    }

    public TabNavigationWidget getNavigation() {
        return navigation;
    }

    @Override
    public void selectTab(int index) {
        super.selectTab(index);
        if (this.navigation != null) {
            this.navigation.selectTab(index, false);
        }
    }
}
