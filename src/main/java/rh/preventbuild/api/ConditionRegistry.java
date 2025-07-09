package rh.preventbuild.api;

import net.minecraft.text.Text;
import rh.preventbuild.conditions.ConditionCategory;
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
        String message = Text.translatable("preventbuild.unknown_keyword", key).getString();
        if (factory == null) throw new IllegalArgumentException(message);

        ICondition condition = factory.parse(category, value);
        if (isAllowedCondition(condition, value)) return condition;

        message = Text.translatable("preventbuild.restricted_condition", key + value).getString();
        throw new IllegalArgumentException(message);
    }
}
