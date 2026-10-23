package dev.redheris.preventbuild.mixin;

import dev.redheris.preventbuild.state.PickItemFromBlockState;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundSetHeldSlotPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class HeldSlotChangeListenerMixin {
    @Unique
    private final PickItemFromBlockState pickItemFromBlockState = PickItemFromBlockState.getInstance();

    @Inject(
            method = "handleSetHeldSlot",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Inventory;setSelectedSlot(I)V",
                    shift = At.Shift.AFTER
            )
    )
    public void handleHeldSlotChange(ClientboundSetHeldSlotPacket packet, CallbackInfo ci) {
        pickItemFromBlockState.setWaitingSlotChange(false);
    }
}
