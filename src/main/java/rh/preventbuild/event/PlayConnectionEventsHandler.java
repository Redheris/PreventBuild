package rh.preventbuild.event;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.text.Text;
import rh.preventbuild.PreventBuildConfig;
import rh.preventbuild.api.ConditionRestrictions;

public class PlayConnectionEventsHandler {
    public static void register() {
        ClientPlayConnectionEvents.DISCONNECT.register((network, client) -> {
            ConditionRestrictions.resetRestrictions();
            PreventBuildConfig.loadConfigs();
        });
        ClientPlayConnectionEvents.JOIN.register((network, sender, client) -> {
            Text exceptionMessage = PreventBuildConfig.getExceptionMessage();
            if (exceptionMessage != null && client.player != null) {
                client.player.sendMessage(exceptionMessage, false);
            }
        });
    }
}
