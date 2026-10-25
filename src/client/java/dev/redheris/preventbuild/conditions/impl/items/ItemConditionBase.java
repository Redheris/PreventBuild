package dev.redheris.preventbuild.conditions.impl.items;

import dev.redheris.preventbuild.conditions.ConditionBase;
import dev.redheris.preventbuild.conditions.ConditionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

public abstract class ItemConditionBase extends ConditionBase {

    protected abstract ConditionResult checkItem(Item item);

    private ConditionResult checkItem(Player player, InteractionHand hand) {
        Item heldItem = player.getItemInHand(hand).getItem();
        return checkItem(heldItem);
    }

    @Override
    public ConditionResult checkAttackAny(Player player, Level level, HitResult hitResult) {
        return checkItem(player, InteractionHand.MAIN_HAND);
    }

    @Override
    public ConditionResult checkAttackBlock(Player player, Level level, BlockHitResult hitResult) {
        return checkItem(player, InteractionHand.MAIN_HAND);
    }

    @Override
    public ConditionResult checkAttackEntity(Player player, Level level, EntityHitResult hitResult) {
        return checkItem(player, InteractionHand.MAIN_HAND);
    }

    @Override
    public ConditionResult checkInteractAny(Player player, Level level, InteractionHand hand, @Nullable HitResult hitResult) {
        return checkItem(player, hand);
    }

    @Override
    public ConditionResult checkInteractBlock(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        return checkItem(player, hand);
    }

    @Override
    public ConditionResult checkInteractEntity(Player player, Level level, InteractionHand hand, EntityHitResult hitResult) {
        return checkItem(player, hand);
    }

    @Override
    public ConditionResult checkUseItem(Player player, Level level, InteractionHand hand, @Nullable HitResult hitResult) {
        return checkItem(player, hand);
    }

    @Override
    public ConditionResult checkPlaceBlock(BlockPlaceContext placeContext) {
        return checkItem(placeContext.getItemInHand().getItem());
    }
}
