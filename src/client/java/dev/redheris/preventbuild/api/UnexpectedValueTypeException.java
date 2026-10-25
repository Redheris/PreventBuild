package dev.redheris.preventbuild.api;

public class UnexpectedValueTypeException extends IllegalConditionValueException {
    public UnexpectedValueTypeException(String message) {
        super(message);
    }
}
