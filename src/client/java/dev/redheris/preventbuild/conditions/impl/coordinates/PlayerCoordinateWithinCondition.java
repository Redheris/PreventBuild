package dev.redheris.preventbuild.conditions.impl.coordinates;

import dev.redheris.preventbuild.conditions.ConditionResult;
import net.minecraft.core.Direction;

public class PlayerCoordinateWithinCondition extends PlayerCoordinateConditionBase {
    private final int minValue;
    private final int maxValue;

    public PlayerCoordinateWithinCondition(Direction.Axis axis, int minValue, int maxValue) {
        super(axis);
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    protected ConditionResult checkCoordinate(int value) {
        return (minValue <= value && value <= maxValue) ? ConditionResult.CANCEL : ConditionResult.PASS;
    }
}
