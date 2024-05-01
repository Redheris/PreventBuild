package rh.preventbuild.conditions;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;

public class ConditionHandler {
    public static boolean checkCondition(CheckType type, ICondtition condition, PlayerEntity player, BlockHitResult hitResult) {
        int height;
        if (type == CheckType.PLACE)
            height = getPlacingY((BlockHitResult) hitResult);
        else
            height = hitResult.getBlockPos().getY();

        boolean res = condition.check(type, player, hitResult, height);
        if (condition.getType() == ConditionType.ADDITIONAL) {
            return res && checkCondition(type, condition.getNestedCondition(), player, hitResult);
        }
        return res;
    }

    private static int getPlacingY(BlockHitResult hitResult){
        return switch (hitResult.getSide()) {
            case UP -> hitResult.getBlockPos().getY() + 1;
            case DOWN -> hitResult.getBlockPos().getY() - 1;
            default -> hitResult.getBlockPos().getY();
        };
    }
}
