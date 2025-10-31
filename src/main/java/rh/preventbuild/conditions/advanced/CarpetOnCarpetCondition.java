package rh.preventbuild.conditions.advanced;

import net.minecraft.block.Block;
import net.minecraft.block.CarpetBlock;
import net.minecraft.block.PaleMossCarpetBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import rh.preventbuild.conditions.ConditionCategory;
import rh.preventbuild.conditions.ICondition;

public class CarpetOnCarpetCondition implements ICondition {
    private final ConditionCategory category;

    public CarpetOnCarpetCondition(ConditionCategory category) {
        this.category = category;
    }

    @Override
    public ConditionCategory getCategory() {
        return category;
    }

    @Override
    public ActionResult placeBlockCheck(PlayerEntity player, Hand hand, int x, int y, int z, BlockHitResult hitResult) {
        Item heldItem = player.getStackInHand(hand).getItem();
        if (heldItem instanceof BlockItem) {
            Block heldBlock = ((BlockItem) heldItem).getBlock();
            if (!(heldBlock instanceof CarpetBlock || heldBlock instanceof PaleMossCarpetBlock))
                return ActionResult.PASS;
        }
        Block block = player.getEntityWorld().getBlockState(new BlockPos(x, y - 1, z)).getBlock();
        if (block instanceof CarpetBlock || block instanceof PaleMossCarpetBlock)
            return ActionResult.FAIL;
        return ActionResult.PASS;
    }
}
