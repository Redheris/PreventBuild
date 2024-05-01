package rh.preventbuild.conditions;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import rh.preventbuild.conditions.basic.NullCondition;

public interface ICondtition {

    boolean check(CheckType type, PlayerEntity player, BlockHitResult hitResult, int x, int y, int z);

    ConditionType getType();

    default ICondtition getNestedCondition() {
        return new NullCondition();
    }
}
