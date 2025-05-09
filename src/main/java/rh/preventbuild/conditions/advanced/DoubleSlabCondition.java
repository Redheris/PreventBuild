package rh.preventbuild.conditions.advanced;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import rh.preventbuild.conditions.ConditionCategory;
import rh.preventbuild.conditions.ICondition;

public class DoubleSlabCondition implements ICondition {
    private final ConditionCategory category;

    public DoubleSlabCondition(ConditionCategory category) {
        this.category = category;
    }
    
    @Override
    public ConditionCategory getCategory() {
        return category;
    }

    @Override
    public ActionResult check(PlayerEntity player, Hand hand, int x, int y, int z) {
        BlockState blockState = player.getWorld().getBlockState(new BlockPos(x, y, z));
        if (blockState.getBlock() instanceof SlabBlock && blockState.get(SlabBlock.TYPE) == SlabType.DOUBLE)
            return ActionResult.FAIL;
        return ActionResult.PASS;
    }

    @Override
    public ActionResult check(PlayerEntity player, Hand hand, int x, int y, int z, BlockHitResult hitResult) {
        BlockState blockState = player.getWorld().getBlockState(new BlockPos(x, y, z));
        Block heldBlock = Block.getBlockFromItem(player.getStackInHand(hand).getItem());
        if (heldBlock instanceof SlabBlock) {
            BlockState blockStateSide = player.getWorld().getBlockState(new BlockPos(x, y + 1, z));
            if (blockStateSide.getBlock() instanceof SlabBlock && hitResult.getSide() == Direction.DOWN && blockStateSide.get(SlabBlock.TYPE) == SlabType.TOP)
                blockState = blockStateSide;
            else {
                blockStateSide = player.getWorld().getBlockState(new BlockPos(x, y - 1, z));
                if (blockStateSide.getBlock() instanceof SlabBlock && hitResult.getSide() == Direction.UP && blockStateSide.get(SlabBlock.TYPE) == SlabType.BOTTOM)
                    blockState = blockStateSide;
            }
            if (Block.getBlockFromItem(player.getStackInHand(hand).getItem()) instanceof SlabBlock
                    && blockState.getBlock() instanceof SlabBlock)
                return ActionResult.FAIL;
        }
        return ActionResult.PASS;
    }
}
