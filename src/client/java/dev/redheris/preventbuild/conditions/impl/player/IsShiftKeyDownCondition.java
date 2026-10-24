package dev.redheris.preventbuild.conditions.impl.player;

import dev.redheris.preventbuild.conditions.ConditionBase;
import dev.redheris.preventbuild.conditions.ConditionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class IsShiftKeyDownCondition extends ConditionBase {
    private final boolean isShiftKeyDown;

    public IsShiftKeyDownCondition(boolean isShiftKeyDown) {
        this.isShiftKeyDown = isShiftKeyDown;
    }

    @Override
    public ConditionResult checkDefault(Player player, Level level) {
        return player.isShiftKeyDown() == isShiftKeyDown ? ConditionResult.CANCEL : ConditionResult.PASS;
    }
}
