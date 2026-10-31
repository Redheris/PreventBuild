package dev.redheris.preventbuild.conditions.impl.items;

import dev.redheris.preventbuild.api.IllegalConditionValueException;
import dev.redheris.preventbuild.conditions.ConditionResult;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class ItemCustomNameCondition extends ItemConditionBase {
    private final boolean isRegex;
    private final String value;

    public ItemCustomNameCondition(boolean isRegex, String value) {
        this.isRegex = isRegex;
        this.value = value;

        if (isRegex) {
            try {
                Pattern.compile(value);
            } catch (PatternSyntaxException e) {
                throw new IllegalConditionValueException("Invalid regex pattern syntax: " + e.getMessage(), e);
            }
        }
    }

    @Override
    protected ConditionResult checkItem(Player player, Level level, ItemStack itemStack) {
        Component customName = itemStack.get(DataComponents.CUSTOM_NAME);
        if (customName == null) {
            return ConditionResult.PASS;
        }

        boolean check = isRegex ? customName.getString().matches(value) : customName.getString().equals(value);

        return check ? ConditionResult.CANCEL : ConditionResult.PASS;
    }
}
