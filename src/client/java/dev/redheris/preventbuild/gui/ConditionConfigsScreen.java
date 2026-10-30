package dev.redheris.preventbuild.gui;

import dev.redheris.preventbuild.PreventBuildClient;
import dev.redheris.preventbuild.conditions.ConditionConfig;
import dev.redheris.preventbuild.conditions.ConditionsManager;
import dev.redheris.preventbuild.config.PreventBuildConfig;
import dev.redheris.preventbuild.config.customtags.CustomTagsManager;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.layouts.SpacerElement;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.CommonColors;
import net.minecraft.util.Util;

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
                    .alignHorizontallyLeft()
                    .paddingTop(1)
                    .paddingHorizontal(5);
            GridLayout.RowHelper entryAdder = entry.createRowHelper(2);
            entryAdder.addChild(
                    SpacerElement.width(boxWidth), 2,
                    entry.newCellSettings().padding(0)
            );
            int entryHeight = 26;

            Component configName = Component.literal(config.name());
            ComponentButton configNameBtn = new ComponentButton(
                    0, 0,
                    font.width(configName), entryHeight,
                    configName,
                    btn -> Util.getPlatform().openPath(config.filepath()),
                    font
            );
            configNameBtn.setUnderlineOnHover(true);
            configNameBtn.setRenderBackground(false);

            entryAdder.addChild(configNameBtn);

            MutableComponent toggleActive;
            boolean active = conditionsManager.isActive(config);
            if (active) {
                toggleActive = Component.translatable("preventbuild.active").withColor(CommonColors.GREEN);
            } else {
                toggleActive = Component.translatable("preventbuild.inactive").withColor(CommonColors.SOFT_RED);
            }
            StringWidget activeLabel = new StringWidget(toggleActive, font);
            entryAdder.addChild(
                    activeLabel,
                    entry.newCellSettings()
                            .alignHorizontallyRight()
                            .paddingRight(10)
            );

            list.addChild(new ConditionConfigsEntry(
                    config, configNameBtn, activeLabel,
                    0, 0,
                    boxWidth, entryHeight + 1,
                    Component.literal(config.name()),
                    entry
            ));
        }

        listWidget = new ConditionConfigsListWidget(
                (width - boxWidth) / 2, (height - boxHeight) / 2, boxWidth, boxHeight, Component.literal("Hello yopta"),
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

        Component openFolderLabel = Component.literal("Открыть папку с конфигами");
        Button openFolderBtn = new ComponentButton(
                closeBtn.getRight() + 2, listWidget.getY() - 24,
                font.width(openFolderLabel) + 7, 20,
                openFolderLabel,
                btn -> Util.getPlatform().openPath(PreventBuildConfig.CONDITIONS_DIR),
                font
        );

        Component updateConfigsLabel = Component.literal("Обновить");
        Button updateConfigsBtn = new ComponentButton(
                openFolderBtn.getRight() + 2, openFolderBtn.getY(),
                font.width(updateConfigsLabel) + 7, 20,
                updateConfigsLabel,
                btn -> {
                    CustomTagsManager.read();
                    if (minecraft.level != null) {
                        conditionsManager.read(minecraft.level.registryAccess());
                    }
                    rebuildWidgets();
                },
                font
        );
        this.addRenderableWidget(closeBtn);
        this.addRenderableWidget(openFolderBtn);
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
