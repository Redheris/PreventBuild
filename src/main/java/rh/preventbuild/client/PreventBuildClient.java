package rh.preventbuild.client;


import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.CarpetBlock;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;
import rh.preventbuild.BlockingLists;
import rh.preventbuild.PreventBuild;
import rh.preventbuild.PreventBuildConfig;

@Environment(EnvType.CLIENT)
public class PreventBuildClient implements ClientModInitializer {

    private static KeyBinding keyBind_openConfigScreen;
    private static KeyBinding keyBind_toggleMod;
    private static KeyBinding keyBind_addCurrentBreakY;
    private static KeyBinding keyBind_addCurrentPlaceY;

    @Override
    public void onInitializeClient() {

        keyBind_openConfigScreen = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.preventbuild.openConfigScreen", // Ключ перевода имени привязки ключей
                InputUtil.Type.KEYSYM, // Тип привязки клавиш, KEYSYM для клавиатуры, MOUSE для мыши.
                GLFW.GLFW_KEY_V, // Ключевой код ключа
                "category.preventbuild" // Ключ перевода категории привязки ключей.
        ));

        keyBind_toggleMod = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.preventbuild.toggleMod", // Ключ перевода имени привязки ключей
                InputUtil.Type.KEYSYM, // Тип привязки клавиш, KEYSYM для клавиатуры, MOUSE для мыши.
                GLFW.GLFW_KEY_R, // Ключевой код ключа
                "category.preventbuild" // Ключ перевода категории привязки ключей.
        ));

        keyBind_addCurrentBreakY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.preventbuild.addCurrentBreakY", // Ключ перевода имени привязки ключей
                InputUtil.Type.KEYSYM, // Тип привязки клавиш, KEYSYM для клавиатуры, MOUSE для мыши.
                InputUtil.UNKNOWN_KEY.getCode(), // Ключевой код ключа
                "category.preventbuild" // Ключ перевода категории привязки ключей.
        ));

        keyBind_addCurrentPlaceY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.preventbuild.addCurrentPlaceY", // Ключ перевода имени привязки ключей
                InputUtil.Type.KEYSYM, // Тип привязки клавиш, KEYSYM для клавиатуры, MOUSE для мыши.
                InputUtil.UNKNOWN_KEY.getCode(), // Ключевой код ключа
                "category.preventbuild" // Ключ перевода категории привязки ключей.
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null) {

                while (keyBind_openConfigScreen.wasPressed()) {
                    client.setScreen(AutoConfig.getConfigScreen(PreventBuildConfig.class, client.currentScreen).get());
                }

                while (keyBind_toggleMod.wasPressed()) {
                    PreventBuild.config.enabled = !PreventBuild.config.enabled;
                    AutoConfig.getConfigHolder(PreventBuildConfig.class).getConfig().enabled = PreventBuild.config.enabled;
                    AutoConfig.getConfigHolder(PreventBuildConfig.class).save();
                    client.inGameHud.getChatHud().addMessage(Text.translatable("preventbuild.togglemod." + PreventBuild.config.enabled));
                }

                while (keyBind_addCurrentBreakY.wasPressed()) {
                    int y = client.player.getBlockY();

                    if (BlockingLists.getBreakY().contains(y))
                        break;

                    if(!PreventBuild.config.breakY.isEmpty())
                        PreventBuild.config.breakY += ",";
                    PreventBuild.config.breakY += y;
                    AutoConfig.getConfigHolder(PreventBuildConfig.class).save();
//                    System.out.println(BlockingLists.getBreakY());
                    client.inGameHud.getChatHud().addMessage(Text.translatable("preventbuild.add_to_break_list", y));
                }

                while (keyBind_addCurrentPlaceY.wasPressed()) {
                    int y = client.player.getBlockY();

                    if (BlockingLists.getPlaceY().contains(y))
                        break;

                    if(!PreventBuild.config.placeY.isEmpty())
                        PreventBuild.config.placeY += ",";
                    PreventBuild.config.placeY += y;

                    AutoConfig.getConfigHolder(PreventBuildConfig.class).save();
//                    System.out.println(BlockingLists.getPlaceY());
                    client.inGameHud.getChatHud().addMessage(Text.translatable("preventbuild.add_to_place_list", y));

                }
            }
        });







        UseBlockCallback.EVENT.register((PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) ->
        {
            if (PreventBuild.config.enabled) {
                Item usedItem = player.getStackInHand(hand).getItem();
                Block used = Block.getBlockFromItem(usedItem);
                Block hitedBlock = world.getBlockState(hitResult.getBlockPos()).getBlock();

                if (PreventBuild.config.blockStripping && usedItem instanceof AxeItem)
                    return ActionResult.FAIL;

                if (PreventBuild.config.blockCarpets && used instanceof CarpetBlock
                        && hitedBlock instanceof CarpetBlock)
                    return ActionResult.FAIL;

                if (PreventBuild.config.doBlockPlaceY && BlockingLists.getPlaceY().contains(getPlacingY(hitResult))
                    && (hand == Hand.MAIN_HAND && player.getStackInHand(Hand.MAIN_HAND).getItem() instanceof BlockItem
                        || hand == Hand.OFF_HAND  && player.getStackInHand(Hand.OFF_HAND).getItem() instanceof BlockItem))
                    return ActionResult.FAIL;
            }
            return ActionResult.PASS;
        });

        AttackBlockCallback.EVENT.register((PlayerEntity player, World world, Hand hand, BlockPos pos, Direction direction) ->
        {
            if (PreventBuild.config.enabled) {
                if (PreventBuild.config.doBlockBreakY && BlockingLists.getBreakY().contains(pos.getY()))
                    return ActionResult.FAIL;
                if (PreventBuild.config.blockBreakBlocks
                        && BlockingLists.getBreakBlocks().contains(world.getBlockState(pos).getBlock().getTranslationKey()))
                    return ActionResult.FAIL;
            }
            return ActionResult.PASS;
        });
    }
    private int getPlacingY(BlockHitResult hitResult){
        return switch (hitResult.getSide()) {
            case UP -> hitResult.getBlockPos().getY() + 1;
            case DOWN -> hitResult.getBlockPos().getY() - 1;
            default -> hitResult.getBlockPos().getY();
        };
    }
}
/* TODO:
    - Two events for preventing placing and breaking blocks
    - Settings' Menu with setting up preventing mods and lists of blocks (including oredicts I hope) which will checking events
    - Modes of preventing:
        - "Place block A by RMB on block B"
        - "Break block A"                           +
        - "Break block A under block B"
        - "Place on levels A,B,..."                 +
        - "Break on levels A,B,..."                 +
        - "Place/Break on levels between A and B"
        - Stripping any logs and wood               +
    - Extra future plan:
        - Create a visual UI for configuration preventing conditions
        - Add an ability to save configurations to switch between them and share with other players(clients)
 */