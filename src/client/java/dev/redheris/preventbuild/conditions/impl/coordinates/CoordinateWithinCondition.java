package dev.redheris.preventbuild.conditions.impl.coordinates;

import dev.redheris.preventbuild.conditions.ConditionResult;
import net.minecraft.core.Direction;

public class CoordinateWithinCondition extends CoordinateConditionBase {
    private final int minValue;
    private final int maxValue;

    public CoordinateWithinCondition(Direction.Axis axis, int minValue, int maxValue) {
        super(axis);
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    protected ConditionResult checkCoordinate(int value) {
        return (minValue <= value && value <= maxValue) ? ConditionResult.CANCEL : ConditionResult.PASS;
    }
}
