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
     * Checking of left-clicking a block action
     */
    default ActionResult attackBlockCheck(PlayerEntity player, Hand hand, int x, int y, int z) {
        return ActionResult.PASS;
    }

    /**
     * Checking of right-clicking a block action
     */
    default ActionResult useBlockCheck(PlayerEntity player, Hand hand, int x, int y, int z, BlockHitResult hitResult) {
        return attackBlockCheck(player, hand, x, y, z);
    }

    /**
     * Checking of left- and right-clicking an entity action
     */
    default ActionResult useEntityCheck(ConditionCategory category, PlayerEntity player, World world, Hand hand, Entity entity, EntityHitResult hitResult) {
        return attackBlockCheck(player, hand, entity.getBlockX(), entity.getBlockY(), entity.getBlockZ());
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
