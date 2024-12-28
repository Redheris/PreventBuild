package rh.preventbuild.conditions.coordinates;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import rh.preventbuild.conditions.ConditionCategory;
import rh.preventbuild.conditions.ICondition;

import java.util.Arrays;

public class XEqualCondition implements ICondition {
    private final int[] x;
    private final ConditionCategory category;

    public XEqualCondition(ConditionCategory category, int[] x) {
        this.category = category;
        this.x = x;
    }

    @Override
    public ConditionCategory getCategory() {
        return category;
    }

    @Override
    public boolean check(PlayerEntity player, Hand hand, int x, int y, int z) {
        return Arrays.stream(this.x).anyMatch(i -> i == x);
    }

}
