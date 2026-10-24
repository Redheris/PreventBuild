package dev.redheris.preventbuild.conditions;

public enum ConditionResult {
    /// Action should be canceled
    CANCEL,
    /// Action should not be canceled
    PASS;

    public ConditionResult opposite() {
        return this == CANCEL ? PASS : CANCEL;
    }
}
