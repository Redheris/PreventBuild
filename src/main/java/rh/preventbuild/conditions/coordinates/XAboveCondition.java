package rh.preventbuild.conditions.coordinates;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import rh.preventbuild.conditions.ICondition;

public class XAboveCondition implements ICondition {
    private final int x;

    public XAboveCondition(int x) {
        this.x = x;
    }
    @Override
    public boolean check(PlayerEntity player, Hand hand, int x, int y, int z) {
        return x > this.x;
    }

}
