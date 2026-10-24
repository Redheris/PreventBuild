package dev.redheris.preventbuild.conditions.impl.blocks;

import dev.redheris.preventbuild.conditions.ConditionBase;
import dev.redheris.preventbuild.conditions.ConditionResult;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

public class BlockEqualsCondition extends ConditionBase {
    private final Identifier blockId;

    public BlockEqualsCondition(Identifier blockId) {
        this.blockId = blockId;
    }

    private boolean blockEquals(Block block) {
        if (!BuiltInRegistries.BLOCK.containsKey(blockId)) return false;
        return BuiltInRegistries.BLOCK.getKey(block).equals(blockId);
    }

    @Override
    public ConditionResult checkAttackAny(Player player, Level level, HitResult hitResult) {
        if (hitResult instanceof BlockHitResult blockHitResult) {
            Block hitBlock = level.getBlockState(blockHitResult.getBlockPos()).getBlock();
            return blockEquals(hitBlock) ? ConditionResult.CANCEL : ConditionResult.PASS;
        }
        return ConditionResult.PASS;
    }

    @Override
    public ConditionResult checkAttackBlock(Player player, Level level, BlockHitResult hitResult) {
        Block hitBlock = level.getBlockState(hitResult.getBlockPos()).getBlock();
        return blockEquals(hitBlock) ? ConditionResult.CANCEL : ConditionResult.PASS;
    }

    @Override
    public ConditionResult checkInteractAny(Player player, Level level, InteractionHand hand, @Nullable HitResult hitResult) {
        return checkAttackAny(player, level, hitResult);
    }

    @Override
    public ConditionResult checkInteractBlock(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        return checkAttackBlock(player, level, hitResult);
    }

    @Override
    public ConditionResult checkUseItem(Player player, Level level, InteractionHand hand, @Nullable HitResult hitResult) {
        if (hitResult instanceof BlockHitResult blockHitResult) {
            Block hitBlock = level.getBlockState(blockHitResult.getBlockPos()).getBlock();
            return blockEquals(hitBlock) ? ConditionResult.CANCEL : ConditionResult.PASS;
        }
        return ConditionResult.PASS;
    }

    @Override
    public ConditionResult checkPlaceBlock(BlockPlaceContext placeContext) {
        Item item = placeContext.getItemInHand().getItem();
        if (item instanceof BlockItem blockItem) {
            Block heldBlock = blockItem.getBlock();
            return blockEquals(heldBlock) ? ConditionResult.CANCEL : ConditionResult.PASS;
        }
        return ConditionResult.PASS;
    }
}
