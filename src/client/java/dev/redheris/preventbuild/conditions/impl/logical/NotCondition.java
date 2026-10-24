package dev.redheris.preventbuild.conditions.impl.logical;

import dev.redheris.preventbuild.conditions.Condition;
import dev.redheris.preventbuild.conditions.ConditionBase;
import dev.redheris.preventbuild.conditions.ConditionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

public class NotCondition extends ConditionBase {
    private final Condition nestedCondition;

    public NotCondition(Condition nestedCondition) {
        this.nestedCondition = nestedCondition;
    }

    @Override
    public ConditionResult checkAttackAny(Player player, Level level, HitResult hitResult) {
        return nestedCondition.checkAttackAny(player, level, hitResult).opposite();
    }

    @Override
    public ConditionResult checkAttackBlock(Player player, Level level, BlockHitResult hitResult) {
        return nestedCondition.checkAttackBlock(player, level, hitResult).opposite();
    }

    @Override
    public ConditionResult checkAttackEntity(Player player, Level level, EntityHitResult hitResult) {
        return nestedCondition.checkAttackEntity(player, level, hitResult).opposite();
    }

    @Override
    public ConditionResult checkInteractAny(Player player, Level level, InteractionHand hand, @Nullable HitResult hitResult) {
        return nestedCondition.checkInteractAny(player, level, hand, hitResult).opposite();
    }

    @Override
    public ConditionResult checkInteractBlock(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        return nestedCondition.checkInteractBlock(player, level, hand, hitResult).opposite();
    }

    @Override
    public ConditionResult checkInteractEntity(Player player, Level level, InteractionHand hand, EntityHitResult hitResult) {
        return nestedCondition.checkInteractEntity(player, level, hand, hitResult).opposite();
    }

    @Override
    public ConditionResult checkUseItem(Player player, Level level, InteractionHand hand, @Nullable HitResult hitResult) {
        return nestedCondition.checkUseItem(player, level, hand, hitResult).opposite();
    }

    @Override
    public ConditionResult checkPlaceBlock(BlockPlaceContext placeContext) {
        return nestedCondition.checkPlaceBlock(placeContext).opposite();
    }
}
