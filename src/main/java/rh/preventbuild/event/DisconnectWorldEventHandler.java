package rh.preventbuild.event;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import rh.preventbuild.PreventBuildConfig;
import rh.preventbuild.api.ConditionRestrictions;

public class DisconnectWorldEventHandler {
    public static void register() {
        ClientPlayConnectionEvents.DISCONNECT.register((network, client) -> {
            ConditionRestrictions.resetRestrictions();
            PreventBuildConfig.loadConditionConfigs();
        });
    }
}
