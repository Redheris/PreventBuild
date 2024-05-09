
package rh.preventbuild.conditions.coordinates;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import rh.preventbuild.conditions.ICondition;

public class ZBelowCondition implements ICondition {
    private final int z;

    public ZBelowCondition(int z) {
        this.z = z;
    }
    @Override
    public boolean check(PlayerEntity player, Hand hand, int x, int y, int z) {
        return z < this.z;
    }

}
