package rh.preventbuild.conditions.blocks;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import rh.preventbuild.conditions.ConditionCategory;
import rh.preventbuild.conditions.ICondition;

import java.util.Arrays;

public class LookingAtBlockCondition implements ICondition {
    private final String[] blocks;

    public LookingAtBlockCondition(String[] blocks) {
        this.blocks = blocks;
    }

    @Override
    public ConditionCategory getCategory() {
        return ConditionCategory.MISCELLANEOUS;
    }

    @Override
    public ActionResult attackBlockCheck(PlayerEntity player, Hand hand, int x, int y, int z) {
        HitResult target = MinecraftClient.getInstance().crosshairTarget;
        assert target != null;
        if (target.getType() != HitResult.Type.BLOCK)
            return ActionResult.PASS;
        BlockHitResult targetBlock = (BlockHitResult) target;
        String blockName = player.getWorld().getBlockState(targetBlock.getBlockPos()).getBlock().getTranslationKey();
        if (Arrays.stream(this.blocks).anyMatch(i -> i.equalsIgnoreCase(blockName)))
            return ActionResult.FAIL;
        return ActionResult.PASS;
    }
}
