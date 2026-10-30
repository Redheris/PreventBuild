package dev.redheris.preventbuild.conditions.impl.world;

import dev.redheris.preventbuild.conditions.ConditionBase;
import dev.redheris.preventbuild.conditions.ConditionResult;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

//? if >=26.2
//import net.minecraft.client.server.IntegratedServer;

public class IsSingleplayerCondition extends ConditionBase {
    private final boolean isSingleplayer;

    public IsSingleplayerCondition(boolean isSingleplayer) {
        this.isSingleplayer = isSingleplayer;
    }

    @Override
    public ConditionResult checkDefault(Player player, Level level) {
        //? if <26.2 {
        return Minecraft.getInstance().isSingleplayer() == isSingleplayer ? ConditionResult.CANCEL : ConditionResult.PASS;
        //?} else {
        /*IntegratedServer integratedServer = Minecraft.getInstance().getSingleplayerServer();
        boolean singleplayer = integratedServer != null && !integratedServer.isPublished();
        return singleplayer == isSingleplayer ? ConditionResult.CANCEL : ConditionResult.PASS;
        *///?}
    }
}
