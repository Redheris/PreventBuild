package rh.preventbuild.conditions.coordinates;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Direction;
import rh.preventbuild.conditions.ICondition;
import rh.preventbuild.conditions.basic.OrCondition;
import rh.preventbuild.conditions.categories.ConditionCategory;

import java.util.Arrays;

public class EqualsCoordinateCondition implements ICondition {
    private final int[] values;
    private final ConditionCategory category;
    private final Direction.Axis axis;
    private final boolean isPlayerCoordinates;

    public EqualsCoordinateCondition(ConditionCategory category, Direction.Axis axis, int[] values, boolean isPlayerCoordinates) {
        this.category = category;
        this.axis = axis;
        this.values = values;
        this.isPlayerCoordinates = isPlayerCoordinates;
    }

    @Override
    public ConditionCategory getCategory() {
        return this.category;
    }

    @Override
    public ActionResult attackBlockCheck(PlayerEntity player, Hand hand, int x, int y, int z) {
        int value = isPlayerCoordinates ? player.getBlockPos().getComponentAlongAxis(axis) : axis.choose(x, y, z);
        return Arrays.stream(this.values).anyMatch(i -> i == value) ? ActionResult.FAIL : ActionResult.PASS;
    }

    public static ICondition parse(ConditionCategory category, Direction.Axis axis, String value, boolean isPlayerCoordinates) {
        String[] values = value.split(",");
        ICondition[] conditions = new ICondition[values.length];
        for (int i = 0; i < values.length; i++) {
            if (values[i].contains("~")) {
                int start = Integer.parseInt(values[i].substring(0, values[i].indexOf("~")).trim());
                int end = Integer.parseInt(values[i].substring(values[i].indexOf("~") + 1).trim());
                conditions[i] = new WithinCoordinatesCondition(category, axis, start, end, isPlayerCoordinates);
            } else {
                conditions[i] = new EqualsCoordinateCondition(category, axis, new int[]{Integer.parseInt(values[i].trim())}, isPlayerCoordinates);
            }
        }
        if (conditions.length == 1)
            return conditions[0];
        return new OrCondition(conditions);
    }
}
