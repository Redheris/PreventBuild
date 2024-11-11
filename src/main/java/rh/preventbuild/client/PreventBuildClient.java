package rh.preventbuild.client;


import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;
import rh.preventbuild.PreventBuild;
import rh.preventbuild.conditions.ConditionCategory;
import rh.preventbuild.conditions.entities.IEntityCondition;

@Environment(EnvType.CLIENT)
public class PreventBuildClient implements ClientModInitializer {

    private static KeyBinding keyBind_openConfigScreen;
    private static KeyBinding keyBind_toggleMod;
    private static KeyBinding keyBind_addCurrentBreakY;
    private static KeyBinding keyBind_addCurrentPlaceY;
    private static KeyBinding keyBind_testConfig;

    @Override
    public void onInitializeClient() {

        keyBind_openConfigScreen = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.preventbuild.openConfigScreen", // Ключ перевода имени привязки ключей
                InputUtil.Type.KEYSYM, // Тип привязки клавиш, KEYSYM для клавиатуры, MOUSE для мыши.
                GLFW.GLFW_KEY_V, // Ключевой код ключа
                "category.preventbuild" // Ключ перевода категории привязки ключей.
        ));

        keyBind_toggleMod = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.preventbuild.toggleMod",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "category.preventbuild"
        ));

        keyBind_addCurrentBreakY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.preventbuild.addCurrentBreakY",
                InputUtil.Type.KEYSYM,
                InputUtil.UNKNOWN_KEY.getCode(),
                "category.preventbuild"
        ));

        keyBind_addCurrentPlaceY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.preventbuild.addCurrentPlaceY",
                InputUtil.Type.KEYSYM,
                InputUtil.UNKNOWN_KEY.getCode(),
                "category.preventbuild"
        ));

        keyBind_testConfig = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.preventbuild.testConfig",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_C,
                "category.preventbuild"
        ));


        UseBlockCallback.EVENT.register((PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) ->
        {
            if (PreventBuild.config != null && PreventBuild.config.isEnabled()) {
                BlockPos pos = hitResult.getBlockPos().offset(hitResult.getSide());

                if (PreventBuild.config.getCondition(ConditionCategory.PLACE).check(player, hand, pos.getX(), pos.getY(), pos.getZ(), hitResult)
                        || PreventBuild.config.getCondition(ConditionCategory.OTHER).check(player, hand, pos.getX(), pos.getY(), pos.getZ(), hitResult))
                    return ActionResult.FAIL;
            }
            return ActionResult.PASS;
        });

        AttackBlockCallback.EVENT.register((PlayerEntity player, World world, Hand hand, BlockPos pos, Direction direction) ->
        {
            if (PreventBuild.config != null && PreventBuild.config.isEnabled()) {
                if (PreventBuild.config.getCondition(ConditionCategory.BREAK).check(player, hand, pos.getX(), pos.getY(), pos.getZ())
                        || PreventBuild.config.getCondition(ConditionCategory.OTHER).check(player, hand, pos.getX(), pos.getY(), pos.getZ()))
                    return ActionResult.FAIL;
            }
            return ActionResult.PASS;
        });

        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (PreventBuild.config != null && PreventBuild.config.isEnabled()) {
                return ((IEntityCondition)(PreventBuild.config.getCondition(ConditionCategory.INTERACT_ENTITY)))
                        .check(ConditionCategory.INTERACT_ENTITY, player, world, hand, entity, hitResult);
            }
            return ActionResult.PASS;
        });

        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (PreventBuild.config != null && PreventBuild.config.isEnabled()) {
                return ((IEntityCondition)(PreventBuild.config.getCondition(ConditionCategory.INTERACT_ENTITY)))
                        .check(ConditionCategory.ATTACK_ENTITY, player, world, hand, entity, hitResult);
            }
            return ActionResult.PASS;
        });
    }
}
/* TODO:
    - Ore dictionaries
    - Multi configs
    - New conditions:
        - "Place block A by RMB on block B"
        - "Item"
    -
    - Extra future plan:
        - Create a visual UI for configuration preventing conditions
        - Add an ability to save configurations to switch between them and share with other players(clients)    +
 */