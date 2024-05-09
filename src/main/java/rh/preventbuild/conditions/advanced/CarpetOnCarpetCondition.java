package rh.preventbuild.conditions.advanced;

import net.minecraft.block.Block;
import net.minecraft.block.CarpetBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import rh.preventbuild.conditions.ICondition;

public class CarpetOnCarpetCondition implements ICondition {

    @Override
    public boolean check(PlayerEntity player, Hand hand, int x, int y, int z) {
        return false;
    }

    @Override
    public boolean check(PlayerEntity player, Hand hand, int x, int y, int z, BlockHitResult hitResult) {
        Block block = player.getWorld().getBlockState(new BlockPos(x, y - 1, z)).getBlock();
        return block instanceof CarpetBlock;
    }
}
