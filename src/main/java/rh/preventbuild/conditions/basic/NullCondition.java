package rh.preventbuild.conditions.basic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import rh.preventbuild.conditions.CheckType;
import rh.preventbuild.conditions.ConditionType;
import rh.preventbuild.conditions.ICondtition;

public class NullCondition implements ICondtition {

    public ConditionType type = ConditionType.NULL;

    public NullCondition() {};

    @Override
    public boolean check(CheckType type, PlayerEntity player, BlockHitResult hitResult, int height) {
        return true;
    }

    @Override
    public ConditionType getType() {
        return type;
    }
}
