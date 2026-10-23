package dev.redheris.preventbuild.conditions;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

public interface Condition {
    ConditionResult checkDefault(Player player, Level level);

    ConditionResult checkAttackBlock(Player player, Level level, BlockHitResult hitResult);

    ConditionResult checkPlace(BlockPlaceContext placeContext);

    ConditionResult checkUseItem(Player player, Level level, InteractionHand hand, @Nullable HitResult hitResult);

    ConditionResult checkAttackEntity(Player player, Level level, EntityHitResult hitResult);

    ConditionResult checkInteractEntity(Player player, Level level, EntityHitResult hitResult);
}
