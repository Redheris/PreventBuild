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
    /// Checks the condition when used in the {@code attackAny} category
    ConditionResult checkAttackAny(Player player, Level level, HitResult hitResult);

    /// Checks the condition when used in the {@code attackAny} category
    ConditionResult checkAttackBlock(Player player, Level level, BlockHitResult hitResult);

    /// Checks the condition when used in the {@code attackEntity} category
    ConditionResult checkAttackEntity(Player player, Level level, EntityHitResult hitResult);

    /// Checks the condition when used in the {@code interactAny} category
    ConditionResult checkInteractAny(Player player, Level level, InteractionHand hand, @Nullable HitResult hitResult);

    /// Checks the condition when used in the {@code interactBlock} category
    ConditionResult checkInteractBlock(Player player, Level level, InteractionHand hand, BlockHitResult hitResult);

    /// Checks the condition when used in the {@code interactEntity} category
    ConditionResult checkInteractEntity(Player player, Level level, InteractionHand hand, EntityHitResult hitResult);

    /// Checks the condition when used in the {@code useItem} category
    ConditionResult checkUseItem(Player player, Level level, InteractionHand hand, @Nullable HitResult hitResult);

    /// Checks the condition when used in the {@code placeBlock} category
    ConditionResult checkPlaceBlock(BlockPlaceContext placeContext);
}
