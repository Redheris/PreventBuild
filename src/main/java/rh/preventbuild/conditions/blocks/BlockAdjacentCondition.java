package rh.preventbuild.conditions.blocks;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import rh.preventbuild.conditions.ConditionCategory;
import rh.preventbuild.conditions.ICondition;

import java.util.Arrays;

public class BlockAdjacentCondition implements ICondition {
    private final String[] blocks;
    private final ConditionCategory category;

    public BlockAdjacentCondition(ConditionCategory category, String[] blocks) {
        this.category = category;
        this.blocks = blocks;
    }

    @Override
    public ConditionCategory getCategory() {
        return category;
    }

    @Override
    public ActionResult attackBlockCheck(PlayerEntity player, Hand hand, int x, int y, int z) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                    if (Math.abs(i) + Math.abs(j) + Math.abs(k) != 1)
                        continue;
                    String blockName = player.getEntityWorld().getBlockState(new BlockPos(x + i, y + j, z + k)).getBlock().getTranslationKey();
                    if (Arrays.stream(this.blocks).anyMatch(blk -> blk.equalsIgnoreCase(blockName)))
                        return ActionResult.FAIL;
                }
            }
        }
        return ActionResult.PASS;
    }
}
