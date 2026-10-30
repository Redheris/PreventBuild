package dev.redheris.preventbuild;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PreventBuild implements ModInitializer {
    public static final String MOD_ID = /*$ mod_id*/ "preventbuild";
    public static final String MOD_NAME = /*$ mod_name*/ "PreventBuild";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    @Override
    public void onInitialize() {

    }

    public Identifier identifier(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
