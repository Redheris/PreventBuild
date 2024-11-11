package rh.preventbuild.conditions.advanced;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import rh.preventbuild.conditions.ConditionCategory;
import rh.preventbuild.conditions.ICondition;

public class DimensionCondition implements ICondition {
    private final String dimension_id;

    public DimensionCondition(String dimension_id) {
        if (dimension_id.contains(":"))
            this.dimension_id = dimension_id;
        else
            this.dimension_id = "minecraft:" + dimension_id;
    }

    @Override
    public ConditionCategory getCategory() {
        return ConditionCategory.OTHER;
    }

    @Override
    public boolean check(PlayerEntity player, Hand hand, int x, int y, int z) {
        if (player.getWorld().isClient)
            return player.getWorld().getDimensionKey().getValue().toString().equals(dimension_id);
        return false;
    }
}
