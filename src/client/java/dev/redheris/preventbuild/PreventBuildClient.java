package dev.redheris.preventbuild;

import dev.redheris.preventbuild.config.conditions.ConditionsManager;
import net.fabricmc.api.ClientModInitializer;

public class PreventBuildClient implements ClientModInitializer {
    public static final ConditionsManager conditionsManager = new ConditionsManager();

    @Override
    public void onInitializeClient() {
        conditionsManager.read();
    }
}
