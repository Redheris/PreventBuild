package rh.preventbuild.conditions.coordinates;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import rh.preventbuild.conditions.ConditionCategory;
import rh.preventbuild.conditions.ICondition;

public class YAboveCondition implements ICondition {
    private final int y;
    private final ConditionCategory category;

    public YAboveCondition(ConditionCategory category, int y) {
        this.category = category;
        this.y = y;
    }

    @Override
    public ConditionCategory getCategory() {
        return category;
    }

    @Override
    public boolean check(PlayerEntity player, Hand hand, int x, int y, int z) {
        return y > this.y;
    }

}
