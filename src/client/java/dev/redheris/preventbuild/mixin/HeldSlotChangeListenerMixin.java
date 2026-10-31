package dev.redheris.preventbuild.mixin;

import dev.redheris.preventbuild.state.PickItemFromBlockState;
import net.minecraft.client.multiplayer.ClientPacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? >1.21.1 {
import net.minecraft.network.protocol.game.ClientboundSetHeldSlotPacket;
//?} else
//import net.minecraft.network.protocol.game.ClientboundSetCarriedItemPacket;

@Mixin(ClientPacketListener.class)
public class HeldSlotChangeListenerMixin {
    @Unique
    private final PickItemFromBlockState pickItemFromBlockState = PickItemFromBlockState.getInstance();

    @Inject(
            //~ if >1.21.1 'handleSetCarriedItem' -> 'handleSetHeldSlot'
            method = "handleSetHeldSlot",
            at = @At(
                    value = "INVOKE",
                    //? if >1.21.4 {
                    target = "Lnet/minecraft/world/entity/player/Inventory;setSelectedSlot(I)V",
                    //?} else
                    //target = "Lnet/minecraft/client/player/LocalPlayer;getInventory()Lnet/minecraft/world/entity/player/Inventory;",
                    shift = At.Shift.AFTER
            )
    ) //~ if >1.21.1 'ClientboundSetCarriedItemPacket' -> 'ClientboundSetHeldSlotPacket'
    public void handleHeldSlotChange(ClientboundSetHeldSlotPacket packet, CallbackInfo ci) {
        pickItemFromBlockState.setWaitingSlotChange(false);
    }
}
