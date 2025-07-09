package rh.preventbuild.conditions.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import rh.preventbuild.conditions.ConditionCategory;
import rh.preventbuild.conditions.ICondition;

import java.util.Arrays;

public class EntityEqualsCondition implements ICondition {
    private final ConditionCategory category;
    private final String[] entities;

    public EntityEqualsCondition(ConditionCategory category, String... entities) {
        this.category = category;
        this.entities = entities;
    }

    @Override
    public ConditionCategory getCategory() {
        return this.category;
    }

    @Override
    public ActionResult useEntityCheck(PlayerEntity player, World world, Hand hand, Entity entity, EntityHitResult hitResult) {
        String entityName = entity.getType().getTranslationKey();
        if (Arrays.stream(this.entities).anyMatch(i -> i.equalsIgnoreCase(entityName)))
            return ActionResult.FAIL;
        return ActionResult.PASS;
    }
}
