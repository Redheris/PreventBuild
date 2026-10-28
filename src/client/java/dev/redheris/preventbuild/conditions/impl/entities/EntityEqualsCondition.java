package dev.redheris.preventbuild.conditions.impl.entities;

import dev.redheris.preventbuild.conditions.ConditionResult;
import dev.redheris.preventbuild.conditions.util.IdentifierTagWrapper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.List;

public class EntityEqualsCondition extends EntityConditionBase {
    private final List<IdentifierTagWrapper<EntityType<?>>> entityIds;

    public EntityEqualsCondition(List<IdentifierTagWrapper<EntityType<?>>> entityIds) {
        this.entityIds = entityIds;
    }

    @Override
    protected ConditionResult checkEntity(Entity entity) {
        EntityType<?> entityType = entity.getType();
        Identifier entityId = BuiltInRegistries.ENTITY_TYPE.getKey(entityType);
        for (IdentifierTagWrapper<EntityType<?>> wrapper : entityIds) {
            if (wrapper.isIn(entityType, entityId)) {
                return ConditionResult.CANCEL;
            }
        }
        return ConditionResult.PASS;
    }
}
