package rh.preventbuild.conditions;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;

public class ConditionHandler {
    public static boolean checkCondition(ICondtition condition, PlayerEntity player, BlockPos pos) {
        boolean res = condition.check(player, pos.getX(), pos.getY(), pos.getZ());
        if (condition.getType() == ConditionType.ADDITIONAL) {
            return res && checkCondition(condition.getNestedCondition(), player, pos);
        }
        return res;
    }

    public static boolean checkCondition(ICondtition condition, PlayerEntity player, BlockHitResult hitResult) {
        BlockPos pos = hitResult.getBlockPos();
        boolean res = condition.check(player, pos.getX(), getPlacingY(hitResult), pos.getZ());
        if (condition.getType() == ConditionType.ADDITIONAL) {
            return res && checkCondition(condition.getNestedCondition(), player, hitResult);
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
