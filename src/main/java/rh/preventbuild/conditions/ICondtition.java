package rh.preventbuild.conditions;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import rh.preventbuild.conditions.basic.NullCondition;

public interface ICondtition {

    boolean check(PlayerEntity player, int x, int y, int z);

    default boolean check(PlayerEntity player, int x, int y, int z, BlockHitResult hitResult) {
        return check(player, x, y, z);
    };

    ConditionType getType();

    default ICondtition getNestedCondition() {
        return new NullCondition();
    }
}
