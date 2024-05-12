package rh.preventbuild.client.gui.conditions;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ConditionsConfigGui extends LightweightGuiDescription {
    public ConditionsConfigGui() {
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(256, 240);
        root.setInsets(Insets.ROOT_PANEL);



        WSprite icon = new WSprite(new Identifier("minecraft:textures/item/redstone.png"));
        root.add(icon, 0, 2, 1, 1);

        WItem item = new WItem(new ItemStack(Items.CRYING_OBSIDIAN));
        root.add(item, 4, 2, 1, 1);

        WItem item2 = new WItem(new ItemStack(Items.GOLDEN_CARROT));
        root.add(item2, 4, 4, 1, 1);

        WButton button = new WButton(Text.translatable("gui.examplemod.examplebutton"));
        button.setOnClick(() -> {
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.literal("Hello, World!"));
        });
        TooltipBuilder tooltip = new TooltipBuilder();
        tooltip.add(Text.literal("Click it"));
        button.addTooltip(tooltip);
        root.add(button, 0, 3, 4, 1);

        WLabel label = new WLabel(Text.literal("Test"), 0xFFFFFF);
        root.add(label, 0, 4, 2, 1);

        root.validate(this);
    }
}
