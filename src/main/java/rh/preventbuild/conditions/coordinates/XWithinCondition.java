package rh.preventbuild.conditions.coordinates;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import rh.preventbuild.conditions.ICondition;

public class XWithinCondition implements ICondition {
    private final int x_start;
    private final int x_end;

    public XWithinCondition(int x_start, int x_end) {
        this.x_start = x_start;
        this.x_end = x_end;
    }
    @Override
    public boolean check(PlayerEntity player, Hand hand, int x, int y, int z) {
        return x_start <= x && x <= x_end;
    }

}
