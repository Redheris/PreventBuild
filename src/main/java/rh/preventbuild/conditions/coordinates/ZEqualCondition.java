package rh.preventbuild.conditions.coordinates;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import rh.preventbuild.conditions.ConditionCategory;
import rh.preventbuild.conditions.ICondition;

import java.util.Arrays;

public class ZEqualCondition implements ICondition {
    private final int[] z;
    private final ConditionCategory category;

    public ZEqualCondition(ConditionCategory category, int[] z) {
        this.category = category;
        this.z = z;
    }

    @Override
    public ConditionCategory getCategory() {
        return category;
    }

    @Override
    public ActionResult check(PlayerEntity player, Hand hand, int x, int y, int z) {
        return Arrays.stream(this.z).anyMatch(i -> i == z) ? ActionResult.FAIL : ActionResult.PASS;
    }

}
