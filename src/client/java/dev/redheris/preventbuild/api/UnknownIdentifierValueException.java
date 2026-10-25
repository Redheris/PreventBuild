package dev.redheris.preventbuild.api;

public class UnknownIdentifierValueException extends IllegalConditionValueException {
    public UnknownIdentifierValueException(String registryName, String identifierValue) {
        super("Unknown " + registryName + " identifier: " + identifierValue);
    }
}
