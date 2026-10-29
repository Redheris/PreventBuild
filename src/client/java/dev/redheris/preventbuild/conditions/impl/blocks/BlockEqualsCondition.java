package dev.redheris.preventbuild.conditions.impl.blocks;

import dev.redheris.preventbuild.conditions.ConditionResult;
import dev.redheris.preventbuild.conditions.util.IdentifierTagWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class BlockEqualsCondition extends BlockConditionBase {
    private final List<IdentifierTagWrapper<Block>> blockIds;

    public BlockEqualsCondition(List<IdentifierTagWrapper<Block>> blockIds) {
        this.blockIds = blockIds;
    }

    protected ConditionResult containsBlock(Block block) {
        Identifier blockId = BuiltInRegistries.BLOCK.getKey(block);
        for (IdentifierTagWrapper<Block> wrapper : blockIds) {
            if (wrapper.isIn(block, blockId)) {
                return ConditionResult.CANCEL;
            }
        }
        return ConditionResult.PASS;
    }

    @Override
    protected ConditionResult checkBlock(Level level, BlockPos blockPos) {
        Block block = level.getBlockState(blockPos).getBlock();
        return containsBlock(block);
    }
}
