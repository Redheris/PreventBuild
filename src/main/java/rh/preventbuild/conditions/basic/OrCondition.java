package rh.preventbuild.conditions.basic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import rh.preventbuild.conditions.CheckType;
import rh.preventbuild.conditions.ConditionHandler;
import rh.preventbuild.conditions.ConditionType;
import rh.preventbuild.conditions.ICondtition;

public class OrCondition implements ICondtition {
    private final ConditionType type = ConditionType.FINAL;
    private final ICondtition[] nestedConditions;

    public OrCondition(ICondtition... conditions) {
        this.nestedConditions = conditions;
    }
    @Override
    public boolean check(CheckType type, PlayerEntity player, BlockHitResult hitResult, int height) {
        for (ICondtition condition : nestedConditions) {
            if (ConditionHandler.checkCondition(type, condition, player, hitResult))
                return true;
        }
        return false;
    }
    @Override
    public ConditionType getType() {
        return type;
    }
}
