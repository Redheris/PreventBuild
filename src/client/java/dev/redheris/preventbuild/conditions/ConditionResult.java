package dev.redheris.preventbuild.conditions;

public enum ConditionResult {
    /// Condition is met. The action should be canceled
    CANCEL,
    /// Condition is not met. The action should be passed normally
    PASS;

    public ConditionResult opposite() {
        return this == CANCEL ? PASS : CANCEL;
    }
}
