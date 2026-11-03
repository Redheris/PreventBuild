package dev.redheris.preventbuild.event;

import dev.redheris.preventbuild.PreventBuildClient;
import dev.redheris.preventbuild.util.PlayerMessages;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

public class JoinWorldEvent {
    public static void init() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (client.level == null || !client.level.isClientSide()) return;

            var conditionsManager = PreventBuildClient.conditionsManager;
            var lastFailedFiles = conditionsManager.getLastFailedFiles();
            if (!lastFailedFiles.isEmpty()) {
                PlayerMessages.displayFailedFilesMessage(client.player, lastFailedFiles);
            }
        });
    }
}
