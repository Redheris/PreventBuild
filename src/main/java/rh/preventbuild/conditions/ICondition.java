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
     * Checking the action of left-clicking a block
     */
    default ActionResult attackBlockCheck(PlayerEntity player, Hand hand, int x, int y, int z) {
        return ActionResult.PASS;
    }

    /**
     * Checking the action of right-clicking a block
     */
    default ActionResult useBlockCheck(PlayerEntity player, Hand hand, int x, int y, int z, BlockHitResult hitResult) {
        return attackBlockCheck(player, hand, x, y, z);
    }

    /**
     * Checking the action of right-clicking an entity
     */
    default ActionResult useEntityCheck(PlayerEntity player, World world, Hand hand, Entity entity, EntityHitResult hitResult) {
        return attackBlockCheck(player, hand, entity.getBlockX(), entity.getBlockY(), entity.getBlockZ());
    }

    /**
     * Checking the action of left-clicking an entity
     */
    default ActionResult attackEntityCheck(PlayerEntity player, World world, Hand hand, Entity entity, EntityHitResult hitResult) {
        return useEntityCheck(player, world, hand, entity, hitResult);
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
