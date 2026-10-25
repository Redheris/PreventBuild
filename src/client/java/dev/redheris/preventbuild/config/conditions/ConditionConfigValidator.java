package dev.redheris.preventbuild.config.conditions;

import dev.redheris.preventbuild.api.UnexpectedValueTypeException;

import java.util.Map;

public class ConditionConfigValidator {
    public static Map<?, ?> requireMap(Object obj) throws UnexpectedValueTypeException {
        if (obj instanceof Map<?, ?> map) {
            return map;
        }
        throw new UnexpectedValueTypeException("Expected Map, but got " + obj.getClass());
    }

    public static String requireString(Object obj) throws UnexpectedValueTypeException {
        if (obj instanceof String string) {
            return string;
        }
        throw new UnexpectedValueTypeException("Expected String, but got " + obj.getClass());
    }
}
