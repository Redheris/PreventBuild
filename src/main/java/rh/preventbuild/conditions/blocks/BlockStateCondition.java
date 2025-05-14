package rh.preventbuild.conditions.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import rh.preventbuild.conditions.ConditionCategory;
import rh.preventbuild.conditions.ICondition;

public class BlockStateCondition implements ICondition {
    private final ConditionCategory category;
    private final String key;
    private final String value;

    public BlockStateCondition(ConditionCategory category, String key, String value) {
        this.category = category;
        this.key = key;
        this.value = value;
    }

    @Override
    public ConditionCategory getCategory() {
        return this.category;
    }

    @Override
    public ActionResult attackBlockCheck(PlayerEntity player, Hand hand, int x, int y, int z) {
        BlockState state = player.getWorld().getBlockState(new BlockPos(x, y, z));

        boolean res = state.getProperties().stream().anyMatch(p ->
                p.getName().equalsIgnoreCase(key) && state.get(p).toString().equalsIgnoreCase(value)
        );

        return res ? ActionResult.FAIL : ActionResult.PASS;
    }
}
