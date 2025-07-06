package rh.preventbuild.api;

import rh.preventbuild.conditions.ICondition;
import rh.preventbuild.conditions.blocks.BlockStateCondition;
import rh.preventbuild.conditions.items.ItemDamageCondition;

public class ConditionRestrictions {
    private static boolean restrictItemDamage = false;
    private static boolean restrictAgeBlockState = false;
    private static boolean restrictBlockState = false;

    static boolean isAllowedCondition(ICondition condition, String value) {
        if (restrictItemDamage && condition instanceof ItemDamageCondition) return false;
        if (condition instanceof BlockStateCondition) {
            if (restrictAgeBlockState) return !value.contains("age=");
            return !restrictBlockState;
        }
        return true;
    }

    public static void setRestrictItemDamage() {
        restrictItemDamage = true;
    }

    public static void setRestrictAgeBlockState() {
        restrictAgeBlockState = true;
    }

    public static void setRestrictBlockState() {
        restrictBlockState = true;
    }

    public static void resetRestrictions() {
        restrictItemDamage = false;
        restrictAgeBlockState = false;
        restrictBlockState = false;
    }
}
