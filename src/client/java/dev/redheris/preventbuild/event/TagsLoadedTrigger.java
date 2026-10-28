package dev.redheris.preventbuild.event;

import dev.redheris.preventbuild.PreventBuildClient;
import dev.redheris.preventbuild.config.customtags.CustomTagsManager;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;

public class TagsLoadedTrigger {
    private static boolean tagsLoaded = false;

    public static void init() {
        CommonLifecycleEvents.TAGS_LOADED.register((registries, client) -> {
            if (tagsLoaded) return;
            CustomTagsManager.read();
            // FIXME: fix conditions failed to create without Level/RegistryAccess
            PreventBuildClient.conditionsManager.read();
            tagsLoaded = true;
        });
    }

    public static boolean isTagsLoaded() {
        return tagsLoaded;
    }
}
