package dev.redheris.preventbuild.conditions.impl.items;

import dev.redheris.preventbuild.conditions.ConditionBase;
import dev.redheris.preventbuild.conditions.ConditionResult;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

public class ItemEqualsCondition extends ConditionBase {
    // TODO: Make List
    private final Identifier itemId;

    public ItemEqualsCondition(Identifier itemId) {
        this.itemId = itemId;
    }

    private ConditionResult itemEquals(Item heldItem) {
        if (!BuiltInRegistries.ITEM.containsKey(itemId)) return ConditionResult.PASS;
        if (BuiltInRegistries.ITEM.getKey(heldItem).equals(itemId)) {
            return ConditionResult.CANCEL;
        }
        return ConditionResult.PASS;
    }

    private ConditionResult itemEquals(Player player, InteractionHand hand) {
        Item heldItem = player.getItemInHand(hand).getItem();
        return itemEquals(heldItem);
    }

    @Override
    public ConditionResult checkAttackAny(Player player, Level level, HitResult hitResult) {
        return itemEquals(player, InteractionHand.MAIN_HAND);
    }

    @Override
    public ConditionResult checkAttackBlock(Player player, Level level, BlockHitResult hitResult) {
        return itemEquals(player, InteractionHand.MAIN_HAND);
    }

    @Override
    public ConditionResult checkAttackEntity(Player player, Level level, EntityHitResult hitResult) {
        return itemEquals(player, InteractionHand.MAIN_HAND);
    }

    @Override
    public ConditionResult checkInteractAny(Player player, Level level, InteractionHand hand, @Nullable HitResult hitResult) {
        return itemEquals(player, hand);
    }

    @Override
    public ConditionResult checkInteractBlock(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        return itemEquals(player, hand);
    }

    @Override
    public ConditionResult checkInteractEntity(Player player, Level level, InteractionHand hand, EntityHitResult hitResult) {
        return itemEquals(player, hand);
    }

    @Override
    public ConditionResult checkUseItem(Player player, Level level, InteractionHand hand, @Nullable HitResult hitResult) {
        return itemEquals(player, hand);
    }

    @Override
    public ConditionResult checkPlaceBlock(BlockPlaceContext placeContext) {
        return itemEquals(placeContext.getItemInHand().getItem());
    }
}
