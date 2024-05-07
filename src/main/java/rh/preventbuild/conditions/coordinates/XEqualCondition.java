package rh.preventbuild.conditions.coordinates;

import net.minecraft.entity.player.PlayerEntity;
import rh.preventbuild.conditions.ICondition;

import java.util.Arrays;

public class XEqualCondition implements ICondition {
    private final int[] x;

    public XEqualCondition(int[] x) {
        this.x = x;
    }
    @Override
    public boolean check(PlayerEntity player, int x, int y, int z) {
        return Arrays.stream(this.x).anyMatch(i -> i == x);
    }

}
