package dev.redheris.preventbuild.conditions.impl.blocks;

import dev.redheris.preventbuild.conditions.ConditionBase;
import dev.redheris.preventbuild.conditions.ConditionResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

public abstract class BlockConditionBase extends ConditionBase {

    protected abstract ConditionResult checkBlock(Level level, BlockPos blockPos);

    @Override
    public ConditionResult checkAttackAny(Player player, Level level, HitResult hitResult) {
        if (hitResult instanceof BlockHitResult blockHitResult) {
            return checkBlock(level, blockHitResult.getBlockPos());
        }
        return ConditionResult.PASS;
    }

    @Override
    public ConditionResult checkAttackBlock(Player player, Level level, BlockHitResult hitResult) {
        return checkBlock(level, hitResult.getBlockPos());
    }

    @Override
    public ConditionResult checkInteractAny(Player player, Level level, InteractionHand hand, @Nullable HitResult hitResult) {
        if (hitResult instanceof BlockHitResult blockHitResult) {
            return checkBlock(level, blockHitResult.getBlockPos());
        }
        return ConditionResult.PASS;
    }

    @Override
    public ConditionResult checkInteractBlock(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        return checkBlock(level, hitResult.getBlockPos());
    }

    @Override
    public ConditionResult checkUseItem(Player player, Level level, InteractionHand hand, @Nullable HitResult hitResult) {
        return checkInteractAny(player, level, hand, hitResult);
    }
}
