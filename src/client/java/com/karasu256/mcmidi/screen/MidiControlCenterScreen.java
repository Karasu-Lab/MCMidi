package com.karasu256.mcmidi.screen;

import com.karasu256.mcmidi.screen.ui.DetailTab;
import com.karasu256.mcmidi.screen.ui.NodesTab;
import com.karasu256.mcmidi.screen.ui.PianoTab;
import com.karasu256.mcmidi.screen.ui.WaveformTab;
import com.karasu256.mcmidi.screen.widget.IControlWidget;
import com.karasu256.mcmidi.screen.widget.PlaybackControlWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tab.Tab;
import net.minecraft.client.gui.tab.TabManager;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.TabNavigationWidget;
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import javax.sound.midi.MidiMessage;

public class MidiControlCenterScreen extends Screen {
    public static final Identifier TAB_HEADER_BACKGROUND_TEXTURE = Identifier.ofVanilla("textures/gui/tab_header_background.png");

    private final ThreePartsLayoutWidget layout = new ThreePartsLayoutWidget(this);
    private final TabManager tabManager = new TabManager(
            element -> this.addDrawableChild(element),
            child -> this.remove(child)
    );
    private final Screen parent;
    private TabNavigationWidget tabNavigation;
    private IControlWidget playbackControl;
    private PlaybackControlWidget playbackControlWidget;

    public MidiControlCenterScreen() {
        this(MinecraftClient.getInstance().currentScreen);
    }

    public MidiControlCenterScreen(Screen parent) {
        super(Text.translatable("mcmidi.midi_control_center"));
        this.parent = parent;
    }

    public TextRenderer getTextRenderer() {
        return this.textRenderer;
    }

    @Override
    protected void init() {
        this.tabNavigation = TabNavigationWidget.builder(this.tabManager, this.width)
                .tabs(new Tab[]{
                        new DetailTab(this),
                        new NodesTab(),
                        new PianoTab(),
                        new WaveformTab()
                })
                .build();
        this.addDrawableChild(this.tabNavigation);

        this.playbackControlWidget = new PlaybackControlWidget(this);
        this.playbackControl = this.playbackControlWidget;
        DirectionalLayoutWidget footer = this.layout.addFooter(this.playbackControlWidget.getLayout());

        this.layout.forEachChild(child -> {
            child.setNavigationOrder(1);
            this.addDrawableChild(child);
        });

        this.tabNavigation.selectTab(0, false);
        this.refreshWidgetPositions();
    }

    @Override
    protected void refreshWidgetPositions() {
        if (this.tabNavigation != null) {
            this.tabNavigation.setWidth(this.width);
            this.tabNavigation.init();
            int headerBottom = this.tabNavigation.getNavigationFocus().getBottom();
            ScreenRect screenRect = new ScreenRect(0, headerBottom, this.width, this.height - this.layout.getFooterHeight() - headerBottom);
            this.tabManager.setTabArea(screenRect);
            this.layout.setHeaderHeight(headerBottom);
            this.layout.refreshPositions();
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawTexture(RenderPipelines.GUI_TEXTURED, Screen.FOOTER_SEPARATOR_TEXTURE, 0, this.height - this.layout.getFooterHeight() - 2, 0.0F, 0.0F, this.width, 2, 32, 2);
    }

    @Override
    protected void renderDarkening(DrawContext context) {
        context.drawTexture(RenderPipelines.GUI_TEXTURED, TAB_HEADER_BACKGROUND_TEXTURE, 0, 0, 0.0F, 0.0F, this.width, this.layout.getHeaderHeight(), 16, 16);
        this.renderDarkening(context, 0, this.layout.getHeaderHeight(), this.width, this.height);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.playbackControl != null) {
            this.playbackControl.tick();
            this.playbackControl.refresh();
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.tabNavigation != null && this.tabNavigation.trySwitchTabsWithKey(keyCode)) {
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    public void onReceive(MidiMessage message) {
        Tab currentTab = this.tabManager.getCurrentTab();
        if (currentTab instanceof DetailTab detailTab) {
            detailTab.onReceive(message, this.textRenderer);
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void close() {
        if (this.client != null) {
            this.client.setScreen(this.parent);
        }
    }
}
