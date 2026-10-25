package dev.redheris.preventbuild.conditions.impl.player;

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

public class InteractionHandCondition extends ConditionBase {
    private final InteractionHand hand;

    public InteractionHandCondition(InteractionHand hand) {
        this.hand = hand;
    }

    @Override
    public ConditionResult checkInteractAny(Player player, Level level, InteractionHand hand, @Nullable HitResult hitResult) {
        return this.hand.equals(hand) ? ConditionResult.CANCEL : ConditionResult.PASS;
    }

    @Override
    public ConditionResult checkInteractBlock(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        return checkInteractAny(player, level, hand, hitResult);
    }

    @Override
    public ConditionResult checkInteractEntity(Player player, Level level, InteractionHand hand, EntityHitResult hitResult) {
        return checkInteractAny(player, level, hand, hitResult);
    }

    @Override
    public ConditionResult checkUseItem(Player player, Level level, InteractionHand hand, @Nullable HitResult hitResult) {
        return checkInteractAny(player, level, hand, hitResult);
    }

    @Override
    public ConditionResult checkPlaceBlock(BlockPlaceContext placeContext) {
        return this.hand.equals(placeContext.getHand()) ? ConditionResult.CANCEL : ConditionResult.PASS;
    }
}
