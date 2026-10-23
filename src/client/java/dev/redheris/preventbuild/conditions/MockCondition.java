package dev.redheris.preventbuild.conditions;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class MockCondition extends ConditionBase {
    private final Object content;

    public MockCondition(Object content) {
        this.content = content;
    }

    @Override
    public ConditionResult checkAttackBlock(Player player, Level level, BlockHitResult hitResult) {
        return player.isCrouching() ? ConditionResult.PASS : ConditionResult.CANCEL;
    }

    @Override
    public ConditionResult checkPlace(BlockPlaceContext placeContext) {
        if (true) return ConditionResult.PASS;
        return placeContext.getClickedPos().getY() == -58 ? ConditionResult.PASS : ConditionResult.CANCEL;
    }

    @Override
    public ConditionResult checkUseItem(Player player, Level level, InteractionHand hand, @Nullable BlockHitResult hitResult) {
//        if (true) return ConditionResult.PASS;
        if (hitResult == null) return ConditionResult.PASS;
//        return hitResult.getBlockPos().getY() == -58 ? ConditionResult.PASS : ConditionResult.CANCEL;
        return ConditionResult.PASS;
    }
}
