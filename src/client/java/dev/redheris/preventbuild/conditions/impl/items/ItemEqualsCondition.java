package dev.redheris.preventbuild.conditions.impl.items;

import dev.redheris.preventbuild.conditions.ConditionResult;
import dev.redheris.preventbuild.conditions.util.IdentifierTagWrapper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class ItemEqualsCondition extends ItemConditionBase {
    private final List<IdentifierTagWrapper<Item>> itemIds;

    public ItemEqualsCondition(List<IdentifierTagWrapper<Item>> itemIds) {
        this.itemIds = itemIds;
    }

    protected ConditionResult checkItem(Player player, Level level, ItemStack itemStack) {
        Item item = itemStack.getItem();
        Identifier itemId = BuiltInRegistries.ITEM.getKey(item);
        for (IdentifierTagWrapper<Item> wrapper : itemIds) {
            if (wrapper.isIn(item, itemId)) {
                return ConditionResult.CANCEL;
            }
        }
        return ConditionResult.PASS;
    }
}
