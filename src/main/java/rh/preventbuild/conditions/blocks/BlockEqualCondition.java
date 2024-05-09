package rh.preventbuild.conditions.blocks;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import rh.preventbuild.conditions.ICondition;

import java.util.Arrays;

public class BlockEqualCondition implements ICondition {

    private final String[] blocks;

    public BlockEqualCondition(String[] blocks) {
        this.blocks = blocks;
    }
    @Override
    public boolean check(PlayerEntity player, Hand hand, int x, int y, int z) {
        String blockName = player.getWorld().getBlockState(new BlockPos(x, y, z)).getBlock().getTranslationKey();
        return Arrays.stream(this.blocks).anyMatch(i -> i.equalsIgnoreCase(blockName));
    }

    @Override
    public boolean check(PlayerEntity player, Hand hand, int x, int y, int z, BlockHitResult hitResult) {
        String blockName = player.getStackInHand(hand).getItem().getTranslationKey();
        return Arrays.stream(this.blocks).anyMatch(i -> i.equalsIgnoreCase(blockName));
    }
}
