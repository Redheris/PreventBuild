package rh.preventbuild.conditions.coordinates;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import rh.preventbuild.conditions.CheckType;
import rh.preventbuild.conditions.ConditionType;
import rh.preventbuild.conditions.ICondtition;

public class XWithinCondition implements ICondtition {
    private final ConditionType type = ConditionType.FINAL;
    private final int x_start;
    private final int x_end;

    public XWithinCondition(int x_start, int x_end) {
        this.x_start = x_start;
        this.x_end = x_end;
    }
    @Override
    public boolean check(CheckType type, PlayerEntity player, BlockHitResult hitResult, int x, int y, int z) {
        return x_start <= x && x <= x_end;
    }

    @Override
    public ConditionType getType() {
        return type;
    }
}
