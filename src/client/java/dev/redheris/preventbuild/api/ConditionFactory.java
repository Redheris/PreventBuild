package dev.redheris.preventbuild.api;

import dev.redheris.preventbuild.conditions.Condition;
import net.minecraft.core.RegistryAccess;

@FunctionalInterface
public interface ConditionFactory {
    /**
     * Creates {@link Condition} from given raw value if the key is registered
     *
     * @param regs  a {@link RegistryAccess} used to get a {@link net.minecraft.core.Registry Registry} used in the condition
     * @param value condition's raw value.
     * @return a {@link Condition} created with the specified condition's value
     * @throws IllegalConditionValueException if the value's type/structure/content is unexpected
     */
    Condition parse(RegistryAccess regs, Object value) throws IllegalConditionValueException;
}
