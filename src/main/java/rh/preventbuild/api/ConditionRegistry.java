package rh.preventbuild.api;

import rh.preventbuild.conditions.categories.ConditionCategory;
import rh.preventbuild.conditions.ICondition;

import java.util.HashMap;
import java.util.Map;

import static rh.preventbuild.api.ConditionRestrictions.isAllowedCondition;

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
        ICondition condition = factory.parse(category, value);
        if (isAllowedCondition(condition, value)) return condition;
        throw new IllegalArgumentException("Restricted condition: " + key + value);
    }
}
