package rh.preventbuild.conditions.basic;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import rh.preventbuild.conditions.ConditionCategory;
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
    public ActionResult attackBlockCheck(PlayerEntity player, Hand hand, int x, int y, int z) {
        for (ICondition condition : nestedConditions) {
//            ActionResult res = ConditionHandler.checkCondition(condition, player, hand, BlockPos.ofFloored(x, y, z));
            ActionResult res = condition.attackBlockCheck(player, hand, x, y, z);
            if (res != ActionResult.PASS)
                return res;
        }
        return ActionResult.PASS;
    }

    @Override
    public ActionResult placeBlockCheck(PlayerEntity player, Hand hand, int x, int y, int z, BlockHitResult hitResult) {
        for (ICondition condition : nestedConditions) {
            ActionResult res = condition.placeBlockCheck(player, hand, x, y, z, hitResult);
            if (res != ActionResult.PASS)
                return res;
        }
        return ActionResult.PASS;
    }

    @Override
    public ActionResult interactBlockCheck(PlayerEntity player, Hand hand, int x, int y, int z, BlockHitResult hitResult) {
        for (ICondition condition : nestedConditions) {
            ActionResult res = condition.interactBlockCheck(player, hand, x, y, z, hitResult);
            if (res != ActionResult.PASS)
                return res;
        }
        return ActionResult.PASS;
    }

    @Override
    public ActionResult useItemCheck(PlayerEntity player, World world, Hand hand) {
        for (ICondition condition : nestedConditions) {
            ActionResult res = condition.useItemCheck(player, world, hand);
            if (res != ActionResult.PASS)
                return res;
        }
        return ActionResult.PASS;
    }

    @Override
    public ActionResult useEntityCheck(PlayerEntity player, World world, Hand hand, Entity entity, EntityHitResult hitResult) {
        for (ICondition condition : nestedConditions) {
            ActionResult res = condition.useEntityCheck(player, world, hand, entity, hitResult);
            if (res != ActionResult.PASS)
                return res;
        }
        return ActionResult.PASS;
    }

    @Override
    public ActionResult attackEntityCheck(PlayerEntity player, World world, Hand hand, Entity entity, EntityHitResult hitResult) {
        for (ICondition condition : nestedConditions) {
            ActionResult res = condition.attackEntityCheck(player, world, hand, entity, hitResult);
            if (res != ActionResult.PASS)
                return res;
        }
        return ActionResult.PASS;
    }

    @Override
    public String getString(int tabs) {
        StringBuilder str = new StringBuilder("|\t".repeat(tabs) + "or:");
        for (ICondition nestedCondition : nestedConditions)
            str.append("\n").append(nestedCondition.getString(tabs + 1));
        return str.toString();
    }
}
