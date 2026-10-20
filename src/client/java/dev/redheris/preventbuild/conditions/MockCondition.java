package dev.redheris.preventbuild.conditions;

public class MockCondition implements Condition {
    private final Object content;

    public MockCondition(Object content) {
        this.content = content;
    }
}
