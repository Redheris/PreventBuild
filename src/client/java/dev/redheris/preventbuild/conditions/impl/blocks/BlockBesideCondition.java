package dev.redheris.preventbuild.conditions.impl.blocks;

import dev.redheris.preventbuild.conditions.ConditionResult;
import dev.redheris.preventbuild.conditions.util.IdentifierTagWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class BlockBesideCondition extends BlockEqualsCondition {
    private final Direction direction;

    public BlockBesideCondition(List<IdentifierTagWrapper<Block>> blockIds, Direction direction) {
        super(blockIds);
        this.direction = direction;
    }

    @Override
    protected ConditionResult checkBlock(Level level, BlockPos blockPos) {
        Block block = level.getBlockState(blockPos.relative(direction)).getBlock();
        return super.containsBlock(block);
    }
}
