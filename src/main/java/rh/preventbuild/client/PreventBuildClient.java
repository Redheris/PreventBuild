package rh.preventbuild.client;


import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import rh.preventbuild.PreventBuildConfig;
import rh.preventbuild.conditions.ConditionCategory;
import rh.preventbuild.conditions.ConditionConfig;
import rh.preventbuild.conditions.ICondition;
import rh.preventbuild.conditions.basic.NullCondition;

import java.util.Map;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

@Environment(EnvType.CLIENT)
public class PreventBuildClient implements ClientModInitializer {

    public static ConditionConfig config;

    @Override
    public void onInitializeClient() {
        PreventBuildConfig.loadOreDictionary();
        PreventBuildConfig.loadConditionConfigs();

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            final LiteralCommandNode<FabricClientCommandSource> pbNode = dispatcher.register(ClientCommandManager.literal("pb")
                    .executes(context -> {
                        context.getSource().getPlayer().sendMessage(Text.literal("§cВызван /pb без аргументов"), false);
                        return 1;
                    })
                    .then(literal("help")
                            .executes(context -> {
                                context.getSource().getPlayer().sendMessage(Text.literal("§aPreventBuild commands:"), false);
                                context.getSource().getPlayer().sendMessage(Text.literal("/pb help - вывести список команд"), false);
                                context.getSource().getPlayer().sendMessage(Text.literal("/pb config list - вывести список конфигов"), false);
                                context.getSource().getPlayer().sendMessage(Text.literal("/pb config load <name> - обновить конфиг name"), false);
                                context.getSource().getPlayer().sendMessage(Text.literal("/pb config switch <name> - включить/выключить конфиг name"), false);
                                context.getSource().getPlayer().sendMessage(Text.literal("/pb config update - обновить список конфигов"), false);
                                return 1;
                            })
                    )
                    .then(literal("config")
                            .executes(context -> {
                                context.getSource().getPlayer().sendMessage(Text.literal("§3Напишите /rh help для получения списка команд"), false);
                                return 1;
                            })
                            .then(literal("list")
                                    .executes(context -> {
                                        Map<String, Boolean> configs = PreventBuildConfig.getConfigsList();
                                        if (configs.isEmpty()) {
                                            context.getSource().getPlayer().sendMessage(Text.literal("§3Не найдено конфигов"), false);
                                        }
                                        for (String config : configs.keySet()) {
                                            context.getSource().getPlayer().sendMessage(Text.literal(
                                                    "§3\"" + config + "\" : " + (configs.get(config) ? "§aактивен" : "§cнеактивен")),
                                                    false
                                            );
                                        }
                                        return 1;
                                    })
                            )
                            .then(literal("switch")
                                    .then(argument("name", StringArgumentType.word())
                                            .suggests((context, builder) -> {
                                                for (String config : PreventBuildConfig.getConfigsList().keySet()) {
                                                    builder.suggest(config.replace(" ", "_"));
                                                }
                                                return builder.buildFuture();
                                            })
                                            .executes(context -> {
                                                String name = context.getArgument("name", String.class).replace("_", " ");
                                                if (PreventBuildConfig.switchConfigEnabled(name) == -1) {
                                                    context.getSource().getPlayer().sendMessage(
                                                            Text.literal("§3Конфиг \"" + name + "\" не найден"),
                                                            false);
                                                    return 0;
                                                }
                                                if (PreventBuildConfig.isConfigEnabled(name))
                                                    context.getSource().getPlayer().sendMessage(
                                                            Text.literal("§3Конфиг \"" + name + "\" теперь §aактивен"),
                                                            true
                                                    );
                                                else
                                                    context.getSource().getPlayer().sendMessage(
                                                            Text.literal("§3Конфиг \"" + name + "\" теперь §cнеактивен"),
                                                            true
                                                    );
                                                return 1;
                                            })
                                    )
                            )
                            .then(literal("update")
                                    .executes(context -> {
                                        boolean loadOreDict = PreventBuildConfig.loadOreDictionary();
                                        if (!loadOreDict) {
                                            context.getSource().getPlayer().sendMessage(
                                                    Text.literal("§cОшибка при загрузке словаря руд"),
                                                    false
                                            );
                                            return 0;
                                        }
                                        boolean loadConditions = PreventBuildConfig.loadConditionConfigs();
                                        if (!loadConditions) {
                                            context.getSource().getPlayer().sendMessage(
                                                    Text.literal("§cОшибка при загрузке конфигов"),
                                                    false
                                            );
                                            return 0;
                                        }

                                        context.getSource().getPlayer().sendMessage(Text.literal("§3Конфиги обновлены"), true);

                                        return 1;
                                    })
                            )
                    )
            );
            dispatcher.register(literal("preventbuild").redirect(pbNode));
        });

        UseBlockCallback.EVENT.register((PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) ->
        {
            if (!player.getWorld().isClient)
                return ActionResult.PASS;

            for (String configName : PreventBuildConfig.getConfigsList().keySet()) {
                ConditionConfig config = PreventBuildConfig.getConditionConfig(configName);
                if (config != null && PreventBuildConfig.isConfigEnabled(configName)) {
                    BlockPos pos = hitResult.getBlockPos().offset(hitResult.getSide());
                    ActionResult resOther = config.getCondition(ConditionCategory.OTHER).check(player, hand, pos.getX(), pos.getY(), pos.getZ(), hitResult);
                    if (resOther != ActionResult.PASS)
                        return resOther;
                    ActionResult resPlacing = config.getCondition(ConditionCategory.PLACE).check(player, hand, pos.getX(), pos.getY(), pos.getZ(), hitResult);
                    if (resPlacing != ActionResult.PASS)
                        return resPlacing;
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
                    ActionResult resOther = config.getCondition(ConditionCategory.OTHER).check(player, hand, pos.getX(), pos.getY(), pos.getZ());
                    if (resOther != ActionResult.PASS)
                        return resOther;
                    ActionResult resBreaking = config.getCondition(ConditionCategory.BREAK).check(player, hand, pos.getX(), pos.getY(), pos.getZ());
                    if (resBreaking != ActionResult.PASS)
                        return resBreaking;
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
                    ICondition condition = config.getCondition(ConditionCategory.INTERACT_ENTITY);
                    if (!(condition instanceof NullCondition)) {
                        ActionResult res = condition.check(ConditionCategory.INTERACT_ENTITY, player, world, hand, entity, hitResult);
                        if (res != ActionResult.PASS)
                            return res;
                    }
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
                    ICondition condition = config.getCondition(ConditionCategory.ATTACK_ENTITY);
                    if (!(condition instanceof NullCondition)) {
                        ActionResult res = condition.check(ConditionCategory.ATTACK_ENTITY, player, world, hand, entity, hitResult);
                        if (res != ActionResult.PASS)
                            return res;
                    }
                }
            }
            return ActionResult.PASS;
        });
    }
}