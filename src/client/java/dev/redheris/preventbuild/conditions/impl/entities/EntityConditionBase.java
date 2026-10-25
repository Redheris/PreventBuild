package dev.redheris.preventbuild.conditions.impl.entities;

import dev.redheris.preventbuild.conditions.ConditionBase;
import dev.redheris.preventbuild.conditions.ConditionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

public abstract class EntityConditionBase extends ConditionBase {

    protected abstract ConditionResult checkEntity(Entity entity);

    @Override
    public ConditionResult checkAttackAny(Player player, Level level, HitResult hitResult) {
        if (hitResult instanceof EntityHitResult entityHitResult) {
            return checkEntity(entityHitResult.getEntity());
        }
        return ConditionResult.PASS;
    }

    @Override
    public ConditionResult checkAttackEntity(Player player, Level level, EntityHitResult hitResult) {
        return checkEntity(hitResult.getEntity());
    }

    @Override
    public ConditionResult checkInteractAny(Player player, Level level, InteractionHand hand, @Nullable HitResult hitResult) {
        if (hitResult instanceof EntityHitResult entityHitResult) {
            return checkEntity(entityHitResult.getEntity());
        }
        return ConditionResult.PASS;
    }

    @Override
    public ConditionResult checkInteractEntity(Player player, Level level, InteractionHand hand, EntityHitResult hitResult) {
        return checkEntity(hitResult.getEntity());
    }

    @Override
    public ConditionResult checkUseItem(Player player, Level level, InteractionHand hand, @Nullable HitResult hitResult) {
        return checkInteractAny(player, level, hand, hitResult);
    }
}
