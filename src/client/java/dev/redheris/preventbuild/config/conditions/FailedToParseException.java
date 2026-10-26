package dev.redheris.preventbuild.config.conditions;

public class FailedToParseException extends RuntimeException {
    public FailedToParseException(String message, Throwable cause) {
        super(message + ": " + cause.getMessage(), cause);
    }
}
