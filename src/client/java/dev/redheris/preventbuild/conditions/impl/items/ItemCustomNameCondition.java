package dev.redheris.preventbuild.conditions.impl.items;

import dev.redheris.preventbuild.api.IllegalConditionValueException;
import dev.redheris.preventbuild.conditions.ConditionResult;
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
        if (itemStack.getCustomName() == null) {
            return ConditionResult.PASS;
        }
        String customName = itemStack.getCustomName().getString();

        boolean check = isRegex ? customName.matches(value) : customName.equals(value);

        return check ? ConditionResult.CANCEL : ConditionResult.PASS;
    }
}
