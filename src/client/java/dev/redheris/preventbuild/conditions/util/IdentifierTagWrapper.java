package dev.redheris.preventbuild.conditions.util;

import dev.redheris.preventbuild.config.customtags.CustomTagsManager;
import dev.redheris.preventbuild.config.customtags.UnknownCustomTagException;
import net.fabricmc.fabric.api.tag.convention.v2.TagUtil;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;

public class IdentifierTagWrapper<T> {
    private final Identifier identifier;
    private final TagKey<T> tagKey;
    private final String customTagKey;

    public IdentifierTagWrapper(Identifier identifier) {
        this.identifier = identifier;
        this.tagKey = null;
        this.customTagKey = null;
    }

    public IdentifierTagWrapper(TagKey<T> tagKey) {
        this.identifier = null;
        this.tagKey = tagKey;
        this.customTagKey = null;
    }

    public IdentifierTagWrapper(String customTagKey) {
        this.identifier = null;
        this.tagKey = null;
        this.customTagKey = customTagKey;

        if (!CustomTagsManager.containsTag(customTagKey)) {
            throw new UnknownCustomTagException(customTagKey);
        }
    }

    public boolean isIn(T obj, Identifier objId) {
        return isIn(obj, objId, null);
    }

    public boolean isIn(T obj, Identifier objId, RegistryAccess registryAccess) {
        if (tagKey != null) {
            return TagUtil.isIn(registryAccess, tagKey, obj);
        }

        if (identifier != null) {
            return identifier.equals(objId);
        }
        return CustomTagsManager.isIn(customTagKey, objId);
    }
}
