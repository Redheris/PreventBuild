
package rh.preventbuild.conditions.coordinates;

import net.minecraft.entity.player.PlayerEntity;
import rh.preventbuild.conditions.ICondition;

public class XBelowCondition implements ICondition {
    private final int x;

    public XBelowCondition(int x) {
        this.x = x;
    }
    @Override
    public boolean check(PlayerEntity player, int x, int y, int z) {
        return x < this.x;
    }

}
