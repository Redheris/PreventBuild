package rh.preventbuild.packet;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import rh.preventbuild.PreventBuildConfig;
import rh.preventbuild.api.ConditionRestrictions;

import static rh.preventbuild.packet.RestrictionSignalPayload.ID;

public class RestrictionMessageHandler {
    public static void register() {
        PayloadTypeRegistry.playS2C().register(ID, RestrictionSignalPayload.CODEC);
        ClientPlayNetworking.registerGlobalReceiver(
                ID,
                (payload, context) ->{
                    String msg = payload.value();

                    if (msg.trim().equals("ResetAll")) {
                        ConditionRestrictions.resetRestrictions();
                        PreventBuildConfig.loadConfigs();
                        return;
                    }

                    boolean hasServerMessage = false;
                    if (msg.contains("ResetAll")) {
                        ConditionRestrictions.resetRestrictions();
                        hasServerMessage = true;
                    }
                    if (msg.contains("NoItemDamage")) {
                        ConditionRestrictions.setRestrictItemDamage();
                        hasServerMessage = true;
                    }
                    if (msg.contains("NoAgeState")) {
                        ConditionRestrictions.setRestrictAgeBlockState();
                        hasServerMessage = true;
                    }
                    if (msg.contains("NoStateCondition")) {
                        ConditionRestrictions.setRestrictBlockState();
                        hasServerMessage = true;
                    }

                    if (hasServerMessage && PreventBuildConfig.getExceptionMessage() == null) {
                        PreventBuildConfig.loadConfigs();
                    }
                }
        );
    }
}
