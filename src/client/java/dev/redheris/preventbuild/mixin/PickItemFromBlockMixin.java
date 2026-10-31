package dev.redheris.preventbuild.mixin;

import dev.redheris.preventbuild.state.PickItemFromBlockState;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if >1.21.1
import net.minecraft.core.BlockPos;

@Mixin(MultiPlayerGameMode.class)
public class PickItemFromBlockMixin {
    @Unique
    private final PickItemFromBlockState pickItemFromBlockState = PickItemFromBlockState.getInstance();

    @Inject(
            //~ if >1.21.1 'handlePickItem' -> 'handlePickItemFromBlock'
            method = "handlePickItemFromBlock",
            at = @At("TAIL")
    ) //~ if >1.21.1 'int index, CallbackInfo ci' -> 'BlockPos pos, boolean includeData, CallbackInfo ci'
    public void handlePickItemFromBlock(BlockPos pos, boolean includeData, CallbackInfo ci) {
        pickItemFromBlockState.setWaitingSlotChange(true);
    }
}
