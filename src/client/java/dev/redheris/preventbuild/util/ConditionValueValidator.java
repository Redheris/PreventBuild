package dev.redheris.preventbuild.util;

import dev.redheris.preventbuild.api.UnexpectedValueTypeException;

import java.util.List;
import java.util.Map;

public class ConditionValueValidator {
    public static int requireInt(Object obj) throws UnexpectedValueTypeException {
        try {
            return (int) obj;
        } catch (ClassCastException e) {
            throw new UnexpectedValueTypeException("Expected int, but got " + obj.getClass());
        }
    }

    public static boolean requireBoolean(Object obj) throws UnexpectedValueTypeException {
        try {
            return (boolean) obj;
        } catch (ClassCastException e) {
            throw new UnexpectedValueTypeException("Expected boolean, but got " + obj.getClass());
        }
    }

    public static String requireString(Object obj) throws UnexpectedValueTypeException {
        if (obj instanceof String string) {
            return string;
        }
        throw new UnexpectedValueTypeException("Expected String, but got " + obj.getClass());
    }

    public static Map<?, ?> requireMap(Object obj) throws UnexpectedValueTypeException {
        if (obj instanceof Map<?, ?> map) {
            return map;
        }
        throw new UnexpectedValueTypeException("Expected Map, but got " + obj.getClass());
    }

    public static List<?> requireList(Object obj) throws UnexpectedValueTypeException {
        if (obj instanceof List<?> list) {
            return list;
        }
        throw new UnexpectedValueTypeException("Expected List, but got " + obj.getClass());
    }
}
