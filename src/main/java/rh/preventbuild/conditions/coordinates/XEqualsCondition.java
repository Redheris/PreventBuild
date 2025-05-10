package rh.preventbuild.conditions.coordinates;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import rh.preventbuild.conditions.ConditionCategory;
import rh.preventbuild.conditions.ICondition;
import rh.preventbuild.conditions.basic.OrCondition;

import java.util.Arrays;

public class XEqualsCondition implements ICondition {
    private final int[] x;
    private final ConditionCategory category;

    public XEqualsCondition(ConditionCategory category, int[] x) {
        this.category = category;
        this.x = x;
    }

    @Override
    public ConditionCategory getCategory() {
        return category;
    }

    @Override
    public ActionResult check(PlayerEntity player, Hand hand, int x, int y, int z) {
        return Arrays.stream(this.x).anyMatch(i -> i == x) ? ActionResult.FAIL : ActionResult.PASS;
    }

    public static ICondition parse(ConditionCategory category, String value) {
        String[] values = value.split(",");
        ICondition[] conditions = new ICondition[values.length];
        for (int i = 0; i < values.length; i++) {
            if (values[i].contains("~")) {
                int start = Integer.parseInt(values[i].substring(0, values[i].indexOf("~")).trim());
                int end = Integer.parseInt(values[i].substring(values[i].indexOf("~") + 1).trim());
                conditions[i] = new XWithinCondition(category, start, end);
            } else {
                conditions[i] = new XEqualsCondition(category, new int[]{Integer.parseInt(values[i].trim())});
            }
        }
        if (conditions.length == 1)
            return conditions[0];
        return new OrCondition(conditions);
    }
}
