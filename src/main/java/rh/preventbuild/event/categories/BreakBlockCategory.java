package rh.preventbuild.event.categories;

import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import rh.preventbuild.PreventBuildConfig;
import rh.preventbuild.conditions.ConditionConfig;
import rh.preventbuild.conditions.ConditionCategory;

public class BreakBlockCategory {
    protected static void register() {
        AttackBlockCallback.EVENT.register((PlayerEntity player, World world, Hand hand, BlockPos pos, Direction direction) ->
        {
            if (!player.getWorld().isClient)
                return ActionResult.PASS;

            for (String configName : PreventBuildConfig.getConfigsList().keySet()) {
                ConditionConfig config = PreventBuildConfig.getConditionConfig(configName);
                if (config != null && PreventBuildConfig.isConfigEnabled(configName)) {
                    ActionResult resBreaking = config.getCondition(ConditionCategory.BREAK).attackBlockCheck(player, hand, pos.getX(), pos.getY(), pos.getZ());
                    if (resBreaking != ActionResult.PASS)
                        return resBreaking;
                }
            }
            return ActionResult.PASS;
        });
    }
}
