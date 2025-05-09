package rh.preventbuild.conditions.basic;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
    public ActionResult check(PlayerEntity player, Hand hand, int x, int y, int z) {
        for (ICondition condition : nestedConditions) {
            ActionResult res = ConditionHandler.checkCondition(condition, player, hand, BlockPos.ofFloored(x, y, z));
            if (res == ActionResult.PASS)
                return res;
        }
        return ActionResult.FAIL;
    }

    @Override
    public ActionResult check(PlayerEntity player, Hand hand, int x, int y, int z, BlockHitResult hitResult) {
        for (ICondition condition : nestedConditions) {
            ActionResult res = ConditionHandler.checkCondition(condition, player, hand, hitResult);
            if (res == ActionResult.PASS)
                return res;
        }
        return ActionResult.FAIL;
    }

    @Override
    public ActionResult check(ConditionCategory category, PlayerEntity player, World world, Hand hand, Entity entity, EntityHitResult hitResult) {
        for (ICondition condition : nestedConditions) {
            ActionResult res = condition.check(category, player, world, hand, entity, hitResult);
            if (res != ActionResult.SUCCESS)
                return res;
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public String getString(int tabs) {
        StringBuilder str = new StringBuilder("|\t".repeat(tabs) + "and:");
        for (ICondition nestedCondition : nestedConditions)
            str.append("\n").append(nestedCondition.getString(tabs + 1));
        return str.toString();
    }
}
