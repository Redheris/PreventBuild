package rh.preventbuild.conditions.coordinates;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import rh.preventbuild.conditions.CheckType;
import rh.preventbuild.conditions.ConditionType;
import rh.preventbuild.conditions.ICondtition;

public class ZEqualCondition implements ICondtition {
    private final ConditionType type = ConditionType.FINAL;
    private final int z;

    ZEqualCondition(int z) {
        this.z = z;
    }
    @Override
    public boolean check(CheckType type, PlayerEntity player, BlockHitResult hitResult, int x, int y, int z) {
        return z == this.z;
    }

    @Override
    public ConditionType getType() {
        return type;
    }
}
