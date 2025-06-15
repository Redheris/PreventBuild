package rh.preventbuild.conditions.blocks;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import rh.preventbuild.conditions.categories.ConditionCategory;
import rh.preventbuild.conditions.ICondition;

import java.util.Arrays;

public class BlockEqualsCondition implements ICondition {
    private final String[] blocks;
    private final ConditionCategory category;

    public BlockEqualsCondition(ConditionCategory category, String[] blocks) {
        this.category = category;
        this.blocks = blocks;
    }

    @Override
    public ConditionCategory getCategory() {
        return category;
    }

    @Override
    public ActionResult attackBlockCheck(PlayerEntity player, Hand hand, int x, int y, int z) {
        String blockName = player.getWorld().getBlockState(new BlockPos(x, y, z)).getBlock().getTranslationKey();
        if (Arrays.stream(this.blocks).anyMatch(i -> i.equalsIgnoreCase(blockName)))
            return ActionResult.FAIL;
        return ActionResult.PASS;
    }

    @Override
    public ActionResult placeBlockCheck(PlayerEntity player, Hand hand, int x, int y, int z, BlockHitResult hitResult) {
        String blockName = player.getStackInHand(hand).getItem().getTranslationKey();
        if (Arrays.stream(this.blocks).anyMatch(i -> i.equalsIgnoreCase(blockName)))
            return ActionResult.FAIL;
        return ActionResult.PASS;
    }

    @Override
    public ActionResult interactBlockCheck(PlayerEntity player, Hand hand, int x, int y, int z, BlockHitResult hitResult) {
        String blockName = player.getWorld().getBlockState(hitResult.getBlockPos()).getBlock().getTranslationKey();
        if (Arrays.stream(this.blocks).anyMatch(i -> i.equalsIgnoreCase(blockName)))
            return ActionResult.FAIL;
        return ActionResult.PASS;
    }
}
