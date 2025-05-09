package rh.preventbuild.conditions.coordinates;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import rh.preventbuild.conditions.ConditionCategory;
import rh.preventbuild.conditions.ICondition;

import java.util.Arrays;

public class YEqualCondition implements ICondition {
    private final int[] y;
    private final ConditionCategory category;

    public YEqualCondition(ConditionCategory category, int[] y) {
        this.category = category;
        this.y = y;
    }

    @Override
    public ConditionCategory getCategory() {
        return category;
    }

    @Override
    public ActionResult check(PlayerEntity player, Hand hand, int x, int y, int z) {
        return Arrays.stream(this.y).anyMatch(i -> i == y) ? ActionResult.FAIL : ActionResult.PASS;
    }

}
