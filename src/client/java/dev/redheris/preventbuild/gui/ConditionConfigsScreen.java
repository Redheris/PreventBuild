package dev.redheris.preventbuild.gui;

import dev.redheris.preventbuild.PreventBuildClient;
import dev.redheris.preventbuild.command.ClientCommands;
import dev.redheris.preventbuild.conditions.ConditionConfig;
import dev.redheris.preventbuild.conditions.ConditionsManager;
import dev.redheris.preventbuild.config.PreventBuildConfig;
import dev.redheris.preventbuild.config.customtags.CustomTagsManager;
import dev.redheris.preventbuild.util.CompatUtils;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.layouts.SpacerElement;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.CommonColors;

public class ConditionConfigsScreen extends Screen {
    private ConditionConfigsListWidget listWidget;

    public ConditionConfigsScreen() {
        super(Component.translatable("preventbuild.gui.condition_configs_screen"));
    }

    @Override
    protected void init() {
        LinearLayout list = LinearLayout.vertical();
        ConditionsManager conditionsManager = PreventBuildClient.conditionsManager;

        int boxWidth = (int) Math.min(350, width * 0.8);
        int boxHeight = (int) Math.min(250, height * 0.7);

        for (ConditionConfig config : conditionsManager.getConfigs()) {

            GridLayout entry = new GridLayout();
            entry.defaultCellSetting()
                    .alignVerticallyMiddle()
                    .alignHorizontallyLeft();
            GridLayout.RowHelper entryAdder = entry.createRowHelper(2);
            int entryHeight = 27;

            Component configName = Component.literal(config.name());
            ComponentButton configNameBtn = new ComponentButton(
                    0, 0,
                    font.width(configName), entryHeight,
                    configName,
                    btn -> CompatUtils.openPath(config.filepath()),
                    font
            );
            configNameBtn.setUnderlineOnHover(true);
            configNameBtn.setRenderBackground(false);

            entryAdder.addChild(configNameBtn, entry.newCellSettings().paddingLeft(5));

            boolean active = conditionsManager.isActive(config);
            MutableComponent activeComponent = Component.translatable("preventbuild.active").withColor(CommonColors.GREEN);
            MutableComponent inactiveComponent = Component.translatable("preventbuild.inactive").withColor(CommonColors.SOFT_RED);
            StringWidget activeLabel = new StringWidget(active ? activeComponent : inactiveComponent, font);
            entryAdder.addChild(
                    activeLabel,
                    entry.newCellSettings()
                            .alignHorizontallyRight()
                            .paddingRight(10)
                            .paddingTop(1)
            );

            configNameBtn.setWidth(Math.min(boxWidth - font.width(inactiveComponent) - 5 - 22, configNameBtn.getWidth()));
            entry.addChild(
                    SpacerElement.width(boxWidth - configNameBtn.getWidth() - 5), 1, 1,
                    entry.newCellSettings().padding(0)
            );

            list.addChild(new ConditionConfigsEntry(
                    config, configNameBtn, activeLabel,
                    0, 0,
                    boxWidth, entryHeight,
                    Component.literal(config.name()),
                    entry
            ));
        }

        listWidget = new ConditionConfigsListWidget(
                (width - boxWidth) / 2, (height - boxHeight) / 2, boxWidth, boxHeight, Component.empty(),
                list
        );

        listWidget.arrangeElements();

        this.addRenderableWidget(listWidget);

        Button closeBtn = new ComponentButton(
                listWidget.getX() + 1, listWidget.getY() - 24,
                20, 20,
                Component.literal("x"),
                btn -> onClose(),
                font
        );

        Component openFolderLabel = Component.translatable("preventbuild.gui.open_conditions_folder");
        Button openFolderBtn = new ComponentButton(
                closeBtn.getRight() + 2, listWidget.getY() - 24,
                font.width(openFolderLabel) + 7, 20,
                openFolderLabel,
                btn -> CompatUtils.openPath(PreventBuildConfig.CONDITIONS_DIR),
                font
        );

        Component customTagsLabel = Component.translatable("preventbuild.gui.edit_custom_tags");
        Button customTagsBtn = new ComponentButton(
                openFolderBtn.getRight() + 2, listWidget.getY() - 24,
                font.width(customTagsLabel) + 7, 20,
                customTagsLabel,
                btn -> CompatUtils.openPath(CustomTagsManager.TAGS_FILE_PATH),
                font
        );

        Component updateConfigsLabel = Component.translatable("preventbuild.gui.update");
        Button updateConfigsBtn = new ComponentButton(
                customTagsBtn.getRight() + 2, openFolderBtn.getY(),
                font.width(updateConfigsLabel) + 7, 20,
                updateConfigsLabel,
                btn -> {
                    if (minecraft.level != null) {
                        int i = ClientCommands.updateConfigs(minecraft.player, minecraft.level.registryAccess());
                        if (i < 0) {
                            onClose();
                        } else {
                            rebuildWidgets();
                        }
                    }
                },
                font
        );
        this.addRenderableWidget(closeBtn);
        this.addRenderableWidget(openFolderBtn);
        this.addRenderableWidget(customTagsBtn);
        this.addRenderableWidget(updateConfigsBtn);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (listWidget != null) {
            return listWidget.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
