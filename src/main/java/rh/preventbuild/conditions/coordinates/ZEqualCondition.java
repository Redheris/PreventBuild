package rh.preventbuild.conditions.coordinates;

import net.minecraft.entity.player.PlayerEntity;
import rh.preventbuild.conditions.ICondition;

import java.util.Arrays;

public class ZEqualCondition implements ICondition {
    private final int[] z;

    public ZEqualCondition(int[] z) {
        this.z = z;
    }
    @Override
    public boolean check(PlayerEntity player, int x, int y, int z) {
        return Arrays.stream(this.z).anyMatch(i -> i == z);
    }

}
