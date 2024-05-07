package rh.preventbuild.conditions;

import rh.preventbuild.conditions.basic.*;
import rh.preventbuild.conditions.coordinates.*;

public class ConditionsNotation {
    public static ICondtition createCondition(String name, ICondtition[] conds) {
        return switch (name) {
            case "and" -> new AndCondition(conds);
            case "or" -> new OrCondition(conds);
            case "not" -> new NotCondition(conds[0]);
            default -> new NullCondition();
        };
    }

    public static ICondtition createCondition(String name, int[] nums) {
        return switch (name) {
            case "xEqual" -> new XEqualCondition(nums);
            case "xAbove" -> new XAboveCondition(nums[0]);
            case "xBelow" -> new XBelowCondition(nums[0]);
            case "xWithin" -> new XWithinCondition(nums[0], nums[1]);
            case "yEqual" -> new YEqualCondition(nums);
            case "yAbove" -> new YAboveCondition(nums[0]);
            case "yBelow" -> new YBelowCondition(nums[0]);
            case "yWithin" -> new YWithinCondition(nums[0], nums[1]);
            case "zEqual" -> new ZEqualCondition(nums);
            case "zAbove" -> new ZAboveCondition(nums[0]);
            case "zBelow" -> new ZBelowCondition(nums[0]);
            case "zWithin" -> new ZWithinCondition(nums[0], nums[1]);
            default -> new NullCondition();
        };
    }

//    public static ICondtition createCondition(String name, String... names) {
//        return switch (name) {
//            case "blockEqual" -> new BlockEqualCondition(names[0]);
//            case "blockAbove" -> new BlockAboveCondition(names[0]);
//            case "blockBelow" -> new BlockBelowCondition(names[0]);
//            case "blockAdjacent" -> new BlockAdjacentCondition(names[0]);
//            default -> new NullCondition();
//        };
//    }
}
