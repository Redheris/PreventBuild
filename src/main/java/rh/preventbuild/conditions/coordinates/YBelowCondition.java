package rh.preventbuild.conditions.coordinates;

import net.minecraft.entity.player.PlayerEntity;
import rh.preventbuild.conditions.ConditionType;
import rh.preventbuild.conditions.ICondtition;

public class YBelowCondition implements ICondtition {
    private final ConditionType type = ConditionType.FINAL;
    private final int y;

    public YBelowCondition(int y) {
        this.y = y;
    }
    @Override
    public boolean check(PlayerEntity player, int x, int y, int z) {
        return y < this.y;
    }

    @Override
    public ConditionType getType() {
        return type;
    }
}
