package dev.redheris.preventbuild.conditions.impl.blocks;

import dev.redheris.preventbuild.conditions.ConditionResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class BlockBesideCondition extends BlockConditionBase {
    private final List<Identifier> blockIds;
    private final Direction direction;

    public BlockBesideCondition(List<Identifier> blockIds, Direction direction) {
        this.blockIds = blockIds;
        this.direction = direction;
    }

    @Override
    protected ConditionResult checkBlock(Level level, BlockPos blockPos) {
        Block block = level.getBlockState(blockPos.relative(direction)).getBlock();
        Identifier blockId = BuiltInRegistries.BLOCK.getKey(block);
        return blockIds.contains(blockId) ? ConditionResult.CANCEL : ConditionResult.PASS;
    }

    @Override
    public ConditionResult checkPlaceBlock(BlockPlaceContext placeContext) {
        return checkBlock(placeContext.getLevel(), placeContext.getClickedPos());
    }
}
