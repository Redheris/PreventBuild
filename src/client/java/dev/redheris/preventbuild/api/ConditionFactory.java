package dev.redheris.preventbuild.api;

import dev.redheris.preventbuild.conditions.Condition;

@FunctionalInterface
public interface ConditionFactory {
    /**
     * Creates {@link Condition} from given raw value if the key is registered
     *
     * @param value condition's raw value.
     * @return a {@link Condition} created with the specified condition's value
     * @throws IllegalConditionValueException if the value's type/structure/content is unexpected
     */
    Condition parse(Object value) throws IllegalConditionValueException;
}
