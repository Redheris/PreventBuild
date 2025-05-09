package rh.preventbuild.conditions;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;

public class ConditionHandler {
    /**
     * Checking the condition for breaking block.
     *
     * @param condition the condition to check
     * @param player    the player entity
     * @param hand      which hand was used
     * @param pos       the position to check
     * @return          the result of the condition check
     */
    public static ActionResult checkCondition(ICondition condition, PlayerEntity player, Hand hand, BlockPos pos) {
        return condition.check(player, hand, pos.getX(), pos.getY(), pos.getZ());
    }
    /**
     * Checking the condition for placing a block with coordinate offset to the block that was clicked.
     *
     * @param  condition   the condition to check
     * @param  player      the player entity
     * @param  hitResult   the block hit result
     * @return             the result of the condition check
     */
    public static ActionResult checkCondition(ICondition condition, PlayerEntity player, Hand hand, BlockHitResult hitResult) {
        BlockPos pos = hitResult.getBlockPos().offset(hitResult.getSide());
        return condition.check(player, hand, pos.getX(), pos.getY(), pos.getZ(), hitResult);
    }
}
