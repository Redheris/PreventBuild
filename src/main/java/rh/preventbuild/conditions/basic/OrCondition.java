package rh.preventbuild.conditions.basic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import rh.preventbuild.conditions.CheckType;
import rh.preventbuild.conditions.ConditionHandler;
import rh.preventbuild.conditions.ConditionType;
import rh.preventbuild.conditions.ICondtition;

public class OrCondition implements ICondtition {
    private final ConditionType type = ConditionType.FINAL;
    private final ICondtition nestedCondition;
    private final ICondtition nestedCondition2;

    public OrCondition(ICondtition left, ICondtition right, int height) {
        this.nestedCondition = left;
        this.nestedCondition2 = right;
    }
    @Override
    public boolean check(CheckType type, PlayerEntity player, BlockHitResult hitResult, int height) {
        return ConditionHandler.checkCondition(type, nestedCondition, player, hitResult)
                || ConditionHandler.checkCondition(type, nestedCondition2, player, hitResult);
    }
    @Override
    public ConditionType getType() {
        return type;
    }
}
