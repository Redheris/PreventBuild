package rh.preventbuild.conditions;

import rh.preventbuild.conditions.advanced.AxeStrippingCondition;
import rh.preventbuild.conditions.advanced.CarpetOnCarpetCondition;
import rh.preventbuild.conditions.advanced.DoubleSlabCondition;
import rh.preventbuild.conditions.basic.*;
import rh.preventbuild.conditions.blocks.BlockAboveCondition;
import rh.preventbuild.conditions.blocks.BlockAdjacentCondition;
import rh.preventbuild.conditions.blocks.BlockBelowCondition;
import rh.preventbuild.conditions.blocks.BlockEqualCondition;
import rh.preventbuild.conditions.coordinates.*;

public class ConditionsNotation {
    public static ICondition createCondition(String name, ICondition[] conds) {
        return switch (name) {
            case "and" -> new AndCondition(conds);
            case "or" -> new OrCondition(conds);
            case "not" -> new NotCondition(conds[0]);
            default -> new NullCondition();
        };
    }

    public static ICondition createCondition(String name, ConditionCategory category, int[] nums) {
        return switch (name) {
            case "xEqual" -> new XEqualCondition(category, nums);
            case "xAbove" -> new XAboveCondition(category, nums[0]);
            case "xBelow" -> new XBelowCondition(category, nums[0]);
            case "xWithin" -> new XWithinCondition(category, nums[0], nums[1]);
            case "yEqual" -> new YEqualCondition(category, nums);
            case "yAbove" -> new YAboveCondition(category, nums[0]);
            case "yBelow" -> new YBelowCondition(category, nums[0]);
            case "yWithin" -> new YWithinCondition(category, nums[0], nums[1]);
            case "zEqual" -> new ZEqualCondition(category, nums);
            case "zAbove" -> new ZAboveCondition(category, nums[0]);
            case "zBelow" -> new ZBelowCondition(category, nums[0]);
            case "zWithin" -> new ZWithinCondition(category, nums[0], nums[1]);
            default -> new NullCondition();
        };
    }

    public static ICondition createCondition(String name, ConditionCategory category, String... names) {
        return switch (name) {
            case "blockEqual" -> new BlockEqualCondition(category, names);
            case "blockAbove" -> new BlockAboveCondition(category, names);
            case "blockBelow" -> new BlockBelowCondition(category, names);
            case "blockAdjacent" -> new BlockAdjacentCondition(category, names);
            default -> new NullCondition();
        };
    }

    public static ICondition createCondition(String name, ConditionCategory category) {
        return switch (name) {
            case "axeStripping" -> new AxeStrippingCondition();
            case "carpetOnCarpet" -> new CarpetOnCarpetCondition(category);
            case "doubleSlab" -> new DoubleSlabCondition(category);
            default -> new NullCondition();
        };
    }
}
