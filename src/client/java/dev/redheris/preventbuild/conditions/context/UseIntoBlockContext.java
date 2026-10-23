package dev.redheris.preventbuild.conditions.context;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class UseIntoBlockContext extends BlockPlaceContext {
    public UseIntoBlockContext(Player player, InteractionHand hand, ItemStack itemStack, BlockHitResult hitResult) {
        super(player, hand, itemStack, hitResult);
    }

    @Override
    public @NotNull BlockPos getClickedPos() {
        return getHitResult().getBlockPos();
    }
}
