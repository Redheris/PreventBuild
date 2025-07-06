package rh.preventbuild.mixin;

import net.minecraft.client.network.message.MessageHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(MessageHandler.class)
public class ServerMessagesChatListener {
    @Inject(
            method = "onGameMessage",
            at = @At("HEAD")
    )
    private void onSystemMessage(Text message, boolean overlay, CallbackInfo ci) {
//        String msg = message.getString();
//        if (msg.contains("§p§b§_§n§o§d")) {
//            System.out.println("Получено системное сообщение");
//        }
    }
}
