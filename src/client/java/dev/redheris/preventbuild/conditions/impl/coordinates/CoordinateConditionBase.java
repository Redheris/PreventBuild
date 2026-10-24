package dev.redheris.preventbuild.conditions.impl.coordinates;

import dev.redheris.preventbuild.conditions.ConditionBase;
import dev.redheris.preventbuild.conditions.ConditionResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

public abstract class CoordinateConditionBase extends ConditionBase {
    protected final Direction.Axis axis;

    public CoordinateConditionBase(Direction.Axis axis) {
        this.axis = axis;
    }

    protected abstract ConditionResult checkCoordinate(int value);

    private ConditionResult checkHitCoordinate(HitResult hitResult) {
        int value = (int) Math.round(hitResult.getLocation().get(axis));
        return checkCoordinate(value);
    }

    private ConditionResult checkBlockCoordinate(BlockPos blockPos) {
        int value = blockPos.get(axis);
        return checkCoordinate(value);
    }

    private ConditionResult checkEntityCoordinate(Entity entity) {
        int value = entity.blockPosition().get(axis);
        return checkCoordinate(value);
    }

    @Override
    public ConditionResult checkAttackAny(Player player, Level level, HitResult hitResult) {
        return checkHitCoordinate(hitResult);
    }

    @Override
    public ConditionResult checkAttackBlock(Player player, Level level, BlockHitResult hitResult) {
        return checkBlockCoordinate(hitResult.getBlockPos());
    }

    @Override
    public ConditionResult checkAttackEntity(Player player, Level level, EntityHitResult hitResult) {
        return checkEntityCoordinate(hitResult.getEntity());
    }

    @Override
    public ConditionResult checkInteractAny(Player player, Level level, InteractionHand hand, @Nullable HitResult hitResult) {
        if (hitResult == null) return ConditionResult.PASS;
        return checkHitCoordinate(hitResult);
    }

    @Override
    public ConditionResult checkInteractBlock(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        return checkBlockCoordinate(hitResult.getBlockPos());
    }

    @Override
    public ConditionResult checkInteractEntity(Player player, Level level, InteractionHand hand, EntityHitResult hitResult) {
        return checkEntityCoordinate(hitResult.getEntity());
    }

    @Override
    public ConditionResult checkUseItem(Player player, Level level, InteractionHand hand, @Nullable HitResult hitResult) {
        return checkEntityCoordinate(player);
    }

    @Override
    public ConditionResult checkPlaceBlock(BlockPlaceContext placeContext) {
        return checkBlockCoordinate(placeContext.getClickedPos());
    }
}
