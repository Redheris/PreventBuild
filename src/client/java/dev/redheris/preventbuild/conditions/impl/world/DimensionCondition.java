package dev.redheris.preventbuild.conditions.impl.world;

import dev.redheris.preventbuild.conditions.ConditionBase;
import dev.redheris.preventbuild.conditions.ConditionResult;
import dev.redheris.preventbuild.conditions.util.IdentifierTagWrapper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

import java.util.List;

public class DimensionCondition extends ConditionBase {
    private final List<IdentifierTagWrapper<DimensionType>> dimensionIds;

    public DimensionCondition(List<IdentifierTagWrapper<DimensionType>> dimensionIds) {
        this.dimensionIds = dimensionIds;
    }

    @Override
    public ConditionResult checkDefault(Player player, Level level) {
        var registry = level.registryAccess().lookupOrThrow(Registries.DIMENSION_TYPE);
        Identifier dimensionId = registry.getKey(level.dimensionType());

        for (IdentifierTagWrapper<DimensionType> wrapper : dimensionIds) {
            if (wrapper.isIn(level.dimensionType(), dimensionId, level.registryAccess())) {
                return ConditionResult.CANCEL;
            }
        }
        return ConditionResult.PASS;
    }
}
