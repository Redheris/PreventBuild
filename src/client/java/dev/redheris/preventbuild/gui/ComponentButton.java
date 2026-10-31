package dev.redheris.preventbuild.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.Style;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class ComponentButton extends Button {
    private final Font font;
    private final Component message;
    private final Component underlinedMessage;
    private boolean renderBackground = true;
    private boolean underlineOnHover = false;

    public ComponentButton(int x, int y, int width, int height, Component message, OnPress onPress, Font font) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
        this.font = font;
        this.message = message;
        //~ if >1.21.4 'message.copy()' -> 'message'
        this.underlinedMessage = ComponentUtils.mergeStyles(message, Style.EMPTY.withUnderlined(true));
    }

    public void setRenderBackground(boolean renderBackground) {
        this.renderBackground = renderBackground;
    }

    public void setUnderlineOnHover(boolean underlineOnHover) {
        this.underlineOnHover = underlineOnHover;
    }

    //~ render_button_contents
    @Override
    public void renderContents(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (renderBackground) {
            float alpha = this.isHoveredOrFocused() ? 0.9f : 0.65f;
            //? if <=1.21.4
            //int alphaInt = (int) (alpha * 255);

            //~ if > 1.21.4 'ARGB.color(alphaInt, 0)' -> 'ARGB.black(alpha)'
            guiGraphics.fill(getX(), getY(), getRight(), getBottom(), ARGB.black(alpha));
        }
        Component component = underlineOnHover && this.isHoveredOrFocused() ? this.underlinedMessage : this.message;

        //~ if >=26.1 'drawString' -> 'text'
        guiGraphics.drawString(
                this.font, component,
                this.getX() + ((width - font.width(component)) / 2) + 1,
                this.getY() + ((height - 9) / 2) + 1,
                16777215 | Mth.ceil(this.alpha * 255.0F) << 24
        );
    }
}
