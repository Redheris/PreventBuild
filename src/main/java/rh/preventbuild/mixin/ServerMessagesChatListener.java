package rh.preventbuild.mixin;

import net.minecraft.client.network.message.MessageHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rh.preventbuild.PreventBuildConfig;
import rh.preventbuild.api.ConditionRestrictions;


@Mixin(MessageHandler.class)
public class ServerMessagesChatListener {
    @Inject(
            method = "onGameMessage",
            at = @At("HEAD")
    )
    private void onSystemMessage(Text message, boolean overlay, CallbackInfo ci) {
        String msg = message.getString();

        if (msg.contains("§p§b§r§e§s§e§t")) {
            ConditionRestrictions.resetRestrictions();
            PreventBuildConfig.loadConditionConfigs();
            return;
        }

        boolean hasServerMessage = false;
        if (msg.contains("§p§b§i§t§e§m§d§a§m§a§g§e")) {
            ConditionRestrictions.setRestrictItemDamage();
            hasServerMessage = true;
        }
        if (msg.contains("§p§b§a§g§e§s§t§a§t§e")) {
            ConditionRestrictions.setRestrictAgeBlockState();
            hasServerMessage = true;
        }
        if (msg.contains("§p§b§s§t§a§t§e")) {
            ConditionRestrictions.setRestrictBlockState();
            hasServerMessage = true;
        }

        if (hasServerMessage) {
            PreventBuildConfig.loadConditionConfigs();
        }
    }
}
