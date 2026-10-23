package dev.redheris.preventbuild.conditions;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public abstract class ConditionBase implements Condition {

    @Override
    public ConditionResult checkDefault(Player player, Level level) {
        return ConditionResult.PASS;
    }

    @Override
    public ConditionResult checkAttackBlock(Player player, Level level, BlockHitResult hitResult) {
        return checkDefault(player, level);
    }

    @Override
    public ConditionResult checkPlace(BlockPlaceContext placeContext) {
        return checkDefault(placeContext.getPlayer(), placeContext.getLevel());
    }

    @Override
    public ConditionResult checkUseItem(Player player, Level level, InteractionHand hand, @Nullable BlockHitResult hitResult) {
        return checkDefault(player, level);
    }
}
