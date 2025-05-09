package rh.preventbuild.conditions;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

public interface ICondition {
    ConditionCategory getCategory();
    /**
     * Checking of breaking action
     */
    default ActionResult check(PlayerEntity player, Hand hand, int x, int y, int z) {
        return ActionResult.PASS;
    }

    /**
     * Checking of placing action
     */
    default ActionResult check(PlayerEntity player, Hand hand, int x, int y, int z, BlockHitResult hitResult) {
        return check(player, hand, x, y, z);
    }

    /**
     * Checking of left- or right-click on entity action
     */
    default ActionResult check(ConditionCategory category, PlayerEntity player, World world, Hand hand, Entity entity, EntityHitResult hitResult) {
        return check(player, hand, entity.getBlockX(), entity.getBlockY(), entity.getBlockZ());
    }

    /**
     * Debug string to print the condition.
     * It can be more useful and convenient to use the debug mode
     */
    default String getString(int tabs) {
        return "|\t".repeat(tabs) + this.getClass().getSimpleName();
    }

    default String getString() {
        return getString(0);
    }
}
