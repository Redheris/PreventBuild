package dev.redheris.preventbuild.conditions.impl.world;

import dev.redheris.preventbuild.conditions.ConditionBase;
import dev.redheris.preventbuild.conditions.ConditionResult;
import dev.redheris.preventbuild.conditions.util.IdentifierTagWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BiomeCondition extends ConditionBase {
    private final List<IdentifierTagWrapper<Biome>> biomeIds;

    public BiomeCondition(List<IdentifierTagWrapper<Biome>> biomeIds) {
        this.biomeIds = biomeIds;

    }

    private ConditionResult checkBiome(Level level, Biome biome) {
        var registry = level.registryAccess().lookupOrThrow(Registries.BIOME);
        Identifier biomeId = registry.getKey(biome);

        for (IdentifierTagWrapper<Biome> wrapper : biomeIds) {
            if (wrapper.isIn(biome, biomeId, level.registryAccess())) {
                return ConditionResult.CANCEL;
            }
        }
        return ConditionResult.PASS;
    }

    @Override
    public ConditionResult checkAttackAny(Player player, Level level, HitResult hitResult) {
        Biome biome = level.getBiome(BlockPos.containing(hitResult.getLocation())).value();
        return checkBiome(level, biome);
    }

    @Override
    public ConditionResult checkAttackBlock(Player player, Level level, BlockHitResult hitResult) {
        Biome biome = level.getBiome(hitResult.getBlockPos()).value();
        return checkBiome(level, biome);
    }

    @Override
    public ConditionResult checkAttackEntity(Player player, Level level, EntityHitResult hitResult) {
        Biome biome = level.getBiome(hitResult.getEntity().blockPosition()).value();
        return checkBiome(level, biome);
    }

    @Override
    public ConditionResult checkInteractAny(Player player, Level level, InteractionHand hand, @Nullable HitResult hitResult) {
        if (hitResult == null) return ConditionResult.PASS;
        Biome biome = level.getBiome(BlockPos.containing(hitResult.getLocation())).value();
        return checkBiome(level, biome);
    }

    @Override
    public ConditionResult checkInteractBlock(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        Biome biome = level.getBiome(hitResult.getBlockPos()).value();
        return checkBiome(level, biome);
    }

    @Override
    public ConditionResult checkInteractEntity(Player player, Level level, InteractionHand hand, EntityHitResult hitResult) {
        Biome biome = level.getBiome(hitResult.getEntity().blockPosition()).value();
        return checkBiome(level, biome);
    }

    @Override
    public ConditionResult checkUseItem(Player player, Level level, InteractionHand hand, @Nullable HitResult hitResult) {
        Biome biome = level.getBiome(player.blockPosition()).value();
        return checkBiome(level, biome);
    }

    @Override
    public ConditionResult checkPlaceBlock(BlockPlaceContext placeContext) {
        Biome biome = placeContext.getLevel().getBiome(placeContext.getClickedPos()).value();
        return checkBiome(placeContext.getLevel(), biome);
    }
}
