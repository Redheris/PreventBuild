package dev.redheris.preventbuild.mixin;

import dev.redheris.preventbuild.state.PickItemFromBlockState;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiPlayerGameMode.class)
public class PickItemFromBlockMixin {
    @Unique
    private final PickItemFromBlockState pickItemFromBlockState = PickItemFromBlockState.getInstance();

    @Inject(
            method = "handlePickItemFromBlock",
            at = @At("TAIL")
    )
    public void handlePickItemFromBlock(BlockPos pos, boolean includeData, CallbackInfo ci) {
        pickItemFromBlockState.setWaitingSlotChange(true);
    }
}
