package dev.redheris.preventbuild.conditions.impl.logical;

import dev.redheris.preventbuild.conditions.Condition;
import dev.redheris.preventbuild.conditions.ConditionBase;
import dev.redheris.preventbuild.conditions.ConditionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AndCondition extends ConditionBase {
    private final List<Condition> nestedConditions;

    public AndCondition(List<Condition> nestedConditions) {
        this.nestedConditions = nestedConditions;
    }

    @Override
    public ConditionResult checkAttackAny(Player player, Level level, HitResult hitResult) {
        for (Condition condition : nestedConditions) {
            if (condition.checkAttackAny(player, level, hitResult) == ConditionResult.PASS) {
                return ConditionResult.PASS;
            }
        }
        return ConditionResult.CANCEL;
    }

    @Override
    public ConditionResult checkAttackBlock(Player player, Level level, BlockHitResult hitResult) {
        for (Condition condition : nestedConditions) {
            if (condition.checkAttackBlock(player, level, hitResult) == ConditionResult.PASS) {
                return ConditionResult.PASS;
            }
        }
        return ConditionResult.CANCEL;
    }

    @Override
    public ConditionResult checkAttackEntity(Player player, Level level, EntityHitResult hitResult) {
        for (Condition condition : nestedConditions) {
            if (condition.checkAttackEntity(player, level, hitResult) == ConditionResult.PASS) {
                return ConditionResult.PASS;
            }
        }
        return ConditionResult.CANCEL;
    }

    @Override
    public ConditionResult checkInteractAny(Player player, Level level, InteractionHand hand, @Nullable HitResult hitResult) {
        for (Condition condition : nestedConditions) {
            if (condition.checkInteractAny(player, level, hand, hitResult) == ConditionResult.PASS) {
                return ConditionResult.PASS;
            }
        }
        return ConditionResult.CANCEL;
    }

    @Override
    public ConditionResult checkInteractBlock(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        for (Condition condition : nestedConditions) {
            if (condition.checkInteractBlock(player, level, hand, hitResult) == ConditionResult.PASS) {
                return ConditionResult.PASS;
            }
        }
        return ConditionResult.CANCEL;
    }

    @Override
    public ConditionResult checkInteractEntity(Player player, Level level, InteractionHand hand, EntityHitResult hitResult) {
        for (Condition condition : nestedConditions) {
            if (condition.checkInteractEntity(player, level, hand, hitResult) == ConditionResult.PASS) {
                return ConditionResult.PASS;
            }
        }
        return ConditionResult.CANCEL;
    }

    @Override
    public ConditionResult checkUseItem(Player player, Level level, InteractionHand hand, @Nullable HitResult hitResult) {
        for (Condition condition : nestedConditions) {
            if (condition.checkUseItem(player, level, hand, hitResult) == ConditionResult.PASS) {
                return ConditionResult.PASS;
            }
        }
        return ConditionResult.CANCEL;
    }

    @Override
    public ConditionResult checkPlaceBlock(BlockPlaceContext placeContext) {
        for (Condition condition : nestedConditions) {
            if (condition.checkPlaceBlock(placeContext) == ConditionResult.PASS) {
                return ConditionResult.PASS;
            }
        }
        return ConditionResult.CANCEL;
    }
}
