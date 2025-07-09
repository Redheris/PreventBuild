package rh.preventbuild.conditions.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import rh.preventbuild.conditions.ConditionCategory;
import rh.preventbuild.conditions.ICondition;

public class ItemCustomNameCondition implements ICondition {
    private final String customName;

    public ItemCustomNameCondition (String customName) {
        this.customName = customName;
    }

    @Override
    public ConditionCategory getCategory() {
        return ConditionCategory.MISCELLANEOUS;
    }

    @Override
    public ActionResult attackBlockCheck(PlayerEntity player, Hand hand, int x, int y, int z) {
        Text customName = player.getStackInHand(hand).getCustomName();
        if (customName == null)
            return ActionResult.PASS;
        return customName.getString().equals(this.customName) ? ActionResult.FAIL : ActionResult.PASS;
    }
}
