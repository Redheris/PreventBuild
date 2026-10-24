package dev.redheris.preventbuild.conditions.impl.coordinates;

import dev.redheris.preventbuild.conditions.ConditionResult;
import net.minecraft.core.Direction;

public class PlayerCoordinateEqualsCondition extends PlayerCoordinateConditionBase {
    private final int[] values;

    public PlayerCoordinateEqualsCondition(Direction.Axis axis, int[] values) {
        super(axis);
        this.values = values;
    }

    @Override
    protected ConditionResult checkCoordinate(int value) {
        for (int i : values) {
            if (value == i) return ConditionResult.CANCEL;
        }
        return ConditionResult.PASS;
    }
}
