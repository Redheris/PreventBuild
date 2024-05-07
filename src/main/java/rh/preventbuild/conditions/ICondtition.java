package rh.preventbuild.conditions;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;

import java.nio.file.Path;

public interface ICondtition {
    /**
     * Check of breaking block
     * */
    boolean check(PlayerEntity player, int x, int y, int z);

    /**
     * Check of placing block
     * */
    default boolean check(PlayerEntity player, int x, int y, int z, BlockHitResult hitResult) {
        return check(player, x, y, z);
    }

    /**
     * Mostly debug string to print the condition.
     * It is more useful and convenient to use the debug mode
     */
    default String getString() {
        return this.getClass().getSimpleName();
    }
}
