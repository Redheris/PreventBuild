package rh.preventbuild.conditions.coordinates;

import net.minecraft.entity.player.PlayerEntity;
import rh.preventbuild.conditions.ConditionType;
import rh.preventbuild.conditions.ICondtition;

public class ZEqualCondition implements ICondtition {
    private final ConditionType type = ConditionType.FINAL;
    private final int z;

    public ZEqualCondition(int z) {
        this.z = z;
    }
    @Override
    public boolean check(PlayerEntity player, int x, int y, int z) {
        return z == this.z;
    }

    @Override
    public ConditionType getType() {
        return type;
    }
}
