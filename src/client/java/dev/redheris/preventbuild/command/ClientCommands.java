package dev.redheris.preventbuild.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.redheris.preventbuild.PreventBuildClient;
import dev.redheris.preventbuild.conditions.ConditionConfig;
import dev.redheris.preventbuild.conditions.ConditionsManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.CommonColors;
import net.minecraft.world.entity.player.Player;

import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class ClientCommands {
    private final static ConditionsManager conditionsManager = PreventBuildClient.conditionsManager;

    public static void registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            var pbNode = dispatcher
                    .register(literal("pb")
                            .then(literal("update")
                                    .executes(ClientCommands::updateConfigs))
                            .then(literal("toggle")
                                    .then(argument("config_name", StringArgumentType.greedyString())
                                            .suggests(ClientCommands::suggestConditionConfigs)
                                            .executes(ClientCommands::toggleConfig)))
                            .then(literal("list")
                                    .executes(ClientCommands::configsList)));
            dispatcher
                    .register(literal("preventbuild")
                            .redirect(pbNode));
        });
    }

    private static int updateConfigs(CommandContext<FabricClientCommandSource> context) {
        Player player = context.getSource().getPlayer();
        var failedFiles = conditionsManager.read();
        int count = conditionsManager.getConfigs().size();

        if (count > 0) {
            player.displayClientMessage(
                    Component.translatable("preventbuild.configs_read", count)
                            .withColor(CommonColors.GREEN),
                    false
            );
            return 0;
        }

        var msg = Component.translatable(
                "preventbuild.failed_to_parse_configs", failedFiles.size()
        );
        for (Map.Entry<Path, String> entry : failedFiles.entrySet()) {
            msg.append("\n");
            Path filepath = entry.getKey();
            String filename = filepath.getFileName().toString();
            String description = entry.getValue();
            msg.append(Component.literal(filename)
                    .withStyle(style -> style
                            .withColor(CommonColors.LIGHT_GRAY)
                            .withUnderlined(true)
                            .withClickEvent(new ClickEvent.OpenFile(filepath))
                            .withHoverEvent(new HoverEvent.ShowText(
                                    Component.translatable("preventbuild.open_file")
                            ))));
            msg.append(" : ");
            msg.append(Component.literal(description).withColor(CommonColors.SOFT_RED));
        }
        player.displayClientMessage(msg, false);
        return -1;
    }

    private static int toggleConfig(CommandContext<FabricClientCommandSource> context) {
        String configName = context.getArgument("config_name", String.class);
        ConditionConfig config = conditionsManager.findConfigByName(configName);
        Player player = context.getSource().getPlayer();

        if (config == null) {
            player.displayClientMessage(
                    Component.translatable("preventbuild.unknown_config", configName)
                            .withColor(CommonColors.SOFT_RED),
                    false
            );
            return -1;
        }

        boolean newActive = !conditionsManager.isActive(config);
        conditionsManager.setActive(config, newActive);

        Component status;
        if (newActive) {
            status = Component.translatable("preventbuild.active").withColor(CommonColors.GREEN);
        } else {
            status = Component.translatable("preventbuild.inactive").withColor(CommonColors.SOFT_RED);
        }
        player.displayClientMessage(
                Component.translatable("preventbuild.config_status_changed", configName, status),
                true
        );
        return 0;
    }

    private static CompletableFuture<Suggestions> suggestConditionConfigs(
            CommandContext<FabricClientCommandSource> context,
            SuggestionsBuilder builder
    ) {
        conditionsManager.getConfigs().stream()
                .map(ConditionConfig::name)
                .filter(name -> name
                        .toLowerCase()
                        .contains(builder.getRemainingLowerCase()))
                .forEach(builder::suggest);
        return builder.buildFuture();
    }

    private static int configsList(CommandContext<FabricClientCommandSource> context) {
        var msg = Component.translatable("preventbuild.configs_list");
        for (ConditionConfig config : conditionsManager.getConfigs()) {
            msg.append("\n");
            msg.append(Component.literal(config.name())
                    .withStyle(style -> style
                            .withColor(CommonColors.LIGHT_GRAY)
                            .withUnderlined(true)
                            .withClickEvent(new ClickEvent.OpenFile(config.filepath()))
                            .withHoverEvent(new HoverEvent.ShowText(
                                    Component.translatable("preventbuild.open_file")
                            ))));
            msg.append(" : ");
            MutableComponent toggleActive;

            boolean active = conditionsManager.isActive(config);
            if (active) {
                toggleActive = Component.translatable("preventbuild.active").withColor(CommonColors.GREEN);
            } else {
                toggleActive = Component.translatable("preventbuild.inactive").withColor(CommonColors.SOFT_RED);
            }

            toggleActive.withStyle(style -> style
//                    .withClickEvent(() -> {
//                        conditionsManager.setActive(config, !active);
//                        return ClickEvent.Action.CUSTOM;
//                    })
                    .withHoverEvent(new HoverEvent.ShowText(
                            Component.translatable("preventbuild.toggle_enabled"))
                    ));
            msg.append(toggleActive);
        }
        context.getSource().getPlayer().displayClientMessage(msg, false);
        return 0;
    }
}
