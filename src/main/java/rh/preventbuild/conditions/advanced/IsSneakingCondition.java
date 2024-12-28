package rh.preventbuild.conditions.advanced;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import rh.preventbuild.conditions.ConditionCategory;
import rh.preventbuild.conditions.ICondition;

public class IsSneakingCondition implements ICondition {
    private final boolean is_sneaking;

    public IsSneakingCondition(boolean is_sneaking) {
        this.is_sneaking = is_sneaking;
    }

    @Override
    public ConditionCategory getCategory() {
        return ConditionCategory.OTHER;
    }

    @Override
    public boolean check(PlayerEntity player, Hand hand, int x, int y, int z) {
        return player.isSneaking() == is_sneaking;
    }
}
