package rh.preventbuild.client;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Formatting;
import rh.preventbuild.PreventBuildConfig;

import java.nio.file.Path;
import java.util.Map;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class ClientCommands {
    protected static void registerAll() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            final LiteralCommandNode<FabricClientCommandSource> pbNode = dispatcher.register(ClientCommandManager.literal("pb")
                    .executes(context -> {
                        context.getSource().getPlayer().sendMessage(
                                Text.translatable("preventbuild.command_with_no_arguments").withColor(Colors.LIGHT_RED),
                                false);
                        return 1;
                    })
                    .then(literal("help").executes(ClientCommands::getHelp))
                    .then(literal("config")
                            .executes(context -> {
                                context.getSource().getPlayer().sendMessage(
                                        Text.translatable("preventbuild.config_without_arguments")
                                                .formatted(Formatting.DARK_AQUA),
                                        false);
                                return 1;
                            })
                            .then(literal("list").executes(ClientCommands::configList))
                            .then(literal("switch")
                                    .then(argument("name", StringArgumentType.word())
                                            .suggests((context, builder) -> {
                                                for (String config : PreventBuildConfig.getConfigsList().keySet()) {
                                                    builder.suggest(config.replace(" ", "_"));
                                                }
                                                return builder.buildFuture();
                                            })
                                            .executes(ClientCommands::configSwitch)
                                    )
                            )
                            .then(literal("update").executes(ClientCommands::configUpdate))
                    )
            );
            dispatcher.register(literal("preventbuild").redirect(pbNode));
            dispatcher.register(ClientCommandManager.literal("toggle_prevent_build_config_enabled")
                    .then(ClientCommandManager.argument("name", StringArgumentType.word())
                            .executes(context -> {
                                context.getSource().getPlayer().sendMessage(Text.empty(), false);
                                ClientCommands.configSwitch(context);
                                ClientCommands.configList(context);
                                return 1;
                            })
                    )
            );
        });
    }

    private static int getHelp(CommandContext<FabricClientCommandSource> context) {
        context.getSource().getPlayer().sendMessage(
                Text.translatable("preventbuild.help_line_1")
                        .formatted(Formatting.DARK_AQUA),
                false);
        context.getSource().getPlayer().sendMessage(
                Text.translatable("preventbuild.help_line_2"),
                false);
        context.getSource().getPlayer().sendMessage(
                Text.translatable("preventbuild.help_line_3"),
                false);
        context.getSource().getPlayer().sendMessage(
                Text.translatable("preventbuild.help_line_4"),
                false);
        context.getSource().getPlayer().sendMessage(
                Text.translatable("preventbuild.help_line_5"),
                false);
        return 1;
    }

    private static int configList(CommandContext<FabricClientCommandSource> context) {
        Map<String, Boolean> configs = PreventBuildConfig.getConfigsList();
        if (configs.isEmpty()) {
            context.getSource().getPlayer().sendMessage(
                    Text.translatable("preventbuild.no_configs_found")
                            .formatted(Formatting.DARK_AQUA),
                    false);
            return 0;
        }
        context.getSource().getPlayer().sendMessage(
                Text.translatable("preventbuild.configs_list")
                        .formatted(Formatting.GREEN),
                false);
        for (String config : configs.keySet()) {
            boolean isActive = configs.get(config);

            Path configPath = PreventBuildConfig.getConditionConfigPath(config);
            Text configName = Text.literal("\"" + config + "\"")
                    .styled(style -> style
                            .withColor(Formatting.DARK_AQUA)
                            .withHoverEvent(new HoverEvent.ShowText(Text.translatable("preventbuild.open_config_file")))
                            .withClickEvent(new ClickEvent.OpenFile(configPath))
                    );
            String toggleCommand = "/toggle_prevent_build_config_enabled " + config.replace(" ", "_");
            Text configIsActive = Text.translatable(isActive ? "preventbuild.config_is_active" : "preventbuild.config_is_inactive")
                    .styled(style -> style
                            .withColor(isActive ? Formatting.GREEN : Formatting.RED)
                            .withHoverEvent(new HoverEvent.ShowText(Text.translatable("preventbuild.toggle_enabled")))
                            .withClickEvent(new ClickEvent.RunCommand(toggleCommand))
                            .withUnderline(true)
                    );

            context.getSource().getPlayer().sendMessage(Text.empty()
                            .append(configName)
                            .append(Text.literal(" : "))
                            .append(configIsActive),
                    false
            );
        }
        return 1;
    }

    private static int configSwitch(CommandContext<FabricClientCommandSource> context) {
        String name = context.getArgument("name", String.class).replace("_", " ");
        if (PreventBuildConfig.switchConfigEnabled(name) == -1) {
            context.getSource().getPlayer().sendMessage(
                    Text.translatable("preventbuild.config_not_found", name)
                            .withColor(Colors.LIGHT_RED),
                    false);
            return 0;
        }
        Text configName = Text.literal(name).formatted(Formatting.WHITE);
        MutableText message = Text.translatable("preventbuild.config_is_toggled", configName)
                .formatted(Formatting.DARK_AQUA);
        if (PreventBuildConfig.isConfigEnabled(name)) {
            context.getSource().getPlayer().sendMessage(
                    message.append(Text.translatable("preventbuild.config_is_active")
                            .formatted(Formatting.GREEN)),
                    true
            );
        } else {
            context.getSource().getPlayer().sendMessage(
                    message.append(Text.translatable("preventbuild.config_is_inactive")
                            .formatted(Formatting.RED)),
                    true
            );
        }
        return 1;
    }

    private static int configUpdate(CommandContext<FabricClientCommandSource> context) {
        PreventBuildConfig.loadConfigs();
        Text exceptionMessage = PreventBuildConfig.getExceptionMessage();

        if (exceptionMessage != null) return 0;

        context.getSource().getPlayer().sendMessage(
                Text.translatable("preventbuild.configs_updated")
                        .formatted(Formatting.DARK_AQUA),
                true
        );
        return 1;
    }

}
