package dev.redheris.preventbuild.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractContainerWidget;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.Layout;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

//? if >1.21.4
import net.minecraft.client.input.MouseButtonEvent;

public class ConditionConfigsListWidget extends AbstractContainerWidget implements Layout {
    private final Layout layout;
    private final List<AbstractWidget> children = new ArrayList<>();
    private final int contentHeight;

    public ConditionConfigsListWidget(int x, int y, int width, int height, Component message, Layout layout) {
        super(x, y, width, height, message/*? if >=26.1 {*//*, defaultSettings(9)*//*?}*/);
        layout.setPosition(getX(), getY());
        layout.arrangeElements();
        this.contentHeight = layout.getHeight();
        this.layout = layout;
        layout.visitWidgets(this.children::add);
    }

    //~ gui_rendering
    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.enableScissor(getX(), getY(), getRight(), getBottom());

        float alpha = 0.5f;
        //? if <=1.21.4
        //int alphaInt = (int) (alpha * 255);

        //~ if > 1.21.4 'ARGB.color(alphaInt, 0)' -> 'ARGB.black(alpha)'
        int color = ARGB.black(alpha);
        guiGraphics.fill(
                getX(), getY(), getRight(), getBottom(),
                color
        );
        for (AbstractWidget child : children) {
            if (isOverScrollbar(mouseX, mouseY)) {
                child.render(guiGraphics, -1, -1, partialTick);
            } else {
                child.render(guiGraphics, mouseX, mouseY, partialTick);
            }
        }

        //~ if >1.21.4 'guiGraphics' -> 'guiGraphics, mouseX, mouseY'
        renderScrollbar(guiGraphics, mouseX, mouseY);

        guiGraphics.disableScissor();
    }
    //~ !gui_rendering

    //~ widget_events
    @Override
    public boolean mouseClicked(@NotNull MouseButtonEvent mouseEvent, boolean isDoubleClick) {
        if (isOverScrollbar(mouseEvent.x(), mouseEvent.y())) {
            //~ if >1.21.4 'mouseX, mouseY, button' -> 'mouseEvent'
            return updateScrolling(mouseEvent);
        }
        return super.mouseClicked(mouseEvent, isDoubleClick);
    }
    //~ !widget_events

    @Override
    public void arrangeElements() {
        layout.arrangeElements();
    }

    //? if >=26.2 {
    /*@Override
    public void removeChildren() {
        layout.removeChildren();
        children.clear();
    }
    *///?}

    @Override
    public void visitChildren(@NotNull Consumer<LayoutElement> visitor) {
        children.forEach(visitor);
    }

    @Override
    protected int contentHeight() {
        return contentHeight;
    }

    @Override
    public void setScrollAmount(double scrollAmount) {
        super.setScrollAmount(scrollAmount);
        this.layout.setY(this.getRectangle().top() - (int) this.scrollAmount());
    }

    @Override
    protected double scrollRate() {
        return 9;
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {
    }

    @Override
    public @NotNull List<? extends GuiEventListener> children() {
        return children;
    }

    //? if <=1.21.4 {
    /*private boolean isOverScrollbar(double mouseX, double mouseY) {
        return mouseX >= (double)this.scrollBarX() && mouseX <= (double)(this.scrollBarX() + 6) && mouseY >= (double)this.getY() && mouseY < (double)this.getBottom();
    }
    *///?}
}
