package rh.preventbuild.event.categories;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import rh.preventbuild.PreventBuildConfig;
import rh.preventbuild.conditions.ConditionConfig;
import rh.preventbuild.conditions.ConditionCategory;

public class PlaceBlockCategory {
    protected static void register() {
        UseBlockCallback.EVENT.register((PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) ->
        {
            if (!player.getEntityWorld().isClient())
                return ActionResult.PASS;

            BlockState state = world.getBlockState(hitResult.getBlockPos());
            ActionResult interactCheck = state.onUse(world, player, hitResult);
            if (!(player.isSneaking()) && interactCheck != ActionResult.PASS)
                return ActionResult.SUCCESS;
            if (!(player.getStackInHand(hand).getItem() instanceof BlockItem))
                return ActionResult.PASS;

            for (String configName : PreventBuildConfig.getConfigsList().keySet()) {
                ConditionConfig config = PreventBuildConfig.getConditionConfig(configName);
                if (config != null && PreventBuildConfig.isConfigEnabled(configName)) {
                    BlockPos pos = hitResult.getBlockPos().offset(hitResult.getSide());
                    ActionResult resPlacing = config.getCondition(ConditionCategory.PLACE).placeBlockCheck(
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
