package rh.preventbuild.conditions.coordinates;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Direction;
import rh.preventbuild.conditions.ICondition;
import rh.preventbuild.conditions.categories.ConditionCategory;

public class CompareCoordinateCondition implements ICondition {
    private final int value;
    private final ConditionCategory category;
    private final Direction.Axis axis;
    private final IntComparator comparator;
    private final boolean isPlayerCoordinates;

    public CompareCoordinateCondition(ConditionCategory category, Direction.Axis axis, IntComparator comp, int value, boolean isPlayerCoordinates) {
        this.category = category;
        this.axis = axis;
        this.comparator = comp;
        this.value = value;
        this.isPlayerCoordinates = isPlayerCoordinates;
    }

    @Override
    public ConditionCategory getCategory() {
        return this.category;
    }

    @Override
    public ActionResult attackBlockCheck(PlayerEntity player, Hand hand, int x, int y, int z) {
        int value = isPlayerCoordinates ? player.getBlockPos().getComponentAlongAxis(axis) : axis.choose(x, y, z);
        return comparator.compare(value, this.value) ? ActionResult.FAIL : ActionResult.PASS;
    }
}
