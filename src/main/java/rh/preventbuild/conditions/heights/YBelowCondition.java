package rh.preventbuild.conditions.heights;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import rh.preventbuild.conditions.CheckType;
import rh.preventbuild.conditions.ConditionType;
import rh.preventbuild.conditions.ICondtition;

public class YBelowCondition implements ICondtition {
    private final ConditionType type = ConditionType.FINAL;
    private final int y;

    YBelowCondition(int y) {
        this.y = y;
    }
    @Override
    public boolean check(CheckType type, PlayerEntity player, BlockHitResult hitResult, int height) {
        return height < y;
    }

    @Override
    public ConditionType getType() {
        return type;
    }
}
