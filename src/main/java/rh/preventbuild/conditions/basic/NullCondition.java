package rh.preventbuild.conditions.basic;

import net.minecraft.entity.player.PlayerEntity;
import rh.preventbuild.conditions.ConditionType;
import rh.preventbuild.conditions.ICondtition;

public class NullCondition implements ICondtition {

    public ConditionType type = ConditionType.NULL;

    public NullCondition() {}

    @Override
    public boolean check(PlayerEntity player, int x, int y, int z) {
        return true;
    }

    @Override
    public ConditionType getType() {
        return type;
    }
}
