package rh.preventbuild.conditions.coordinates;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import rh.preventbuild.conditions.CheckType;
import rh.preventbuild.conditions.ConditionType;
import rh.preventbuild.conditions.ICondtition;

public class YBelowCondition implements ICondtition {
    private final ConditionType type = ConditionType.FINAL;
    private final int y;

    public YBelowCondition(int y) {
        this.y = y;
    }
    @Override
    public boolean check(CheckType type, PlayerEntity player, BlockHitResult hitResult, int x, int y, int z) {
        return y < this.y;
    }

    @Override
    public ConditionType getType() {
        return type;
    }
}
