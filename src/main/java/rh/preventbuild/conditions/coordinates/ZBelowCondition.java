
package rh.preventbuild.conditions.coordinates;

import net.minecraft.entity.player.PlayerEntity;
import rh.preventbuild.conditions.ICondtition;

public class ZBelowCondition implements ICondtition {
    private final int z;

    public ZBelowCondition(int z) {
        this.z = z;
    }
    @Override
    public boolean check(PlayerEntity player, int x, int y, int z) {
        return z < this.z;
    }

}
