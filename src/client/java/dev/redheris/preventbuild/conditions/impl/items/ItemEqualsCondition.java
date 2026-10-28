package dev.redheris.preventbuild.conditions.impl.items;

import dev.redheris.preventbuild.conditions.ConditionResult;
import dev.redheris.preventbuild.conditions.util.IdentifierTagWrapper;
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

import java.util.List;

public class ItemEqualsCondition extends ItemConditionBase {
    private final List<IdentifierTagWrapper<Item>> itemIds;

    public ItemEqualsCondition(List<IdentifierTagWrapper<Item>> itemIds) {
        this.itemIds = itemIds;
    }

    protected ConditionResult checkItem(Item item) {
        Identifier itemId = BuiltInRegistries.ITEM.getKey(item);
        for (IdentifierTagWrapper<Item> wrapper : itemIds) {
            if (wrapper.isIn(item, itemId)) {
                return ConditionResult.CANCEL;
            }
        }
        return ConditionResult.PASS;
    }

    private ConditionResult checkItem(Player player, InteractionHand hand) {
        Item heldItem = player.getItemInHand(hand).getItem();
        return this.checkItem(heldItem);
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
        return this.checkItem(placeContext.getItemInHand().getItem());
    }
}
