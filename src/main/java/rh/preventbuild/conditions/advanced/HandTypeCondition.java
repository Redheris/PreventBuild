package rh.preventbuild.conditions.advanced;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import rh.preventbuild.conditions.ConditionCategory;
import rh.preventbuild.conditions.ICondition;

public class HandTypeCondition implements ICondition {
    private final Hand hand;

    public HandTypeCondition(String hand) {
        if (hand.equals("main_hand"))
            this.hand = Hand.MAIN_HAND;
        else
            this.hand = Hand.OFF_HAND;
    }
    @Override
    public ConditionCategory getCategory() {
        return ConditionCategory.MISCELLANEOUS;
    }

    @Override
    public ActionResult attackBlockCheck(PlayerEntity player, Hand hand, int x, int y, int z) {
        if (hand == this.hand)
            return ActionResult.FAIL;
        return ActionResult.PASS;
    }
}
