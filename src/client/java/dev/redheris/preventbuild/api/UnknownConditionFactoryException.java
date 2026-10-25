package dev.redheris.preventbuild.api;

public class UnknownConditionFactoryException extends RuntimeException {
    public UnknownConditionFactoryException(String key) {
        super("Unknown key: \"" + key + "\"");
    }
}
