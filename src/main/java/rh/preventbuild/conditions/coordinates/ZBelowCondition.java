
package rh.preventbuild.conditions.coordinates;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import rh.preventbuild.conditions.categories.ConditionCategory;
import rh.preventbuild.conditions.ICondition;

public class ZBelowCondition implements ICondition {
    private final int z;
    private final ConditionCategory category;

    public ZBelowCondition(ConditionCategory category, int z) {
        this.category = category;
        this.z = z;
    }

    @Override
    public ConditionCategory getCategory() {
        return category;
    }

    @Override
    public ActionResult attackBlockCheck(PlayerEntity player, Hand hand, int x, int y, int z) {
        return z < this.z ? ActionResult.FAIL : ActionResult.PASS;
    }

}
