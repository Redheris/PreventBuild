package rh.preventbuild.conditions.advanced;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import rh.preventbuild.conditions.categories.ConditionCategory;
import rh.preventbuild.conditions.ICondition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AxeStrippingCondition implements ICondition {
    protected static final List<Block> STRIPPABLE_BLOCKS;
    private final String[] blacklist;

    public AxeStrippingCondition() {
        this.blacklist = null;
    }

    public AxeStrippingCondition(String[] blacklist) {
        this.blacklist = blacklist;
    }

    @Override
    public ConditionCategory getCategory() {
        return ConditionCategory.INTERACT_BLOCK;
    }

    @Override
    public ActionResult useItemCheck(PlayerEntity player, World world, Hand hand) {
        HitResult target = MinecraftClient.getInstance().crosshairTarget;
        assert target != null;
        if (target.getType() != HitResult.Type.BLOCK)
            return ActionResult.PASS;
        BlockHitResult targetBlock = (BlockHitResult) target;
        Block lookingAt = player.getWorld().getBlockState(targetBlock.getBlockPos()).getBlock();

        if (blacklist != null && Arrays.stream(blacklist).anyMatch(i -> i.equalsIgnoreCase(lookingAt.getTranslationKey()))) {
            return ActionResult.PASS;
        }
        if (player.getStackInHand(hand).getItem() instanceof AxeItem && STRIPPABLE_BLOCKS.contains(lookingAt))
            return ActionResult.FAIL;
        return ActionResult.PASS;
    }

    static {
        STRIPPABLE_BLOCKS = new ArrayList<>(Arrays.asList(
                Blocks.OAK_WOOD,
                Blocks.OAK_LOG,
                Blocks.DARK_OAK_WOOD,
                Blocks.DARK_OAK_LOG,
                Blocks.ACACIA_WOOD,
                Blocks.ACACIA_LOG,
                Blocks.CHERRY_WOOD,
                Blocks.CHERRY_LOG,
                Blocks.BIRCH_WOOD,
                Blocks.BIRCH_LOG,
                Blocks.JUNGLE_WOOD,
                Blocks.JUNGLE_LOG,
                Blocks.SPRUCE_WOOD,
                Blocks.SPRUCE_LOG,
                Blocks.WARPED_STEM,
                Blocks.WARPED_HYPHAE,
                Blocks.CRIMSON_STEM,
                Blocks.CRIMSON_HYPHAE,
                Blocks.MANGROVE_WOOD,
                Blocks.MANGROVE_LOG,
                Blocks.BAMBOO_BLOCK,
                Blocks.PALE_OAK_LOG,
                Blocks.PALE_OAK_WOOD
        ));
    }

    public static ICondition parse(ConditionCategory ignoredCategory, String value) {
        String[] values = value.split(",");
        for (int i = 0; i < values.length; i++)
            if (!values[i].contains("."))
                values[i] = "block.minecraft." + values[i];
        return new AxeStrippingCondition(values);
    }
}
