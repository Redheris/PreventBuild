package rh.preventbuild.conditions.coordinates;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import rh.preventbuild.conditions.categories.ConditionCategory;
import rh.preventbuild.conditions.ICondition;

public class ZWithinCondition implements ICondition {
    private final int z_start;
    private final int z_end;
    private final ConditionCategory category;

    public ZWithinCondition(ConditionCategory category, int z_start, int z_end) {
        this.category = category;
        this.z_start = z_start;
        this.z_end = z_end;
    }

    @Override
    public ConditionCategory getCategory() {
        return category;
    }

    @Override
    public ActionResult attackBlockCheck(PlayerEntity player, Hand hand, int x, int y, int z) {
        return z_start <= z && z <= z_end ? ActionResult.FAIL : ActionResult.PASS;
    }

}
