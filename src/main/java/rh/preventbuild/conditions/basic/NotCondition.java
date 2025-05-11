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

public class NotCondition implements ICondition {
    private final ICondition nestedCondition;
    public NotCondition(ICondition condition) {
        this.nestedCondition = condition;
    }

    @Override
    public ConditionCategory getCategory() {
        return ConditionCategory.LOGIC;
    }

    @Override
    public ActionResult attackBlockCheck(PlayerEntity player, Hand hand, int x, int y, int z) {
        ActionResult res = ConditionHandler.checkCondition(nestedCondition, player, hand, BlockPos.ofFloored(x, y, z));
        if (res != ActionResult.PASS)
            return ActionResult.FAIL;
        return ActionResult.PASS;

    }
    @Override
    public ActionResult useBlockCheck(PlayerEntity player, Hand hand, int x, int y, int z, BlockHitResult hitResult) {
        ActionResult res = ConditionHandler.checkCondition(nestedCondition, player, hand, hitResult);
        if (res == ActionResult.PASS)
            return ActionResult.FAIL;
        return ActionResult.PASS;
    }

    @Override
    public ActionResult useEntityCheck(PlayerEntity player, World world, Hand hand, Entity entity, EntityHitResult hitResult) {
        if (nestedCondition.useEntityCheck(player, world, hand, entity, hitResult) != ActionResult.PASS)
            return ActionResult.PASS;
        return ActionResult.FAIL;
    }

    @Override
    public ActionResult attackEntityCheck(PlayerEntity player, World world, Hand hand, Entity entity, EntityHitResult hitResult) {
        if (nestedCondition.attackEntityCheck(player, world, hand, entity, hitResult) != ActionResult.PASS)
            return ActionResult.PASS;
        return ActionResult.FAIL;
    }

    @Override
    public String getString(int tabs) {
        return "|\t".repeat(tabs) + "not:\n" + nestedCondition.getString(tabs + 1);
    }
}
