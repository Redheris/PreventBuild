package dev.redheris.preventbuild.util;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

//? if >=26.3
//import com.mojang.blaze3d.Blaze3D;
//? if <26.3
import net.minecraft.util.Util;

//? if >1.21.4
import java.net.URI;

public class CompatUtils {

    public static void sendMessage(@NotNull Player player, Component message, boolean overlay) {
        Minecraft.getInstance().execute(() -> {
            //? if >=26.1 {
            /*if (overlay)
                player.sendOverlayMessage(message);
            else
                player.sendSystemMessage(message);
            *///?} else
            player.displayClientMessage(message, overlay);
        });
    }

    public static ClickEvent clickEvent(ClickEvent.Action action, String value) {
        //? if >1.21.4 {
        return switch (action) {
            case OPEN_FILE -> new ClickEvent.OpenFile(value);
            case OPEN_URL -> new ClickEvent.OpenUrl(URI.create(value));
            case COPY_TO_CLIPBOARD -> new ClickEvent.CopyToClipboard(value);
            case null, default -> throw new UnsupportedOperationException();
        };
        //?} else
        //return new ClickEvent(action, value);
    }

    public static HoverEvent textHoverEvent(Component text) {
        //? if >1.21.4 {
        return new HoverEvent.ShowText(text);
         //?} else
        //return new HoverEvent(HoverEvent.Action.SHOW_TEXT, text);
    }

    public static void openPath(@NotNull Path path) {
        //~ if >=26.3 'Util.getPlatform()' -> 'Blaze3D'
        Util.getPlatform().openPath(path);
    }
}
