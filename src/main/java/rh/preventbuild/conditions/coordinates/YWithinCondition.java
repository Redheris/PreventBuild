package rh.preventbuild.conditions.coordinates;

import net.minecraft.entity.player.PlayerEntity;
import rh.preventbuild.conditions.ICondtition;

public class YWithinCondition implements ICondtition {
    private final int y_start;
    private final int y_end;

    public YWithinCondition(int y_start, int y_end) {
        this.y_start = y_start;
        this.y_end = y_end;
    }
    @Override
    public boolean check(PlayerEntity player, int x, int y, int z) {
        return y_start <= y && y <= y_end;
    }
}
