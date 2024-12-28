package rh.preventbuild.conditions.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import rh.preventbuild.conditions.ConditionCategory;
import rh.preventbuild.conditions.ICondition;

import java.util.Arrays;

public class HeldItemCondition implements ICondition {
    private final String[] items;

    public HeldItemCondition(String[] items) {
        this.items = items;
    }

    @Override
    public ConditionCategory getCategory() {
        return ConditionCategory.OTHER;
    }

    @Override
    public boolean check(PlayerEntity player, Hand hand, int x, int y, int z) {
        String itemName = player.getStackInHand(hand).getItem().getTranslationKey();
        return Arrays.stream(this.items).anyMatch(i -> i.equalsIgnoreCase(itemName));
    }
}
