package rh.preventbuild.conditions.basic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import rh.preventbuild.conditions.ConditionCategory;
import rh.preventbuild.conditions.ConditionHandler;
import rh.preventbuild.conditions.ICondition;

public class OrCondition implements ICondition {
    private final ICondition[] nestedConditions;

    public OrCondition(ICondition... conditions) {
        this.nestedConditions = conditions;
    }

    @Override
    public ConditionCategory getCategory() {
        return ConditionCategory.LOGIC;
    }

    @Override
    public boolean check(PlayerEntity player, Hand hand, int x, int y, int z) {
        for (ICondition condition : nestedConditions) {
            if (ConditionHandler.checkCondition(condition, player, hand, BlockPos.ofFloored(x, y, z)))
                return true;
        }
        return false;
    }
    @Override
    public boolean check(PlayerEntity player, Hand hand, int x, int y, int z, BlockHitResult hitResult) {
        for (ICondition condition : nestedConditions) {
            if (ConditionHandler.checkCondition(condition, player, hand, hitResult))
                return true;
        }
        return false;
    }
    @Override
    public String getString() {
        String str = "or: \n{";
        for (ICondition nestedCondition : nestedConditions)
            str += "\n-> " + nestedCondition.getString();
        str += "}";
        str += "\n}\n";
        return str;
    }
}
