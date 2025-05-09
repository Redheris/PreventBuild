package rh.preventbuild.conditions.coordinates;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import rh.preventbuild.conditions.ConditionCategory;
import rh.preventbuild.conditions.ICondition;

public class YWithinCondition implements ICondition {
    private final int y_start;
    private final int y_end;
    private final ConditionCategory category;

    public YWithinCondition(ConditionCategory category, int y_start, int y_end) {
        this.category = category;
        this.y_start = y_start;
        this.y_end = y_end;
    }

    @Override
    public ConditionCategory getCategory() {
        return category;
    }

    @Override
    public ActionResult check(PlayerEntity player, Hand hand, int x, int y, int z) {
        return y_start <= y && y <= y_end ? ActionResult.FAIL : ActionResult.PASS;
    }
}
