package rh.preventbuild.event.categories;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import rh.preventbuild.PreventBuildConfig;
import rh.preventbuild.conditions.ConditionConfig;
import rh.preventbuild.conditions.ConditionCategory;

public class UseItemCategory {
    protected static void register() {
        UseBlockCallback.EVENT.register((PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) ->
        {
            if (!player.getWorld().isClient)
                return ActionResult.PASS;

            BlockState state = world.getBlockState(hitResult.getBlockPos());
            ActionResult interactCheck = state.onUse(world, player, hitResult);
            if (!(player.isSneaking()) && interactCheck != ActionResult.PASS)
                return ActionResult.SUCCESS;

            for (String configName : PreventBuildConfig.getConfigsList().keySet()) {
                ConditionConfig config = PreventBuildConfig.getConditionConfig(configName);
                if (config != null && PreventBuildConfig.isConfigEnabled(configName)) {
                    ActionResult res = config.getCondition(ConditionCategory.USE_ITEM).useItemCheck(player, world, hand);
                    if (res != ActionResult.PASS)
                        return res;
                }
            }

            return ActionResult.PASS;
        });

        UseItemCallback.EVENT.register((player, world, hand) ->
        {
            if (!player.getWorld().isClient)
                return ActionResult.PASS;

            for (String configName : PreventBuildConfig.getConfigsList().keySet()) {
                ConditionConfig config = PreventBuildConfig.getConditionConfig(configName);
                if (config != null && PreventBuildConfig.isConfigEnabled(configName)) {
                    ActionResult res = config.getCondition(ConditionCategory.USE_ITEM).useItemCheck(player, world, hand);
                    if (res != ActionResult.PASS)
                        return res;
                }
            }
            return ActionResult.PASS;
        });
    }
}
