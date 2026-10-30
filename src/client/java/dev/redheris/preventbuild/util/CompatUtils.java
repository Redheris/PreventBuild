package dev.redheris.preventbuild.util;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

//? if >=26.3
//import com.mojang.blaze3d.Blaze3D;
//? if <26.3
import net.minecraft.util.Util;

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

    public static void openPath(@NotNull Path path) {
        //~ if >=26.3 'Util.getPlatform()' -> 'Blaze3D'
        Util.getPlatform().openPath(path);
    }
}
