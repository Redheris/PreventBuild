package rh.preventbuild.conditions.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import rh.preventbuild.conditions.ConditionCategory;
import rh.preventbuild.conditions.ICondition;

public interface IEntityCondition extends ICondition {

    /**
     * Check of left- or right-click on entity action
     */
    ActionResult check(ConditionCategory category, PlayerEntity player, World world, Hand hand, Entity entity, EntityHitResult hitResult);

    /**
     * Check of breaking and placing blocks action
     */
    default boolean check(PlayerEntity player, Hand hand, int x, int y, int z) {
        return false;
    }
}
