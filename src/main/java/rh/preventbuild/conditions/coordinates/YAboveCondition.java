package rh.preventbuild.conditions.coordinates;

import net.minecraft.entity.player.PlayerEntity;
import rh.preventbuild.conditions.ICondtition;

public class YAboveCondition implements ICondtition {
    private final int y;

    public YAboveCondition(int y) {
        this.y = y;
    }
    @Override
    public boolean check(PlayerEntity player, int x, int y, int z) {
        return y > this.y;
    }

}
