package dev.redheris.preventbuild.api;

import dev.redheris.preventbuild.conditions.Condition;
import net.minecraft.core.RegistryAccess;

import java.util.HashMap;
import java.util.Map;

public class ConditionRegistry {
    private final static ConditionRegistry INSTANCE = new ConditionRegistry();
    private final Map<String, ConditionFactory> registry = new HashMap<>();

    public static ConditionRegistry getInstance() {
        return INSTANCE;
    }

    /**
     * Registers new ConditionFactory if the key is unique
     *
     * @param key     factory's key used in parsing
     * @param factory condition's factory creating a condition object with provided value object
     * @return {@code false} if the provided key is already registered. {@code true} if the factory is registered
     */
    public boolean register(String key, ConditionFactory factory) {
        if (registry.containsKey(key)) return false;
        registry.put(key, factory);
        return true;
    }

    /**
     * @param key registered factory's key
     * @return the {@link ConditionFactory} object to which the specified key is mapped, or {@code null} if the key is unregistered
     */
    public ConditionFactory getFactory(String key) {
        return registry.get(key);
    }

    /**
     * Creates {@link Condition} from given key and raw value if the key is registered
     *
     * @param registries a {@link RegistryAccess} used in {@link ConditionFactory#parse ConditionFactory#parse}
     * @param key        registered factory's key
     * @param value      given condition's value object
     * @return a {@link Condition} created with a registered factory
     * @throws UnknownConditionFactoryException if the key is unregistered
     * @throws IllegalConditionValueException   if the value's type/structure/content is unexpected
     */
    public Condition parseCondition(RegistryAccess registries, String key, Object value) throws UnknownConditionFactoryException, IllegalConditionValueException {
        ConditionFactory factory = getFactory(key);
        if (factory == null) {
            throw new UnknownConditionFactoryException(key);
        }

        return factory.parse(registries, value);
    }
}
