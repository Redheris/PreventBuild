package rh.preventbuild.conditions.blocks;

import net.minecraft.block.AirBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import rh.preventbuild.conditions.categories.ConditionCategory;
import rh.preventbuild.conditions.ICondition;

import java.util.Arrays;

public class ReplaceBlockCondition implements ICondition {
    private final String[] blocks;

    public ReplaceBlockCondition(String[] blocks) {
        this.blocks = blocks;
    }

    @Override
    public ConditionCategory getCategory() {
        return ConditionCategory.PLACE;
    }

    @Override
    public ActionResult placeBlockCheck(PlayerEntity player, Hand hand, int x, int y, int z, BlockHitResult hitResult) {
        BlockState replaceBlock = player.getWorld().getBlockState(hitResult.getBlockPos());
        if (!replaceBlock.isReplaceable()) {
            replaceBlock = player.getWorld().getBlockState(new BlockPos(x, y, z));
            if (replaceBlock.getBlock() instanceof AirBlock || !replaceBlock.isReplaceable())
                return ActionResult.PASS;
        }
        if (player.getStackInHand(hand).getItem() == replaceBlock.getBlock().asItem())
            return ActionResult.PASS;
        String finalCurrentBlock = replaceBlock.getBlock().getTranslationKey();
        if (blocks == null || Arrays.stream(this.blocks).anyMatch(i -> i.equalsIgnoreCase(finalCurrentBlock)))
            return ActionResult.FAIL;
        return ActionResult.PASS;
    }
}
