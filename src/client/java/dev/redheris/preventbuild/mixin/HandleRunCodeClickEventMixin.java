package dev.redheris.preventbuild.mixin;

import dev.redheris.preventbuild.gui.RunCodeClickEvent;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

//? if >1.21.4 {
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.ClickEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//?} else {
/*import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
*///?}

@Mixin(Screen.class)
public class HandleRunCodeClickEventMixin {
    //? if >1.21.4 {
    @Inject(
            method = "defaultHandleClickEvent",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void handleRunCodeClickEvent(ClickEvent event, Minecraft minecraft, Screen activeScreen, CallbackInfo ci) {
        if (event instanceof RunCodeClickEvent runCode) {
            runCode.run();
            ci.cancel();
        }
    }
    //?} else {
    /*@Inject(
            method = "handleComponentClicked",
            at = @At("HEAD"),
            cancellable = true
    )
    private void handleRunCodeClickEvent(Style style, CallbackInfoReturnable<Boolean> cir) {
        if (style.getClickEvent() instanceof RunCodeClickEvent runCode) {
            runCode.run();
            cir.setReturnValue(true);
        }
    }
    *///?}
}
