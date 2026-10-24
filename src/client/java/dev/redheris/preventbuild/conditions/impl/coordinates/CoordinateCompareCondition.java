package dev.redheris.preventbuild.conditions.impl.coordinates;

import dev.redheris.preventbuild.conditions.ConditionResult;
import net.minecraft.core.Direction;

public class CoordinateCompareCondition extends CoordinateConditionBase {
    private final int pivot;
    private final IntComparator comparator;

    public CoordinateCompareCondition(Direction.Axis axis, int pivot, IntComparator comparator) {
        super(axis);
        this.pivot = pivot;
        this.comparator = comparator;
    }

    @Override
    protected ConditionResult checkCoordinate(int value) {
        return comparator.compare(value, pivot) ? ConditionResult.CANCEL : ConditionResult.PASS;
    }
}
