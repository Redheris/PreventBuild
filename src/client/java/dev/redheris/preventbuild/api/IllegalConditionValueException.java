package dev.redheris.preventbuild.api;

public class IllegalConditionValueException extends RuntimeException {
    public IllegalConditionValueException(String message) {
        super(message);
    }

    public IllegalConditionValueException(String message, Throwable cause) {
        super(message, cause);
    }
}
