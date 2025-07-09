package rh.preventbuild.api;

import rh.preventbuild.conditions.ConditionCategory;
import rh.preventbuild.conditions.ICondition;

@FunctionalInterface
public interface ConditionFactory {
    ICondition parse(ConditionCategory category, String value);
}
