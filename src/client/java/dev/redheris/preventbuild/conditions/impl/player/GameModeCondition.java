package dev.redheris.preventbuild.conditions.impl.player;

import dev.redheris.preventbuild.conditions.ConditionBase;
import dev.redheris.preventbuild.conditions.ConditionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;

//? if <=1.21.4
//import net.minecraft.client.Minecraft;

public class GameModeCondition extends ConditionBase {
    private final GameType gameMode;

    public GameModeCondition(GameType gameMode) {
        this.gameMode = gameMode;
    }

    @Override
    public ConditionResult checkDefault(Player player, Level level) {
        //? if >1.21.4 {
        return player.gameMode() == gameMode ? ConditionResult.CANCEL : ConditionResult.PASS;
        //?} else {
        /*GameType playerGameMode = Minecraft.getInstance().gameMode.getPlayerMode();
        return playerGameMode == gameMode ? ConditionResult.CANCEL : ConditionResult.PASS;
        *///?}
    }
}
