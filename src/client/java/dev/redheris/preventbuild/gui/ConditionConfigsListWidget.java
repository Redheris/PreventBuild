package dev.redheris.preventbuild.gui;

import dev.redheris.preventbuild.util.ARGB;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.Layout;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

//? if >1.21.4
import net.minecraft.client.input.MouseButtonEvent;
//? if >1.21.1
import net.minecraft.client.gui.components.AbstractContainerWidget;
//? if 1.21.1 {
/*import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.components.AbstractScrollWidget;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import org.jetbrains.annotations.Nullable;
*///?}

//~ if 1.21.1 'extends AbstractContainerWidget implements Layout' -> 'extends AbstractScrollWidget implements ContainerEventHandler, Layout'
public class ConditionConfigsListWidget extends AbstractContainerWidget implements Layout {
    private final Layout layout;
    private final List<AbstractWidget> children = new ArrayList<>();
    private final int contentHeight;
    //? if 1.21.1 {
    /*@Nullable
    private GuiEventListener focused;
    private boolean isDragging;
    *///?}

    public ConditionConfigsListWidget(int x, int y, int width, int height, Component message, Layout layout) {
        super(x, y, width, height, message/*? if >=26.1 {*//*, defaultSettings(9)*//*?}*/);
        layout.setPosition(getX(), getY());
        layout.arrangeElements();
        this.contentHeight = layout.getHeight();
        this.layout = layout;
        layout.visitWidgets(this.children::add);
    }

    //? if 1.21.1 {
    /*@Override
    protected void renderContents(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderWidget(guiGraphics, mouseX, mouseY, partialTick);
    }
    *///?}

    //~ gui_rendering
    @Override
    public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.enableScissor(getX(), getY(), getRight(), getBottom());

        guiGraphics.fill(
                getX(), getY(), getRight(), getBottom(),
                ARGB.black(0.5f)
        );
        for (AbstractWidget child : children) {
            if (isOverScrollbar(mouseX, mouseY) || !isMouseOver(mouseX, mouseY)) {
                child.render(guiGraphics, -1, -1, partialTick);
            } else {
                child.render(guiGraphics, mouseX, mouseY, partialTick);
            }
        }

        //? if 1.21.1 {
        /*renderDecorations(guiGraphics);
        *///?} else {
        //~ if >1.21.4 'guiGraphics' -> 'guiGraphics, mouseX, mouseY'
        //renderScrollbar(guiGraphics, mouseX, mouseY);
        //?}

        guiGraphics.disableScissor();
    }
    //~ !gui_rendering

    //~ widget_events
    @Override
    public boolean mouseClicked(@NotNull MouseButtonEvent mouseEvent, boolean isDoubleClick) {
        //? if >1.21.1 {
        if (isOverScrollbar(mouseEvent.x(), mouseEvent.y())) {
            //~ if >1.21.4 'mouseX, mouseY, button' -> 'mouseEvent'
            return updateScrolling(mouseEvent);
        }
        return super.mouseClicked(mouseEvent, isDoubleClick);
        //?} else {
        /*for (AbstractWidget child : children) {
            if (child.isHovered() && child.mouseClicked(mouseEvent, isDoubleClick)) {
                return true;
            }
        }

        if (!this.visible) {
            return false;
        } else {
            boolean bl = this.withinContentAreaPoint(mouseEvent.x(), mouseEvent.y());
            boolean bl2 = this.scrollbarVisible() && isOverScrollbar(mouseEvent.x(), mouseEvent.y());
            if (bl2 && mouseEvent.button() == 0) {
                this.scrolling = true;
                return true;
            } else {
                return bl || bl2;
            }
        }
        *///?}
    }

    //? if 1.21.1 {
    /*@Override
    public boolean isDragging() {
        return isDragging;
    }

    @Override
    public void setDragging(boolean isDragging) {
        this.isDragging = isDragging;
    }

    @Override
    public @Nullable GuiEventListener getFocused() {
        return focused;
    }

    @Override
    public void setFocused(@Nullable GuiEventListener focused) {
        this.focused = focused;
    }
    *///?}
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

    @Override //~ if 1.21.1 'contentHeight' -> 'getInnerHeight'
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
        //? 1.21.1
        //return this.scrollbarVisible() && mouseX >= (double) (this.getX() + this.width - 6) && mouseX <= (double) (this.getX() + this.width) && mouseY >= (double) this.getY() && mouseY < (double) (this.getY() + this.height);
        //? >1.21.1
        return mouseX >= (double)this.scrollBarX() && mouseX <= (double)(this.scrollBarX() + 6) && mouseY >= (double)this.getY() && mouseY < (double)this.getBottom();
    }
    *///?}

    //? if 1.21.1 {
    /*@Override
    protected void renderScrollBar(GuiGraphics guiGraphics) {
        int scrollerHeight = this.getScrollBarHeight();
        int scrollBarX = this.getX() + this.width - 6;
        int scrollBarY = Math.max(this.getY(), (int) this.scrollAmount * (this.height - scrollerHeight) / this.getMaxScrollAmount() + this.getY());
        RenderSystem.enableBlend();
        guiGraphics.blitSprite(SCROLLER_SPRITE, scrollBarX, scrollBarY, 6, scrollerHeight);
        RenderSystem.disableBlend();
    }
    *///?}
}
