package rh.preventbuild.client;


import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import rh.preventbuild.PreventBuildConfig;
import rh.preventbuild.api.Conditions;
import rh.preventbuild.conditions.categories.CategoriesRegister;
import rh.preventbuild.event.DisconnectWorldEventHandler;

@Environment(EnvType.CLIENT)
public class PreventBuildClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        PreventBuildConfig.loadOreDictionary();
        Conditions.register();
        PreventBuildConfig.loadConditionConfigs();

        DisconnectWorldEventHandler.register();
        ClientCommands.registerAll();
        CategoriesRegister.registerAll();
    }
}