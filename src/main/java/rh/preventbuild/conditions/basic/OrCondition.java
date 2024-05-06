package rh.preventbuild.conditions.basic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import rh.preventbuild.conditions.ConditionHandler;
import rh.preventbuild.conditions.ICondtition;

public class OrCondition implements ICondtition {
    private final ICondtition[] nestedConditions;

    public OrCondition(ICondtition... conditions) {
        this.nestedConditions = conditions;
    }
    @Override
    public boolean check(PlayerEntity player, int x, int y, int z) {
        for (ICondtition condition : nestedConditions) {
            if (ConditionHandler.checkCondition(condition, player, BlockPos.ofFloored(x, y, z)))
                return true;
        }
        return false;
    }
    @Override
    public boolean check(PlayerEntity player, int x, int y, int z, BlockHitResult hitResult) {
        for (ICondtition condition : nestedConditions) {
            if (ConditionHandler.checkCondition(condition, player, hitResult))
                return true;
        }
        return false;
    }
}
