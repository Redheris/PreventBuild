package rh.preventbuild.conditions.basic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
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
    public boolean check(PlayerEntity player, int x, int y, int z) {
        for (ICondtition condition : nestedConditions) {
            if (!ConditionHandler.checkCondition(condition, player, BlockPos.ofFloored(x, y, z)))
                return false;
        }
        return true;
    }

    @Override
    public boolean check(PlayerEntity player, int x, int y, int z, BlockHitResult hitResult) {
        for (ICondtition condition : nestedConditions) {
            if (!ConditionHandler.checkCondition(condition, player, hitResult))
                return false;
        }
        return true;
    }

    @Override
    public ConditionType getType() {
        return type;
    }
}
