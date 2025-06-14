
package rh.preventbuild.conditions.coordinates;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import rh.preventbuild.conditions.categories.ConditionCategory;
import rh.preventbuild.conditions.ICondition;

public class XBelowCondition implements ICondition {
    private final int x;
    private final ConditionCategory category;

    public XBelowCondition(ConditionCategory category, int x) {
        this.category = category;
        this.x = x;
    }

    @Override
    public ConditionCategory getCategory() {
        return category;
    }

    @Override
    public ActionResult attackBlockCheck(PlayerEntity player, Hand hand, int x, int y, int z) {
        return x < this.x ? ActionResult.FAIL : ActionResult.PASS;
    }

}
