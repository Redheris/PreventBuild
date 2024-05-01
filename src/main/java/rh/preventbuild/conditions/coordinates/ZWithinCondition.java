package rh.preventbuild.conditions.coordinates;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import rh.preventbuild.conditions.CheckType;
import rh.preventbuild.conditions.ConditionType;
import rh.preventbuild.conditions.ICondtition;

public class ZWithinCondition implements ICondtition {
    private final ConditionType type = ConditionType.FINAL;
    private final int z_start;
    private final int z_end;

    ZWithinCondition(int z_start, int z_end) {
        this.z_start = z_start;
        this.z_end = z_end;
    }
    @Override
    public boolean check(CheckType type, PlayerEntity player, BlockHitResult hitResult, int x, int y, int z) {
        return z_start <= z && z <= z_end;
    }

    @Override
    public ConditionType getType() {
        return type;
    }
}
