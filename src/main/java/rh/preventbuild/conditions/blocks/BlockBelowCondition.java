package rh.preventbuild.conditions.blocks;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import rh.preventbuild.conditions.ConditionCategory;
import rh.preventbuild.conditions.ICondition;

import java.util.Arrays;

public class BlockBelowCondition implements ICondition {
    private final String[] blocks;
    private final ConditionCategory category;

    public BlockBelowCondition(ConditionCategory category, String[] blocks) {
        this.category = category;
        this.blocks = blocks;
    }

    @Override
    public ConditionCategory getCategory() {
        return category;
    }

    @Override
    public ActionResult check(PlayerEntity player, Hand hand, int x, int y, int z) {
        String blockName = player.getWorld().getBlockState(new BlockPos(x, y - 1, z)).getBlock().getTranslationKey();
        if (Arrays.stream(this.blocks).anyMatch(i -> i.equalsIgnoreCase(blockName)))
            return ActionResult.FAIL;
        return ActionResult.PASS;
    }
}
