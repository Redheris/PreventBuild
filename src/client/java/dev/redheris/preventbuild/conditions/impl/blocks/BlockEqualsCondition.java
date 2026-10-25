package dev.redheris.preventbuild.conditions.impl.blocks;

import dev.redheris.preventbuild.conditions.ConditionResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class BlockEqualsCondition extends BlockConditionBase {
    private final List<Identifier> blockIds;

    public BlockEqualsCondition(List<Identifier> blockIds) {
        this.blockIds = blockIds;
    }

    private ConditionResult checkBlock(Block block) {
        Identifier blockId = BuiltInRegistries.BLOCK.getKey(block);
        return blockIds.contains(blockId) ? ConditionResult.CANCEL : ConditionResult.PASS;
    }

    @Override
    protected ConditionResult checkBlock(Level level, BlockPos blockPos) {
        Block block = level.getBlockState(blockPos).getBlock();
        return checkBlock(block);
    }

    @Override
    public ConditionResult checkPlaceBlock(BlockPlaceContext placeContext) {
        Item item = placeContext.getItemInHand().getItem();
        if (item instanceof BlockItem blockItem) {
            return checkBlock(blockItem.getBlock());
        }
        return ConditionResult.PASS;
    }
}
