package rh.preventbuild.conditions.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import rh.preventbuild.conditions.ConditionCategory;

public class NullEntityCondition implements IEntityCondition {
    @Override
    public ConditionCategory getCategory() {
        return ConditionCategory.LOGIC;
    }

    @Override
    public ActionResult check(ConditionCategory category, PlayerEntity player, World world, Hand hand, Entity entity, EntityHitResult hitResult) {
        return ActionResult.PASS;
    }

    @Override
    public String getString(int tabs) {
        return "|\t".repeat(tabs) + "False";
    }
}
