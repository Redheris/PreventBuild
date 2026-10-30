package dev.redheris.preventbuild.gui;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import dev.redheris.preventbuild.PreventBuildClient;
import dev.redheris.preventbuild.conditions.ConditionConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.Layout;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.minecraft.util.CommonColors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ConditionConfigsEntry extends AbstractWidget implements Layout {
    private final Layout layout;
    private final List<GuiEventListener> children = new ArrayList<>();
    private final ConditionConfig conditionConfig;
    private final Button configNameBtn;
    private final StringWidget activeLabel;

    public ConditionConfigsEntry(ConditionConfig config, Button configNameBtn, StringWidget activeLabel,
                                 int x, int y, int width, int height, Component message, Layout layout) {
        super(x, y, width, height, message);
        this.layout = layout;
        layout.visitWidgets(children::add);
        this.conditionConfig = config;
        this.configNameBtn = configNameBtn;
        this.activeLabel = activeLabel;
    }

    //~ gui_rendering
    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        float alpha = isHoveredOrFocused() ? 0.6f : 0.0f;

        guiGraphics.fill(getX(), getY(), getRight(), getBottom(), ARGB.black(alpha));
        if (isMouseOver(mouseX, mouseY)) {
            guiGraphics.requestCursor(CursorTypes.POINTING_HAND);

            //~ if >=26.1 'vLine' -> 'verticalLine' {
            guiGraphics.vLine(getX(), getY(), getBottom(), ARGB.white(0.6f));
            guiGraphics.vLine(getRight() - 1, getY(), getBottom(), ARGB.white(0.6f));
            //~}

            if (configNameBtn.isMouseOver(mouseX, mouseY)) {
                guiGraphics.setTooltipForNextFrame(Component.translatable("preventbuild.open_file"), mouseX, mouseY);
            } else {
                guiGraphics.setTooltipForNextFrame(Component.translatable("preventbuild.toggle_config"), mouseX, mouseY);
            }
        }
        layout.visitWidgets(w -> w.render(guiGraphics, mouseX, mouseY, partialTick));
    }
    //~ !gui_rendering

    @Override
    public void setX(int x) {
        super.setX(x);
        layout.setX(x);
    }

    @Override
    public void setY(int y) {
        super.setY(y);
        layout.setY(y);
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {}

    @Override
    public void visitChildren(@NotNull Consumer<LayoutElement> visitor) {
        visitor.accept(layout);
    }

    //~ widget_events
    @Override
    public boolean mouseClicked(@NotNull MouseButtonEvent event, boolean isDoubleClick) {
        for (GuiEventListener w : children) {
            if (w.isMouseOver(event.x(), event.y())) {
                return w.mouseClicked(event, isDoubleClick);
            }
        }
        boolean isActive = PreventBuildClient.conditionsManager.isActive(conditionConfig);
        PreventBuildClient.conditionsManager.setActive(conditionConfig, !isActive);
        this.updateActiveLabel(!isActive);
        layout.arrangeElements();
        return true;
    }
    //~ !widget_events

    @Override
    public void arrangeElements() {
        layout.setPosition(getX(), getY());
        layout.arrangeElements();
    }

    //? if >=26.2 {
    /*@Override
    public void removeChildren() {
        layout.removeChildren();
        children.clear();
    }
    *///?}

    private void updateActiveLabel(boolean newStatus) {
        if (newStatus) {
            activeLabel.setMessage(Component.translatable("preventbuild.active").withColor(CommonColors.GREEN));
        } else {
            activeLabel.setMessage(Component.translatable("preventbuild.inactive").withColor(CommonColors.SOFT_RED));
        }
    }
}
