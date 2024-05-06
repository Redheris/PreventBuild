package rh.preventbuild.conditions.basic;

import net.minecraft.entity.player.PlayerEntity;
import rh.preventbuild.conditions.ICondtition;

public class NullCondition implements ICondtition {
    public NullCondition() {}

    @Override
    public boolean check(PlayerEntity player, int x, int y, int z) {
        return true;
    }

}
