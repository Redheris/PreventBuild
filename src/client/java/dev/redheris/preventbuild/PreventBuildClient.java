package dev.redheris.preventbuild;

import dev.redheris.preventbuild.conditions.ConditionsManager;
import dev.redheris.preventbuild.config.PreventBuildConfig;
import net.fabricmc.api.ClientModInitializer;

public class PreventBuildClient implements ClientModInitializer {
    public static final ConditionsManager conditionsManager = new ConditionsManager();
    public static final PreventBuildConfig commonConfig = new PreventBuildConfig();

    @Override
    public void onInitializeClient() {
        commonConfig.read();
        conditionsManager.read();
    }
}
