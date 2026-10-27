package dev.redheris.preventbuild.mixin;

import dev.redheris.preventbuild.gui.RunCodeClickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.ClickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class HandleRunCodeClickEventMixin {
    @Inject(
            method = "defaultHandleClickEvent",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void handleRunCodeClickEvent(ClickEvent clickEvent, Minecraft minecraft, Screen screen, CallbackInfo ci) {
        if (clickEvent instanceof RunCodeClickEvent runCode) {
            runCode.run();
            ci.cancel();
        }
    }
}
