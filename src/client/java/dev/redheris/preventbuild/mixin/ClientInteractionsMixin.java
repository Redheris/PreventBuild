package dev.redheris.preventbuild.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.redheris.preventbuild.PreventBuildClient;
import dev.redheris.preventbuild.conditions.Condition;
import dev.redheris.preventbuild.conditions.ConditionResult;
import dev.redheris.preventbuild.conditions.context.NoReplaceBlockPlaceContext;
import dev.redheris.preventbuild.conditions.context.UseIntoBlockContext;
import dev.redheris.preventbuild.config.conditions.ConditionConfig;
import dev.redheris.preventbuild.state.PickItemFromBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class ClientInteractionsMixin {
    @Unique
    private final PickItemFromBlockState pickItemFromBlockState = PickItemFromBlockState.getInstance();

    @Shadow
    @Nullable
    public LocalPlayer player;

    @Shadow
    @Nullable
    public ClientLevel level;

    @Shadow
    @Nullable
    public HitResult hitResult;

    @Inject(
            method = "startAttack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/multiplayer/ClientLevel;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;",
                    ordinal = 0
            ),
            cancellable = true
    )
    public void onStartAttackBlock(CallbackInfoReturnable<Boolean> cir, @Local BlockHitResult blockHitResult) {
        // attackBlock category
        if (preventAttackBlock(blockHitResult)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(
            method = "continueAttack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;continueDestroyBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Z",
                    ordinal = 0
            ),
            cancellable = true
    )
    public void onContinueAttackBlock(boolean leftClick, CallbackInfo ci, @Local BlockHitResult blockHitResult) {
        // attackBlock category
        if (preventAttackBlock(blockHitResult)) {
            ci.cancel();
        }
    }

    @Inject(
            method = "startUseItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;useItemOn(Lnet/minecraft/client/player/LocalPlayer;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/phys/BlockHitResult;)Lnet/minecraft/world/InteractionResult;"
            ),
            cancellable = true
    )
    public void preventUseBlockAndItemOn(CallbackInfo ci, @Local InteractionHand hand, @Local BlockHitResult blockHitResult) {
        if (player == null || level == null) return;

        if (pickItemFromBlockState.isWaitingSlotChange()) {
            // Pick item by block packet can change the selected slot on the server side before the client
            // will be notified. It leads to a problem when client sends a useItemOn packet with
            // different context from the server-side data.
            // Usually it takes fractions of a second (I got 5-60 ms in my tests with ~40-60 ms ping)
            // TODO: Make opt-out
            return;
        }

        for (ConditionConfig config : PreventBuildClient.conditionsManager.getConfigs()) {
            if (!config.isActive()) continue;

            ConditionResult check = ConditionResult.PASS;

            // placeBlock category
            Condition placeBlockCondition = config.getPlaceBlock();
            if (placeBlockCondition != null) {
                ItemStack itemStack = player.getItemInHand(hand);
                Item item = itemStack.getItem();
                var placeContext = new BlockPlaceContext(player, hand, player.getItemInHand(hand), blockHitResult);

                if (!(item instanceof BlockItem)) {
                    boolean useSamePos = true;

                    if (item instanceof BucketItem bucketItem && bucketItem.getContent() != Fluids.EMPTY) {
                        useSamePos = false;
                        BlockState blockState = level.getBlockState(blockHitResult.getBlockPos());

                        if (blockState.getBlock() instanceof LiquidBlockContainer liquidBlockContainer) {
                            useSamePos = !player.isShiftKeyDown() && liquidBlockContainer.canPlaceLiquid(
                                    player, level, blockHitResult.getBlockPos(), blockState, bucketItem.getContent()
                            );
                        }
                    }

                    if (useSamePos) {
                        placeContext = new UseIntoBlockContext(player, hand, itemStack, blockHitResult);
                    } else {
                        placeContext = new NoReplaceBlockPlaceContext(player, hand, itemStack, blockHitResult);
                    }
                }
                check = placeBlockCondition.checkPlace(placeContext);
            }

            // useItem category (with block hit result)
            Condition useItemCondition = config.getUseItem();
            if (check != ConditionResult.CANCEL && useItemCondition != null) {
                check = useItemCondition.checkUseItem(player, level, hand, blockHitResult);
            }

            if (check == ConditionResult.CANCEL) {
                ci.cancel();
            }
        }
    }

    @Inject(
            method = "startUseItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;useItem(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;"
            ),
            cancellable = true
    )
    public void onUseItem(CallbackInfo ci, @Local InteractionHand hand) {
        if (player == null || level == null) return;

        // useItem category (with missed hit)
        BlockHitResult missHitResult = hitResult == null ? null : (BlockHitResult) hitResult;
        for (ConditionConfig config : PreventBuildClient.conditionsManager.getConfigs()) {
            if (!config.isActive()) continue;
            Condition useItemCondition = config.getUseItem();
            if (useItemCondition == null) continue;

            ConditionResult check = useItemCondition.checkUseItem(player, level, hand, missHitResult);

            if (check == ConditionResult.CANCEL) {
                ci.cancel();
            }
        }
    }


    @Unique
    private boolean preventAttackBlock(BlockHitResult blockHitResult) {
        if (player == null || level == null) return false;

        for (ConditionConfig config : PreventBuildClient.conditionsManager.getConfigs()) {
            if (!config.isActive()) continue;
            Condition attackBlockCondition = config.getAttackBlock();
            if (attackBlockCondition == null) continue;

            ConditionResult check = attackBlockCondition.checkAttackBlock(player, level, blockHitResult);

            if (check == ConditionResult.CANCEL) {
                return true;
            }
        }
        return false;
    }
}
