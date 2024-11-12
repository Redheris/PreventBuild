package rh.preventbuild.conditions.entities;

import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import rh.preventbuild.conditions.ConditionCategory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClickThroughCondition implements IEntityCondition {

    private final int sneaking_mode;
    private static final List<Block> INTERACTION_BLOCKS;

    /**
     * @param sneaking_mode prevent: 0 = always, 1 = when isn't sneaking, 2 = when sneaking
     */
    public ClickThroughCondition(int sneaking_mode) {
        this.sneaking_mode = sneaking_mode;
    }

    @Override
    public ConditionCategory getCategory() {
        return ConditionCategory.INTERACT_ENTITY;
    }

    public ActionResult check(ConditionCategory category, PlayerEntity player, World world, Hand hand, Entity entity, EntityHitResult hitResult) {
        if (category != ConditionCategory.INTERACT_ENTITY)
            return ActionResult.PASS;
        if (sneaking_mode == 1 && player.isSneaking() || sneaking_mode == 2 && !player.isSneaking())
            return ActionResult.PASS;
        if (entity instanceof ItemFrameEntity) {
            Direction frameFacingMirror = entity.getHorizontalFacing().getOpposite();
            BlockPos pos = entity.getBlockPos().offset(frameFacingMirror);
            Block block = world.getBlockState(pos).getBlock();
            if (block instanceof ChestBlock || block instanceof CraftingTableBlock || block instanceof AbstractFurnaceBlock
                    || block instanceof ShulkerBoxBlock || INTERACTION_BLOCKS.contains(block)) {
                Vec3d chestPos = new Vec3d(pos.getX(), pos.getY(), pos.getZ());
                Direction hitDirection = entity.getHorizontalFacing();
                BlockHitResult blockHitResult = new BlockHitResult(chestPos, hitDirection, pos, false);
                assert MinecraftClient.getInstance().interactionManager != null;

                return MinecraftClient.getInstance().interactionManager.interactBlock((ClientPlayerEntity)player, hand, blockHitResult);
            }
        }
        return ActionResult.PASS;
    }
    static {
        INTERACTION_BLOCKS = new ArrayList<>(Arrays.asList(
                Blocks.ENDER_CHEST,
                Blocks.SHULKER_BOX,
                Blocks.BARREL,
                Blocks.DISPENSER,
                Blocks.DROPPER,
                Blocks.HOPPER
        ));
    }
}
