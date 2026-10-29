package dev.redheris.preventbuild.conditions.impl.world;

import dev.redheris.preventbuild.conditions.ConditionBase;
import dev.redheris.preventbuild.conditions.ConditionResult;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class IsSingleplayerCondition extends ConditionBase {
    private final boolean isSingleplayer;

    public IsSingleplayerCondition(boolean isSingleplayer) {
        this.isSingleplayer = isSingleplayer;
    }

    @Override
    public ConditionResult checkDefault(Player player, Level level) {
        return Minecraft.getInstance().isSingleplayer() == isSingleplayer ? ConditionResult.CANCEL : ConditionResult.PASS;
    }
}
