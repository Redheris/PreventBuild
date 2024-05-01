package rh.preventbuild.conditions.coordinates;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import rh.preventbuild.conditions.CheckType;
import rh.preventbuild.conditions.ConditionType;
import rh.preventbuild.conditions.ICondtition;

public class XAboveCondition implements ICondtition {
    private final ConditionType type = ConditionType.FINAL;
    private final int x;

    public XAboveCondition(int x) {
        this.x = x;
    }
    @Override
    public boolean check(CheckType type, PlayerEntity player, BlockHitResult hitResult, int x, int y, int z) {
        return x > this.x;
    }

    @Override
    public ConditionType getType() {
        return type;
    }
}
