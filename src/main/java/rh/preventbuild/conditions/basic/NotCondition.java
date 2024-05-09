package rh.preventbuild.conditions.basic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import rh.preventbuild.conditions.ConditionHandler;
import rh.preventbuild.conditions.ICondition;

public class NotCondition implements ICondition {
    private final ICondition nestedCondition;
    public NotCondition(ICondition condition) {
        this.nestedCondition = condition;
    }
    @Override
    public boolean check(PlayerEntity player, Hand hand, int x, int y, int z) {
        return !ConditionHandler.checkCondition(nestedCondition, player, hand, BlockPos.ofFloored(x, y, z));
    }
    @Override
    public boolean check(PlayerEntity player, Hand hand, int x, int y, int z, BlockHitResult hitResult) {
        return !ConditionHandler.checkCondition(nestedCondition, player, hand, hitResult);
    }
    @Override
    public String getString() {
        return "not: {" + nestedCondition.getString() + "}";
    }
}
