package rh.preventbuild.conditions.coordinates;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import rh.preventbuild.conditions.ConditionCategory;
import rh.preventbuild.conditions.ICondition;

public class XWithinCondition implements ICondition {
    private final int x_start;
    private final int x_end;
    private final ConditionCategory category;

    public XWithinCondition(ConditionCategory category, int x_start, int x_end) {
        this.category = category;
        this.x_start = x_start;
        this.x_end = x_end;
    }

    @Override
    public ConditionCategory getCategory() {
        return category;
    }

    @Override
    public boolean check(PlayerEntity player, Hand hand, int x, int y, int z) {
        return x_start <= x && x <= x_end;
    }

}
