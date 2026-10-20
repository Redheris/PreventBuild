package dev.redheris.preventbuild.config.conditions;

import java.util.Map;

public class ConditionConfigValidator {
    public static Map<?, ?> requireMap(Object obj) {
        if (obj instanceof Map<?, ?> map) {
            return map;
        }
        throw new IllegalArgumentException();
    }

    public static String requireString(Object obj) {
        if (obj instanceof String string) {
            return string;
        }
        throw new IllegalArgumentException();
    }
}
