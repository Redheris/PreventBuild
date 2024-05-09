package rh.preventbuild.conditions;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;

public interface ICondition {
    /**
     * Check of breaking block
     * */
    boolean check(PlayerEntity player, Hand hand, int x, int y, int z);

    /**
     * Check of placing block
     * */
    default boolean check(PlayerEntity player, Hand hand, int x, int y, int z, BlockHitResult hitResult) {
        return check(player, hand, x, y, z);
    }

    /**
     * Mostly debug string to print the condition.
     * It is more useful and convenient to use the debug mode
     */
    default String getString() {
        return this.getClass().getSimpleName();
    }
}
