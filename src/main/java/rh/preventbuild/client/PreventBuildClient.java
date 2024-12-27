package rh.preventbuild.client;


import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;
import rh.preventbuild.PreventBuildConfig;
import rh.preventbuild.conditions.ConditionCategory;
import rh.preventbuild.conditions.ConditionConfig;
import rh.preventbuild.conditions.entities.IEntityCondition;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

@Environment(EnvType.CLIENT)
public class PreventBuildClient implements ClientModInitializer {

    private static KeyBinding keyBind_openConfigScreen;
    private static KeyBinding keyBind_toggleMod;

    public static ConditionConfig config;

    @Override
    public void onInitializeClient() {
        PreventBuildConfig.loadConditionConfigs();

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            final LiteralCommandNode<FabricClientCommandSource> pbNode = dispatcher.register(ClientCommandManager.literal("pb")
                    .executes(context -> {
                        context.getSource().getPlayer().sendMessage(Text.literal("§3Called /pb with no arguments"));
                        return 1;
                    })
                    .then(literal("help")
                            .executes(context -> {
                                context.getSource().getPlayer().sendMessage(Text.literal("§aPreventBuild commands:"));
                                context.getSource().getPlayer().sendMessage(Text.literal("/pb help - вывести список команд"));
                                context.getSource().getPlayer().sendMessage(Text.literal("/pb config list - вывести список конфигов"));
                                context.getSource().getPlayer().sendMessage(Text.literal("/pb config load <name> - обновить конфиг name"));
                                context.getSource().getPlayer().sendMessage(Text.literal("/pb config save <name> - обновить конфиг name"));
                                context.getSource().getPlayer().sendMessage(Text.literal("/pb config update - обновить список конфигов"));
                                return 1;
                            })
                    )
                    .then(literal("config")
                            .executes(context -> {
                                context.getSource().getPlayer().sendMessage(Text.literal("Type /rh help to see a list of all commands"));
                                return 1;
                            })
                            .then(literal("load")
                                    .then(argument("filename", StringArgumentType.word())
                                            .executes(context -> {
                                                String name = context.getArgument("filename", String.class);
                                                context.getSource().getPlayer().sendMessage(Text.literal("§3Загрузка конфиг-файла \"" + name + "\""));
                                                try {
                                                    config = new ConditionConfig(name);
                                                    context.getSource().getPlayer().sendMessage(
                                                            Text.literal("§aУспешно загружен конфиг §3\"" + config.getName() + "\"")
                                                    );
                                                    System.out.println("\nname:" + config.getName() + "\n" + config.getCondition().getString());
                                                } catch (Exception e) {
                                                    context.getSource().getPlayer().sendMessage(
                                                            Text.literal("§cПроизошла ошибка при чтении конфига, " +
                                                                    "для подробностей откройте логи клиента")
                                                    );
                                                    System.out.println("Unexpected error while loading config file:\n" + e.getMessage());
                                                }
                                                return 1;
                                            })
                                    )
                            )
                            .then(literal("list")
                                    .executes(context -> {
                                        Map<String, Boolean> configs = PreventBuildConfig.getConfigsList();
                                        if (configs.isEmpty()) {
                                            context.getSource().getPlayer().sendMessage(Text.literal("§3Не найдено конфигов"));
                                        }
                                        for (String config : configs.keySet()) {
                                            context.getSource().getPlayer().sendMessage(Text.literal(
                                                    "§3\"" + config + "\" : " + (configs.get(config) ? "§aactive" : "§cinactive")
                                            ));
                                        }
                                        return 1;
                                    })
                            )
                            .then(literal("switch")
                                    .then(argument("name", StringArgumentType.word())
                                            .suggests(new SuggestionProvider<FabricClientCommandSource>() {
                                                @Override
                                                public CompletableFuture<Suggestions> getSuggestions(CommandContext<FabricClientCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
                                                    for (String config : PreventBuildConfig.getConfigsList().keySet()) {
                                                        builder.suggest(config.replace(" ", "_"));
                                                    }
                                                    return builder.buildFuture();
                                                }
                                            })
                                            .executes(context -> {
                                                String name = context.getArgument("name", String.class).replace("_", " ");
                                                if (PreventBuildConfig.switchConfigEnabled(name) == -1) {
                                                    context.getSource().getPlayer().sendMessage(Text.literal("§3Config \"" + name + "\" not found"));
                                                    return 0;
                                                }
                                                if (PreventBuildConfig.isConfigEnabled(name))
                                                    context.getSource().getPlayer().sendMessage(Text.literal("§3Config \"" + name + "\" is §aactive §3now"));
                                                else
                                                    context.getSource().getPlayer().sendMessage(Text.literal("§3Config \"" + name + "\" is §cinactive §3now"));
                                                return 1;
                                            })
                                    )
                            )
                            .then(literal("update")
                                    .executes(context -> {
                                        PreventBuildConfig.loadConditionConfigs();
                                        context.getSource().getPlayer().sendMessage(Text.literal("§3Список конфигов обновлён"));
                                        return 1;
                                    })
                            )
                    )
            );
            dispatcher.register(literal("preventbuild").redirect(pbNode));
        });

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


        UseBlockCallback.EVENT.register((PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) ->
        {
            if (!player.getWorld().isClient)
                return ActionResult.PASS;

            for (String configName : PreventBuildConfig.getConfigsList().keySet()) {
                ConditionConfig config = PreventBuildConfig.getConditionConfig(configName);
                if (config != null && PreventBuildConfig.isConfigEnabled(configName)) {
                    BlockPos pos = hitResult.getBlockPos().offset(hitResult.getSide());

                    if (config.getCondition(ConditionCategory.PLACE).check(player, hand, pos.getX(), pos.getY(), pos.getZ(), hitResult)
                            || config.getCondition(ConditionCategory.OTHER).check(player, hand, pos.getX(), pos.getY(), pos.getZ(), hitResult))
                        return ActionResult.FAIL;
                }
            }
            return ActionResult.PASS;
        });

        AttackBlockCallback.EVENT.register((PlayerEntity player, World world, Hand hand, BlockPos pos, Direction direction) ->
        {
            if (!player.getWorld().isClient)
                return ActionResult.PASS;

            for (String configName : PreventBuildConfig.getConfigsList().keySet()) {
                ConditionConfig config = PreventBuildConfig.getConditionConfig(configName);
                if (config != null && PreventBuildConfig.isConfigEnabled(configName)) {
                    if (config.getCondition(ConditionCategory.BREAK).check(player, hand, pos.getX(), pos.getY(), pos.getZ())
                            || config.getCondition(ConditionCategory.OTHER).check(player, hand, pos.getX(), pos.getY(), pos.getZ()))
                        return ActionResult.FAIL;
                }
            }
            return ActionResult.PASS;
        });

        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (!player.getWorld().isClient)
                return ActionResult.PASS;

            for (String configName : PreventBuildConfig.getConfigsList().keySet()) {
                ConditionConfig config = PreventBuildConfig.getConditionConfig(configName);
                if (config != null && PreventBuildConfig.isConfigEnabled(configName)) {
                    return ((IEntityCondition)(config.getCondition(ConditionCategory.INTERACT_ENTITY)))
                            .check(ConditionCategory.INTERACT_ENTITY, player, world, hand, entity, hitResult);
                }
            }
            return ActionResult.PASS;
        });

        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (!player.getWorld().isClient)
                return ActionResult.PASS;

            for (String configName : PreventBuildConfig.getConfigsList().keySet()) {
                ConditionConfig config = PreventBuildConfig.getConditionConfig(configName);
                if (config != null && PreventBuildConfig.isConfigEnabled(configName)) {
                    return ((IEntityCondition)(config.getCondition(ConditionCategory.INTERACT_ENTITY)))
                            .check(ConditionCategory.ATTACK_ENTITY, player, world, hand, entity, hitResult);
                }
            }
            return ActionResult.PASS;
        });
    }
}
/* TODO:
    - Ore dictionaries
    - New conditions:
        - "Place block A by RMB on block B"
        - "blockReplace: replace block A by any block (grass, light)"
    -
    - Extra future plan:
        - Create a visual UI for configuration preventing conditions
 */