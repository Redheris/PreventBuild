package dev.redheris.preventbuild.conditions.impl.coordinates;

import dev.redheris.preventbuild.conditions.ConditionBase;
import dev.redheris.preventbuild.conditions.ConditionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public abstract class PlayerCoordinateConditionBase extends ConditionBase {
    protected final Direction.Axis axis;

    public PlayerCoordinateConditionBase(Direction.Axis axis) {
        this.axis = axis;
    }

    protected abstract ConditionResult checkCoordinate(int value);

    @Override
    public ConditionResult checkDefault(Player player, Level level) {
        int value = player.blockPosition().get(axis);
        return checkCoordinate(value);
    }
}
