package dev.redheris.preventbuild.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.redheris.preventbuild.PreventBuildClient;
import dev.redheris.preventbuild.conditions.Condition;
import dev.redheris.preventbuild.conditions.ConditionResult;
import dev.redheris.preventbuild.conditions.context.NoReplaceBlockPlaceContext;
import dev.redheris.preventbuild.conditions.context.UseIntoBlockContext;
import dev.redheris.preventbuild.config.conditions.ConditionConfig;
import dev.redheris.preventbuild.config.conditions.ConditionsManager;
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
import net.minecraft.world.phys.EntityHitResult;
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
    @Unique
    private final ConditionsManager conditionsManager = PreventBuildClient.conditionsManager;

    @Shadow
    public LocalPlayer player;

    @Shadow
    public ClientLevel level;

    @Shadow
    @Nullable
    public HitResult hitResult;

    @Inject(
            method = "startAttack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;get(Lnet/minecraft/core/component/DataComponentType;)Ljava/lang/Object;",
                    ordinal = 0
            ),
            cancellable = true
    )
    public void preventAttackAny(CallbackInfoReturnable<Boolean> cir) {
        // attackAny category
        for (ConditionConfig config : conditionsManager.getConfigs()) {
            if (!config.isActive()) continue;
            Condition attackAnyCondition = config.getAttackAny();
            if (attackAnyCondition == null) continue;

            ConditionResult check = attackAnyCondition.checkAttackAny(player, level, hitResult);

            if (check == ConditionResult.CANCEL) {
                cir.setReturnValue(false);
            }
        }
    }

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
            method = "startAttack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;attack(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/Entity;)V"
            ),
            cancellable = true
    )
    public void preventAttackEntity(CallbackInfoReturnable<Boolean> cir) {
        // attackEntity category
        for (ConditionConfig config : PreventBuildClient.conditionsManager.getConfigs()) {
            if (!config.isActive()) continue;
            Condition attackEntityCondition = config.getAttackEntity();
            if (attackEntityCondition == null) continue;

            ConditionResult check = attackEntityCondition.checkAttackEntity(
                    player, level, (EntityHitResult) hitResult
            );

            if (check == ConditionResult.CANCEL) {
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(
            method = "startUseItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;getItemInHand(Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;"
            ),
            cancellable = true
    )
    public void preventInteractAny(CallbackInfo ci, @Local InteractionHand hand) {
        if (pickItemFromBlockState.isWaitingSlotChange()) {
            // Pick item by block packet can change the selected slot on the server side before the client
            // will be notified. It leads to a problem when client sends a useItemOn packet with
            // different context from the server-side data.
            // Usually it takes fractions of a second (I got 5-60 ms in my tests with ~40-60 ms ping)
            // TODO: Make opt-out
            ci.cancel();
        }

        // interactAny category
        for (ConditionConfig config : conditionsManager.getConfigs()) {
            if (!config.isActive()) continue;
            Condition interactAnyCondition = config.getInteractAny();
            if (interactAnyCondition == null) continue;

            ConditionResult check = interactAnyCondition.checkInteractAny(player, level, hand, hitResult);

            if (check == ConditionResult.CANCEL) {
                ci.cancel();
            }
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
    public void preventInteractBlock(CallbackInfo ci, @Local InteractionHand hand, @Local BlockHitResult blockHitResult) {
        for (ConditionConfig config : conditionsManager.getConfigs()) {
            if (!config.isActive()) continue;

            // TODO: Think about adding and implementing this check.
            //  I believe this could potentially interfere with some conditions
            // Built-in interaction suppression check from ServerPlayerGameMode#useItemOn.
            // There's a tiny chance that the server could have modified it, so I believe it should be opt-out
//            boolean haveSomethingInOurHands = !player.getMainHandItem().isEmpty() || !player.getOffhandItem().isEmpty();
//            boolean suppressUsingBlock = player.isSecondaryUseActive() && haveSomethingInOurHands;
//            if (suppressUsingBlock) return;

            ConditionResult check = ConditionResult.PASS;

            Condition interactBlockCondition = config.getInteractBlock();
            if (interactBlockCondition != null) {
                // interactBlock category
                check = interactBlockCondition.checkInteractBlock(player, level, hand, blockHitResult);
            }

            Condition placeBlockCondition = config.getPlaceBlock();
            if (check == ConditionResult.PASS && placeBlockCondition != null) {
                // placeBlock category
                check = preventPlaceBlock(placeBlockCondition, hand, blockHitResult);
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
                    target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;interactAt(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/EntityHitResult;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;"
            ),
            cancellable = true
    )
    public void preventInteractEntity(CallbackInfo ci, @Local InteractionHand hand, @Local EntityHitResult entityHitResult) {
        // interactEntity category
        for (ConditionConfig config : PreventBuildClient.conditionsManager.getConfigs()) {
            if (!config.isActive()) continue;
            Condition interactEntityCondition = config.getInteractEntity();
            if (interactEntityCondition == null) continue;

            ConditionResult check = interactEntityCondition.checkInteractEntity(player, level, hand, entityHitResult);

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
    public void preventUseItem(CallbackInfo ci, @Local InteractionHand hand) {
        for (ConditionConfig config : conditionsManager.getConfigs()) {
            if (!config.isActive()) continue;
            Condition useItemCondition = config.getUseItem();
            if (useItemCondition == null) continue;

            ConditionResult check = useItemCondition.checkUseItem(player, level, hand);

            if (check == ConditionResult.CANCEL) {
                ci.cancel();
            }
        }
    }

    @Unique
    private boolean preventAttackBlock(BlockHitResult blockHitResult) {
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

    @Unique
    private ConditionResult preventPlaceBlock(Condition placeBlockCondition, InteractionHand hand, BlockHitResult blockHitResult) {
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

        return placeBlockCondition.checkPlaceBlock(placeContext);
    }
}
