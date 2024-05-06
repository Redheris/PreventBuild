package rh.preventbuild.conditions.coordinates;

import net.minecraft.entity.player.PlayerEntity;
import rh.preventbuild.conditions.ICondtition;

public class XAboveCondition implements ICondtition {
    private final int x;

    public XAboveCondition(int x) {
        this.x = x;
    }
    @Override
    public boolean check(PlayerEntity player, int x, int y, int z) {
        return x > this.x;
    }

}
