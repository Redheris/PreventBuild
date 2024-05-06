package rh.preventbuild.conditions;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class ConditionHandler {
    public static boolean checkCondition(ICondtition condition, PlayerEntity player, BlockPos pos) {
        return condition.check(player, pos.getX(), pos.getY(), pos.getZ());
    }

    public static boolean checkCondition(ICondtition condition, PlayerEntity player, BlockHitResult hitResult) {
        BlockPos pos = getPlacingPos(hitResult.getBlockPos(), hitResult.getSide());;
        return condition.check(player, pos.getX(), pos.getY(), pos.getZ());
    }

    private static BlockPos getPlacingPos(BlockPos pos, Direction side){
        return switch (side) {
            case UP -> new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ());
            case DOWN -> new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ());
            case SOUTH -> new BlockPos(pos.getX(), pos.getY(), pos.getZ() + 1);
            case NORTH -> new BlockPos(pos.getX(), pos.getY(), pos.getZ() - 1);
            case WEST -> new BlockPos(pos.getX() - 1, pos.getY(), pos.getZ());
            case EAST -> new BlockPos(pos.getX() + 1, pos.getY(), pos.getZ());
        };
    }
}
