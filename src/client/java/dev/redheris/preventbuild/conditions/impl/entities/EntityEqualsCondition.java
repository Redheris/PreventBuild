package dev.redheris.preventbuild.conditions.impl.entities;

import dev.redheris.preventbuild.conditions.ConditionBase;
import dev.redheris.preventbuild.conditions.ConditionResult;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

public class EntityEqualsCondition extends ConditionBase {
    // TODO: Make List
    private final Identifier entityId;

    public EntityEqualsCondition(Identifier entityId) {
        this.entityId = entityId;
    }

    private boolean entityEquals(Entity entity) {
        if (!BuiltInRegistries.ENTITY_TYPE.containsKey(entityId)) return false;
        return BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).equals(entityId);
    }

    @Override
    public ConditionResult checkAttackAny(Player player, Level level, HitResult hitResult) {
        if (hitResult instanceof EntityHitResult entityHitResult) {
            if (entityEquals(entityHitResult.getEntity())) {
                return ConditionResult.CANCEL;
            }
        }
        return ConditionResult.PASS;
    }

    @Override
    public ConditionResult checkAttackEntity(Player player, Level level, EntityHitResult hitResult) {
        return super.checkAttackEntity(player, level, hitResult);
    }

    @Override
    public ConditionResult checkInteractAny(Player player, Level level, InteractionHand hand, @Nullable HitResult hitResult) {
        if (hitResult instanceof EntityHitResult entityHitResult) {
            if (entityEquals(entityHitResult.getEntity())) {
                return ConditionResult.CANCEL;
            }
        }
        return ConditionResult.PASS;
    }

    @Override
    public ConditionResult checkInteractEntity(Player player, Level level, InteractionHand hand, EntityHitResult hitResult) {
        return super.checkInteractEntity(player, level, hand, hitResult);
    }

    @Override
    public ConditionResult checkUseItem(Player player, Level level, InteractionHand hand, @Nullable HitResult hitResult) {
        if (hitResult instanceof EntityHitResult entityHitResult) {
            if (entityEquals(entityHitResult.getEntity())) {
                return ConditionResult.CANCEL;
            }
        }
        return ConditionResult.PASS;
    }
}
