package dev.redheris.preventbuild.api;

public class ConditionValueFormatException extends IllegalConditionValueException {
    public ConditionValueFormatException(String message) {
        super("Invalid value format: " + message);
    }

    public ConditionValueFormatException(Throwable cause) {
        super("Invalid value format:", cause);
    }
}
