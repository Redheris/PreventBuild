package dev.redheris.preventbuild.conditions.impl.blocks;

import dev.redheris.preventbuild.conditions.ConditionResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class BlockStateCondition extends BlockConditionBase {
    private final String name;
    private final List<String> values;

    public BlockStateCondition(String name, List<String> values) {
        this.name = name;
        this.values = values;
    }

    protected ConditionResult checkBlock(Level level, BlockPos blockPos) {
        BlockState blockState = level.getBlockState(blockPos);

        boolean check = blockState.getProperties().stream().anyMatch(p ->
                p.getName().equals(name) && values.contains(blockState.getValue(p).toString()));

        return check ? ConditionResult.CANCEL : ConditionResult.PASS;
    }
}
