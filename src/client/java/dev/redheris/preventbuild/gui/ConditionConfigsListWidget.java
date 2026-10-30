package dev.redheris.preventbuild.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractContainerWidget;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.Layout;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ConditionConfigsListWidget extends AbstractContainerWidget implements Layout {
    private final Layout layout;
    private final List<AbstractWidget> children = new ArrayList<>();
    private final int contentHeight;

    public ConditionConfigsListWidget(int x, int y, int width, int height, Component message, Layout layout) {
        super(x, y, width, height, message);
        layout.setPosition(getX(), getY());
        layout.arrangeElements();
        this.contentHeight = layout.getHeight();
        this.layout = layout;
        layout.visitWidgets(this.children::add);
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.enableScissor(getX(), getY(), getRight(), getBottom());

        guiGraphics.fill(
                getX(), getY(), getRight(), getBottom(),
                ARGB.black(0.5f)
        );
        for (AbstractWidget child : children) {
            if (isOverScrollbar(mouseX, mouseY)) {
                child.render(guiGraphics, -1, -1, partialTick);
            } else {
                child.render(guiGraphics, mouseX, mouseY, partialTick);
            }
        }

        renderScrollbar(guiGraphics, mouseX, mouseY);

        guiGraphics.disableScissor();
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean isDoubleClick) {
        if (isOverScrollbar(event.x(), event.y())) {
            return updateScrolling(event);
        }
        return super.mouseClicked(event, isDoubleClick);
    }

    @Override
    public void arrangeElements() {
        layout.arrangeElements();
    }

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
}
