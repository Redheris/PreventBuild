package dev.redheris.preventbuild.conditions.impl.player;

import dev.redheris.preventbuild.conditions.ConditionBase;
import dev.redheris.preventbuild.conditions.ConditionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class IsCrouchingCondition extends ConditionBase {
    private final boolean isCrouching;

    public IsCrouchingCondition(boolean isCrouching) {
        this.isCrouching = isCrouching;
    }

    @Override
    public ConditionResult checkDefault(Player player, Level level) {
        return player.isCrouching() == isCrouching ? ConditionResult.CANCEL : ConditionResult.PASS;
    }
}
