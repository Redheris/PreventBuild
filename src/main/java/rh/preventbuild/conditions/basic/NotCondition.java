package rh.preventbuild.conditions.basic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import rh.preventbuild.conditions.CheckType;
import rh.preventbuild.conditions.ConditionHandler;
import rh.preventbuild.conditions.ConditionType;
import rh.preventbuild.conditions.ICondtition;

public class NotCondition implements ICondtition {
    private final ConditionType type = ConditionType.FINAL;
    private final ICondtition nestedCondition;
    public NotCondition(ICondtition condition) {
        this.nestedCondition = condition;
    }
    @Override
    public boolean check(CheckType type, PlayerEntity player, BlockHitResult hitResult, int height) {
        return !ConditionHandler.checkCondition(type, nestedCondition, player, hitResult);
    }
    @Override
    public ConditionType getType() {
        return type;
    }
}
