package rh.preventbuild.client;


import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rh.preventbuild.PreventBuildConfig;
import rh.preventbuild.api.Conditions;
import rh.preventbuild.conditions.categories.CategoriesRegister;
import rh.preventbuild.event.PlayConnectionEventsHandler;
import rh.preventbuild.packet.RestrictionMessageHandler;

@Environment(EnvType.CLIENT)
public class PreventBuildClient implements ClientModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("PBConditionConfig");

    @Override
    public void onInitializeClient() {
        Conditions.register();
        PreventBuildConfig.loadConfigs();

        RestrictionMessageHandler.register();
        PlayConnectionEventsHandler.register();

        ClientCommands.registerAll();
        CategoriesRegister.registerAll();
    }
}