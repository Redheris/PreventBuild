package rh.preventbuild.conditions.advanced;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ShovelItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import rh.preventbuild.conditions.ICondition;
import rh.preventbuild.conditions.ConditionCategory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DirtPathCreatingCondition implements ICondition {
    private static final List<Block> PATH_BLOCKS;

    @Override
    public ConditionCategory getCategory() {
        return ConditionCategory.INTERACT_BLOCK;
    }

    @Override
    public ActionResult useItemCheck(PlayerEntity player, World world, Hand hand) {
        if (!(player.getStackInHand(hand).getItem() instanceof ShovelItem))
            return ActionResult.PASS;

        HitResult target = MinecraftClient.getInstance().crosshairTarget;
        assert target != null;
        if (target.getType() != HitResult.Type.BLOCK)
            return ActionResult.PASS;
        BlockHitResult targetBlock = (BlockHitResult) target;
        BlockState lookingAt = player.getEntityWorld().getBlockState(targetBlock.getBlockPos());

        if (!world.getBlockState(((BlockHitResult) target).getBlockPos().up()).isAir() || ((BlockHitResult) target).getSide() == Direction.DOWN)
            return ActionResult.PASS;
        if (PATH_BLOCKS.contains(lookingAt.getBlock()))
            return ActionResult.FAIL;

        return ActionResult.PASS;
    }

    static {
        PATH_BLOCKS = new ArrayList<>(Arrays.asList(
                Blocks.GRASS_BLOCK,
                Blocks.DIRT,
                Blocks.PODZOL,
                Blocks.COARSE_DIRT,
                Blocks.MYCELIUM,
                Blocks.ROOTED_DIRT
        ));
    }
}
