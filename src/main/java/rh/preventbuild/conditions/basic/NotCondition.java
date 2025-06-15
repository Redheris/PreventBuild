package rh.preventbuild.conditions.basic;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import rh.preventbuild.conditions.categories.ConditionCategory;
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
        ActionResult res = nestedCondition.attackBlockCheck(player, hand, x, y, z);
        if (res == ActionResult.PASS)
            return ActionResult.FAIL;
        return ActionResult.PASS;

    }

    @Override
    public ActionResult placeBlockCheck(PlayerEntity player, Hand hand, int x, int y, int z, BlockHitResult hitResult) {
        ActionResult res = nestedCondition.placeBlockCheck(player, hand, x, y, z, hitResult);
        if (res == ActionResult.PASS)
            return ActionResult.FAIL;
        return ActionResult.PASS;
    }

    @Override
    public ActionResult interactBlockCheck(PlayerEntity player, Hand hand, int x, int y, int z, BlockHitResult hitResult) {
        ActionResult res = nestedCondition.interactBlockCheck(player, hand, x, y, z, hitResult);
        if (res == ActionResult.PASS)
            return ActionResult.FAIL;
        return ActionResult.PASS;
    }

    @Override
    public ActionResult useItemCheck(PlayerEntity player, World world, Hand hand) {
        ActionResult res = nestedCondition.useItemCheck(player, world, hand);
        if (res == ActionResult.PASS)
            return ActionResult.FAIL;
        return ActionResult.PASS;
    }

    @Override
    public ActionResult useEntityCheck(PlayerEntity player, World world, Hand hand, Entity entity, EntityHitResult hitResult) {
        if (nestedCondition.useEntityCheck(player, world, hand, entity, hitResult) == ActionResult.PASS)
            return ActionResult.FAIL;
        return ActionResult.PASS;
    }

    @Override
    public ActionResult attackEntityCheck(PlayerEntity player, World world, Hand hand, Entity entity, EntityHitResult hitResult) {
        if (nestedCondition.attackEntityCheck(player, world, hand, entity, hitResult) == ActionResult.PASS)
            return ActionResult.FAIL;
        return ActionResult.PASS;
    }

    @Override
    public String getString(int tabs) {
        return "|\t".repeat(tabs) + "not:\n" + nestedCondition.getString(tabs + 1);
    }
}
