package rh.preventbuild.conditions.coordinates;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import rh.preventbuild.conditions.categories.ConditionCategory;
import rh.preventbuild.conditions.ICondition;
import rh.preventbuild.conditions.basic.OrCondition;

import java.util.Arrays;

public class YEqualsCondition implements ICondition {
    private final int[] y;
    private final ConditionCategory category;

    public YEqualsCondition(ConditionCategory category, int[] y) {
        this.category = category;
        this.y = y;
    }

    @Override
    public ConditionCategory getCategory() {
        return category;
    }

    @Override
    public ActionResult attackBlockCheck(PlayerEntity player, Hand hand, int x, int y, int z) {
        return Arrays.stream(this.y).anyMatch(i -> i == y) ? ActionResult.FAIL : ActionResult.PASS;
    }

    public static ICondition parse(ConditionCategory category, String value) {
        String[] values = value.split(",");
        ICondition[] conditions = new ICondition[values.length];
        for (int i = 0; i < values.length; i++) {
            if (values[i].contains("~")) {
                int start = Integer.parseInt(values[i].substring(0, values[i].indexOf("~")).trim());
                int end = Integer.parseInt(values[i].substring(values[i].indexOf("~") + 1).trim());
                conditions[i] = new YWithinCondition(category, start, end);
            } else {
                conditions[i] = new YEqualsCondition(category, new int[]{Integer.parseInt(values[i].trim())});
            }
        }
        if (conditions.length == 1)
            return conditions[0];
        return new OrCondition(conditions);
    }
}
