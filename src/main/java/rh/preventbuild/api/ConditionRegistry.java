package rh.preventbuild.api;

import rh.preventbuild.conditions.ConditionCategory;
import rh.preventbuild.conditions.ICondition;

import java.util.HashMap;
import java.util.Map;

public class ConditionRegistry {

    private static final Map<String, ConditionFactory> registry = new HashMap<>();

    public static void register(String key, ConditionFactory factory) {
        registry.put(key, factory);
    }

    public static ConditionFactory get(String key) {
        return registry.get(key);
    }

    public static ICondition parse(ConditionCategory category, String key, String value) {
        ConditionFactory factory = get(key);
        if (factory == null) throw new IllegalArgumentException("Unknown condition key: " + key);
        return factory.parse(category, value);
    }
}
