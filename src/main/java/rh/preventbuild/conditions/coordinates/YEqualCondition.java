package rh.preventbuild.conditions.coordinates;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import rh.preventbuild.conditions.ICondition;

import java.util.Arrays;

public class YEqualCondition implements ICondition {
    private final int[] y;

    public YEqualCondition(int[] y) {
        this.y = y;
    }
    @Override
    public boolean check(PlayerEntity player, Hand hand, int x, int y, int z) {
        return Arrays.stream(this.y).anyMatch(i -> i == y);
    }

}
