package rh.preventbuild.conditions.coordinates;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import rh.preventbuild.conditions.ICondition;

public class ZWithinCondition implements ICondition {
    private final int z_start;
    private final int z_end;

    public ZWithinCondition(int z_start, int z_end) {
        this.z_start = z_start;
        this.z_end = z_end;
    }
    @Override
    public boolean check(PlayerEntity player, Hand hand, int x, int y, int z) {
        return z_start <= z && z <= z_end;
    }

}
