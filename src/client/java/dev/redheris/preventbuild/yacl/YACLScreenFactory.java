package dev.redheris.preventbuild.yacl;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.gui.controllers.BooleanController;
import dev.isxander.yacl3.gui.controllers.string.number.IntegerFieldController;
import dev.redheris.preventbuild.PreventBuildClient;
import dev.redheris.preventbuild.config.PreventBuildConfig;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class YACLScreenFactory {

    public static Screen getConfigScreen(Screen parent) {
        PreventBuildConfig config = PreventBuildClient.commonConfig;
        PreventBuildConfig.Defaults defaults = PreventBuildConfig.DEFAULTS;

        return YetAnotherConfigLib.createBuilder()
                .save(config::save)
                .title(Component.translatable("maparthelper.config.title"))
                .category(ConfigCategory.createBuilder()
                        .name(Component.translatable("preventbuild.config.category.common"))
                        .option(Option.<Boolean>createBuilder()
                                .name(Component.translatable("preventbuild.config.common.fixPickItemByBlock"))
                                .description(OptionDescription.of(Component.translatable("preventbuild.config.common.description.fixPickItemByBlock")))
                                .customController(opt -> new BooleanController(opt, true))
                                .binding(
                                        defaults.fixPickItemByBlock(),
                                        config::fixPickItemByBlock,
                                        config::setFixPickItemByBlock
                                )
                                .build())

                        .option(Option.<Integer>createBuilder()
                                .name(Component.translatable("preventbuild.config.common.maxWaitingTimeoutMs"))
                                .description(OptionDescription.of(Component.translatable("preventbuild.config.common.description.maxWaitingTimeoutMs")))
                                .customController(opt -> new IntegerFieldController(opt, 1, 60000))
                                .binding(
                                        defaults.maxWaitingTimeoutMs(),
                                        config::maxWaitingTimeoutMs,
                                        config::setMaxWaitingTimeoutMs
                                )
                                .build())

                        .option(Option.<Boolean>createBuilder()
                                .name(Component.translatable("preventbuild.config.common.logWaitingTime"))
                                .description(OptionDescription.of(Component.translatable("preventbuild.config.common.description.logWaitingTime")))
                                .customController(opt -> new BooleanController(opt, true))
                                .binding(
                                        defaults.logWaitingTime(),
                                        config::logWaitingTime,
                                        config::setLogWaitingTime
                                )
                                .build())

                        .build())
                .build().generateScreen(parent);
    }
}
