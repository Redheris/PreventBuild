package dev.redheris.preventbuild.util;

import dev.redheris.preventbuild.api.ConditionRegistry;
import dev.redheris.preventbuild.api.UnknownIdentifierValueException;
import dev.redheris.preventbuild.conditions.Condition;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static dev.redheris.preventbuild.util.ConditionValueValidator.*;

public class ConditionValueExtractor {
    private final static ConditionRegistry registry = ConditionRegistry.getInstance();

    public static int[] extractIntArray(Object value) {
        List<?> list = requireList(value);
        int[] ints = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            ints[i] = requireInt(list.get(i));
        }
        return ints;
    }

    public static List<Identifier> extractIdentifierList(String registryName, Registry<?> registry, Object value) {
        List<?> list = requireList(value);
        List<Identifier> identifiers = new ArrayList<>();
        for (Object obj : list) {
            Identifier id = extractIdentifier(registryName, registry, obj);
            identifiers.add(id);
        }
        return identifiers;
    }

    public static Identifier extractIdentifier(String registryName, Registry<?> registry, Object value) {
        String idValue = requireString(value);
        Identifier id = Identifier.parse(idValue);
        if (!registry.containsKey(id)) {
            throw new UnknownIdentifierValueException(registryName, idValue);
        }
        return id;
    }

    public static List<Condition> extractConditionList(Object value) {
        List<?> list = requireList(value);
        List<Condition> conditions = new ArrayList<>();
        for (Object obj : list) {
            var entry = extractEntry(obj);
            conditions.add(registry.parseCondition(entry.getKey(), entry.getValue()));
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
