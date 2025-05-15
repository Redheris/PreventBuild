package rh.preventbuild.conditions.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import rh.preventbuild.conditions.ConditionCategory;
import rh.preventbuild.conditions.ICondition;

public class ItemDamageCondition implements ICondition {
    private final int value;
    private final int mode;
    private final boolean durabilityMode;

    /**
     * Prevents if the held item match the specified damage or durability value
     * @param value The value to be compared to
     * @param durabilityMode If {@code true}, checks remaining durability instead of lost durability
     * @param mode Defines the mode of compare:
     * {@code -1} - current value is less than the {@code value},
     * {@code 0} - current value is equal to the {@code value},
     * {@code 1} - current value is greater than the {@code value},
     */
    public ItemDamageCondition(int value, boolean durabilityMode, int mode) {
        this.value = value;
        this.durabilityMode = durabilityMode;
        this.mode = mode;
    }

    @Override
    public ConditionCategory getCategory() {
        return ConditionCategory.USE_ITEM;
    }

    @Override
    public ActionResult attackBlockCheck(PlayerEntity player, Hand hand, int x, int y, int z) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (!itemStack.isDamageable())
            return ActionResult.PASS;

        int compareOf = itemStack.getDamage();
        if (durabilityMode)
            compareOf = itemStack.getMaxDamage() - compareOf;

        if (mode == 0 && compareOf == this.value)
            return ActionResult.FAIL;
        if (mode == -1 && compareOf < this.value)
            return ActionResult.FAIL;
        if (mode == 1 && compareOf > this.value)
            return ActionResult.FAIL;

        return ActionResult.PASS;
    }
}
