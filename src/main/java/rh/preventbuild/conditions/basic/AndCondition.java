package rh.preventbuild.conditions.basic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import rh.preventbuild.conditions.ConditionCategory;
import rh.preventbuild.conditions.ConditionHandler;
import rh.preventbuild.conditions.ICondition;

public class AndCondition implements ICondition {
    private final ICondition[] nestedConditions;

    public AndCondition(ICondition... conditions) {
        this.nestedConditions = conditions;
    }

    @Override
    public ConditionCategory getCategory() {
        return ConditionCategory.LOGIC;
    }

    @Override
    public boolean check(PlayerEntity player, Hand hand, int x, int y, int z) {
        for (ICondition condition : nestedConditions) {
            if (!ConditionHandler.checkCondition(condition, player, hand, BlockPos.ofFloored(x, y, z)))
                return false;
        }
        return true;
    }

    @Override
    public boolean check(PlayerEntity player, Hand hand, int x, int y, int z, BlockHitResult hitResult) {
        for (ICondition condition : nestedConditions) {
            if (!ConditionHandler.checkCondition(condition, player, hand, hitResult))
                return false;
        }
        return true;
    }
    @Override
    public String getString(int tabs) {
        StringBuilder str = new StringBuilder("|\t".repeat(tabs) + "and:");
        for (ICondition nestedCondition : nestedConditions)
            str.append("\n").append(nestedCondition.getString(tabs + 1));
        return str.toString();
    }
}
