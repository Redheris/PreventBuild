package rh.preventbuild.conditions.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import rh.preventbuild.conditions.ConditionCategory;
import rh.preventbuild.conditions.ICondition;

public class EntityCondition implements ICondition {
    private final ConditionCategory category;
    private final String entity;

    public EntityCondition(ConditionCategory category, String entity) {
        this.category = category;
        this.entity = entity;
    }

    @Override
    public ConditionCategory getCategory() {
        return this.category;
    }

    @Override
    public ActionResult check(ConditionCategory category, PlayerEntity player, World world, Hand hand, Entity entity, EntityHitResult hitResult) {
        if (entity.getType().getTranslationKey().equals(this.entity))
            return ActionResult.FAIL;
        return ActionResult.PASS;
    }
}
