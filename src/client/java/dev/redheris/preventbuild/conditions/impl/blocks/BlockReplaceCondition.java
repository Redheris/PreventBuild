package dev.redheris.preventbuild.conditions.impl.blocks;

import dev.redheris.preventbuild.conditions.ConditionBase;
import dev.redheris.preventbuild.conditions.ConditionResult;
import net.minecraft.world.item.context.BlockPlaceContext;

public class BlockReplaceCondition extends ConditionBase {
    private final boolean replace;

    public BlockReplaceCondition(boolean replace) {
        this.replace = replace;
    }

    @Override
    public ConditionResult checkPlaceBlock(BlockPlaceContext placeContext) {
        return placeContext.replacingClickedOnBlock() == replace ? ConditionResult.CANCEL : ConditionResult.PASS;
    }
}
