package dev.redheris.preventbuild.conditions.impl.coordinates;

import dev.redheris.preventbuild.conditions.ConditionResult;
import net.minecraft.core.Direction;

public class PlayerCoordinateCompareCondition extends PlayerCoordinateConditionBase {
    private final int pivot;
    private final IntComparator comparator;

    public PlayerCoordinateCompareCondition(Direction.Axis axis, int pivot, IntComparator comparator) {
        super(axis);
        this.pivot = pivot;
        this.comparator = comparator;
    }

    @Override
    protected ConditionResult checkCoordinate(int value) {
        return comparator.compare(value, pivot) ? ConditionResult.CANCEL : ConditionResult.PASS;
    }
}
