package rh.preventbuild.event.categories;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import rh.preventbuild.PreventBuildConfig;
import rh.preventbuild.conditions.ConditionConfig;
import rh.preventbuild.conditions.ConditionCategory;

public class InteractBlockCategory {
    protected static void register() {
        UseBlockCallback.EVENT.register((PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) ->
        {
            if (!player.getWorld().isClient)
                return ActionResult.PASS;

            for (String configName : PreventBuildConfig.getConfigsList().keySet()) {
                ConditionConfig config = PreventBuildConfig.getConditionConfig(configName);
                if (config != null && PreventBuildConfig.isConfigEnabled(configName)) {
                    BlockPos pos = hitResult.getBlockPos();
                    ActionResult resPlacing = config.getCondition(ConditionCategory.INTERACT_BLOCK).interactBlockCheck(
                            player, hand, pos.getX(), pos.getY(), pos.getZ(), hitResult
                    );
                    if (resPlacing != ActionResult.PASS)
                        return resPlacing;
                }
            }

            return ActionResult.PASS;
        });
    }
}
