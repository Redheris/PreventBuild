package rh.preventbuild.event.categories;

import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.util.ActionResult;
import rh.preventbuild.PreventBuildConfig;
import rh.preventbuild.conditions.ConditionConfig;
import rh.preventbuild.conditions.ICondition;
import rh.preventbuild.conditions.basic.NullCondition;
import rh.preventbuild.conditions.ConditionCategory;

public class InteractEntityCategory {
    protected static void register() {
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (!player.getWorld().isClient)
                return ActionResult.PASS;

            for (String configName : PreventBuildConfig.getConfigsList().keySet()) {
                ConditionConfig config = PreventBuildConfig.getConditionConfig(configName);
                if (config != null && PreventBuildConfig.isConfigEnabled(configName)) {
                    ICondition condition = config.getCondition(ConditionCategory.INTERACT_ENTITY);
                    if (!(condition instanceof NullCondition)) {
                        ActionResult res = condition.useEntityCheck(player, world, hand, entity, hitResult);
                        if (res != ActionResult.PASS)
                            return res;
                    }
                }
            }
            return ActionResult.PASS;
        });
    }
}
