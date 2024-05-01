package rh.preventbuild.conditions.basic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import rh.preventbuild.conditions.CheckType;
import rh.preventbuild.conditions.ConditionHandler;
import rh.preventbuild.conditions.ConditionType;
import rh.preventbuild.conditions.ICondtition;

public class AndCondition implements ICondtition {
    private final ConditionType type = ConditionType.FINAL;
    private final ICondtition[] nestedConditions;

    public AndCondition(ICondtition... conditions) {
        this.nestedConditions = conditions;
    }
    @Override
    public boolean check(CheckType type, PlayerEntity player, BlockHitResult hitResult, int x, int y, int z) {
        for (ICondtition condition : nestedConditions) {
            if (!ConditionHandler.checkCondition(type, condition, player, hitResult))
                return false;
        }
        return true;
    }

    @Override
    public ConditionType getType() {
        return type;
    }
}
