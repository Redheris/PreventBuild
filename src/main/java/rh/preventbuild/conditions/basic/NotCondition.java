package rh.preventbuild.conditions.basic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
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
    public boolean check(PlayerEntity player, int x, int y, int z) {
        return !ConditionHandler.checkCondition(nestedCondition, player, BlockPos.ofFloored(x, y, z));
    }
    @Override
    public boolean check(PlayerEntity player, int x, int y, int z, BlockHitResult hitResult) {
        return !ConditionHandler.checkCondition(nestedCondition, player, hitResult);
    }
    @Override
    public ConditionType getType() {
        return type;
    }
}
