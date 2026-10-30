package dev.redheris.preventbuild.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import com.terraformersmc.modmenu.util.NullScreenFactory;
import dev.redheris.preventbuild.yacl.YACLScreenFactory;
import net.fabricmc.loader.api.FabricLoader;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        FabricLoader fabricLoader = FabricLoader.getInstance();

        if (fabricLoader.isModLoaded("yet_another_config_lib_v3")) {
            return YACLScreenFactory::getConfigScreen;
        }

        return new NullScreenFactory<>();
    }
}
