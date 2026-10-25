package dev.redheris.preventbuild.conditions.impl.entities;

import dev.redheris.preventbuild.conditions.ConditionResult;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;

import java.util.List;

public class EntityEqualsCondition extends EntityConditionBase {
    private final List<Identifier> entityIds;

    public EntityEqualsCondition(List<Identifier> entityIds) {
        this.entityIds = entityIds;
    }

    @Override
    protected ConditionResult checkEntity(Entity entity) {
        Identifier entityId = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
        return entityIds.contains(entityId) ? ConditionResult.CANCEL : ConditionResult.PASS;
    }
}
