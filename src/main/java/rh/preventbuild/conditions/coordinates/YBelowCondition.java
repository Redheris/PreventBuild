package rh.preventbuild.conditions.coordinates;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import rh.preventbuild.conditions.ICondition;

public class YBelowCondition implements ICondition {
    private final int y;

    public YBelowCondition(int y) {
        this.y = y;
    }
    @Override
    public boolean check(PlayerEntity player, Hand hand, int x, int y, int z) {
        return y < this.y;
    }

}
