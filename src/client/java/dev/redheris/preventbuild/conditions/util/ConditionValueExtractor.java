package dev.redheris.preventbuild.conditions.util;

import dev.redheris.preventbuild.api.ConditionRegistry;
import dev.redheris.preventbuild.api.IllegalConditionValueException;
import dev.redheris.preventbuild.api.UnknownConditionFactoryException;
import dev.redheris.preventbuild.api.UnknownIdentifierValueException;
import dev.redheris.preventbuild.conditions.Condition;
import dev.redheris.preventbuild.config.conditions.FailedToParseException;
import dev.redheris.preventbuild.config.customtags.CustomTagsManager;
import dev.redheris.preventbuild.config.customtags.UnknownCustomTagException;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static dev.redheris.preventbuild.conditions.util.ConditionValueValidator.*;

public class ConditionValueExtractor {
    private final static ConditionRegistry registry = ConditionRegistry.getInstance();

    public static int[] extractIntArray(Object value) {
        if (value instanceof Integer integer) {
            return new int[]{integer};
        }
        List<?> list = requireList(value);
        int[] ints = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            ints[i] = requireInt(list.get(i));
        }
        return ints;
    }

    public static <T> List<IdentifierTagWrapper<T>> extractIdentifierTagList(String registryName, Registry<T> registry, Object value) {
        List<String> values = extractStringList(value);

        return values.stream()
                .map(s -> extractIdentifierTag(registryName, registry, s))
                .toList();
    }

    public static List<String> extractStringList(Object value) {
        if (!(value instanceof List)) {
            return List.of(value.toString());
        }
        List<?> list = requireList(value);
        return list.stream()
                .map(Object::toString)
                .toList();
    }

    public static <T> IdentifierTagWrapper<T> extractIdentifierTag(String registryName, Registry<T> registry, String value) {
        if (value.startsWith("##")) {
            String customTagKey = value.substring(2);
            if (!CustomTagsManager.containsTag(customTagKey)) {
                throw new UnknownCustomTagException(customTagKey);
            }
            return new IdentifierTagWrapper<>(customTagKey);
        }

        if (value.startsWith("#")) {
            TagKey<T> tagKey = TagKey.create(
                    registry.key(), Identifier.parse(value.substring(1))
            );
            return new IdentifierTagWrapper<>(tagKey);
        }

        return new IdentifierTagWrapper<>(extractIdentifier(registryName, registry, value));
    }

    public static Identifier extractIdentifier(String registryName, Registry<?> registry, String idValue) {
        Identifier id = Identifier.parse(idValue);
        if (!registry.containsKey(id)) {
            throw new UnknownIdentifierValueException(registryName, idValue);
        }
        return id;
    }

    public static List<Condition> extractConditionList(RegistryAccess registries, Object value) {
        List<?> list = requireList(value);
        List<Condition> conditions = new ArrayList<>();

        for (Object obj : list) {
            var entry = extractEntry(obj);
            Condition condition;
            try {
                condition = registry.parseCondition(registries, entry.getKey(), entry.getValue());
            } catch (UnknownConditionFactoryException | IllegalConditionValueException e) {
                throw new FailedToParseException("Failed to parse \"" + entry.getKey() + "\" condition value", e);
            }
            conditions.add(condition);
        }

        return conditions;
    }

    public static Map.Entry<String, Object> extractEntry(Object value) {
        var content = requireMap(value);
        var entry = content.entrySet().iterator().next();
        String key = requireString(entry.getKey());
        return Map.entry(key, entry.getValue());
    }
}
