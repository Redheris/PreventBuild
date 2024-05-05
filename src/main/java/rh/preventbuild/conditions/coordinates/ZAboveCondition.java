package rh.preventbuild.conditions.coordinates;

import net.minecraft.entity.player.PlayerEntity;
import rh.preventbuild.conditions.ConditionType;
import rh.preventbuild.conditions.ICondtition;

public class ZAboveCondition implements ICondtition {
    private final ConditionType type = ConditionType.FINAL;
    private final int z;

    public ZAboveCondition(int z) {
        this.z = z;
    }
    @Override
    public boolean check(PlayerEntity player, int x, int y, int z) {
        return z > this.z;
    }

    @Override
    public ConditionType getType() {
        return type;
    }
}
