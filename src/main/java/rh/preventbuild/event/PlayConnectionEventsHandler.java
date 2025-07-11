package rh.preventbuild.event;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import rh.preventbuild.PreventBuildConfig;
import rh.preventbuild.api.ConditionRestrictions;

public class PlayConnectionEventsHandler {
    public static void register() {
        ClientPlayConnectionEvents.DISCONNECT.register((network, client) ->
            ConditionRestrictions.resetRestrictions()
        );
        ClientPlayConnectionEvents.JOIN.register((network, sender, client) ->
            PreventBuildConfig.loadConfigs()
        );
    }
}
