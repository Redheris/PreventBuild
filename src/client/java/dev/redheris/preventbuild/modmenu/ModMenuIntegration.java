package dev.redheris.preventbuild.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.redheris.preventbuild.yacl.YACLScreenFactory;
import net.fabricmc.loader.api.FabricLoader;

//? if >1.21.4
import com.terraformersmc.modmenu.util.NullScreenFactory;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        FabricLoader fabricLoader = FabricLoader.getInstance();

        if (fabricLoader.isModLoaded("yet_another_config_lib_v3")) {
            return YACLScreenFactory::getConfigScreen;
        }

        //~ if >1.21.4 'null' -> 'new NullScreenFactory<>()'
        return new NullScreenFactory<>();
    }
}
