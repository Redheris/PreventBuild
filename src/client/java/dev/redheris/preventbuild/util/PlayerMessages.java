package dev.redheris.preventbuild.util;

import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.CommonColors;
import net.minecraft.world.entity.player.Player;

import java.nio.file.Path;
import java.util.Map;

import static dev.redheris.preventbuild.util.CompatUtils.clickEvent;
import static dev.redheris.preventbuild.util.CompatUtils.textHoverEvent;

public class PlayerMessages {
    public static void displayFailedFilesMessage(Player player, Map<Path, String> failedFiles) {
        var msg = Component.literal("[PreventBuild] ")
                .append(Component.translatable(
                        "preventbuild.failed_to_parse_configs", failedFiles.size()
                ));
        for (Map.Entry<Path, String> entry : failedFiles.entrySet()) {
            msg.append("\n");
            Path filepath = entry.getKey();
            String filename = filepath.getFileName().toString();
            String description = entry.getValue();
            msg.append(Component.literal(filename)
                    .withStyle(style -> style
                            .withColor(CommonColors.LIGHT_GRAY)
                            .withUnderlined(true)
                            .withClickEvent(clickEvent(ClickEvent.Action.OPEN_FILE, filepath.toString()))
                            .withHoverEvent(textHoverEvent(Component.translatable("preventbuild.open_file")))
                    ));
            msg.append(" : ");
            msg.append(Component.literal(description).withColor(CommonColors.SOFT_RED));
        }
        CompatUtils.sendMessage(player, msg, false);
    }
}
