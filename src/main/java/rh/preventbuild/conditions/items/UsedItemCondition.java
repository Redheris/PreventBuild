package rh.preventbuild.conditions.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import rh.preventbuild.conditions.categories.ConditionCategory;
import rh.preventbuild.conditions.ICondition;

import java.util.Arrays;

public class UsedItemCondition implements ICondition {
    private final String[] items;

    public UsedItemCondition(String[] items) {
        this.items = items;
    }

    @Override
    public ConditionCategory getCategory() {
        return ConditionCategory.MISCELLANEOUS;
    }

    @Override
    public ActionResult attackBlockCheck(PlayerEntity player, Hand hand, int x, int y, int z) {
        String itemName = player.getStackInHand(hand).getItem().getTranslationKey();
        if (Arrays.stream(this.items).anyMatch(i -> i.equalsIgnoreCase(itemName)))
            return ActionResult.FAIL;
        return ActionResult.PASS;
    }
}
