package rh.preventbuild.conditions.coordinates;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Direction;
import rh.preventbuild.conditions.ICondition;
import rh.preventbuild.conditions.ConditionCategory;

public class WithinCoordinatesCondition implements ICondition {
    private final int value_start;
    private final int value_end;
    private final ConditionCategory category;
    private final Direction.Axis axis;
    private final boolean isPlayerCoordinates;

    public WithinCoordinatesCondition(ConditionCategory category, Direction.Axis axis, int start, int end, boolean isPlayerCoordinates) {
        this.category = category;
        this.axis = axis;
        this.value_start = Math.min(start, end);
        this.value_end = Math.max(start, end);
        this.isPlayerCoordinates = isPlayerCoordinates;
    }

    @Override
    public ConditionCategory getCategory() {
        return this.category;
    }

    @Override
    public ActionResult attackBlockCheck(PlayerEntity player, Hand hand, int x, int y, int z) {
        int value = isPlayerCoordinates ? player.getBlockPos().getComponentAlongAxis(axis) : axis.choose(x, y, z);
        return value_start <= value && value <= value_end ? ActionResult.FAIL : ActionResult.PASS;
    }
}
