package rh.preventbuild.conditions;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import rh.preventbuild.conditions.categories.ConditionCategory;

public interface ICondition {
    ConditionCategory getCategory();
    /**
     * Checking the action of left-clicking a block
     */
    default ActionResult attackBlockCheck(PlayerEntity player, Hand hand, int x, int y, int z) {
        return ActionResult.PASS;
    }

    /**
     * Checking the action of placing a block
     */
    default ActionResult placeBlockCheck(PlayerEntity player, Hand hand, int x, int y, int z, BlockHitResult hitResult) {
        return attackBlockCheck(player, hand, x, y, z);
    }
    /**
     * Checking the action of tight-clicking a block
     */
    default ActionResult interactBlockCheck(PlayerEntity player, Hand hand, int x, int y, int z, BlockHitResult hitResult) {
        return placeBlockCheck(player, hand, x, y, z, hitResult);
    }

    /**
     * Checking the action of using an item
     */
    default ActionResult useItemCheck(PlayerEntity player, World world, Hand hand) {
        return attackBlockCheck(player, hand, player.getBlockX(), player.getBlockY(), player.getBlockZ());
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
