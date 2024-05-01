package rh.preventbuild.conditions;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import rh.preventbuild.conditions.basic.NullCondition;

public interface ICondtition {

    boolean check(CheckType type, PlayerEntity player, BlockHitResult hitResult, int height);

    ConditionType getType();

    default ICondtition getNestedCondition() {
        return new NullCondition();
    };
}
