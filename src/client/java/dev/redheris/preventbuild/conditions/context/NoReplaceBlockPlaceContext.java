package dev.redheris.preventbuild.conditions.context;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.phys.BlockHitResult;

public class NoReplaceBlockPlaceContext extends BlockPlaceContext {
    public NoReplaceBlockPlaceContext(Player player, InteractionHand hand, ItemStack itemStack, BlockHitResult hitResult) {
        super(player, hand, itemStack, hitResult);
        this.replaceClicked = false;
    }
}
