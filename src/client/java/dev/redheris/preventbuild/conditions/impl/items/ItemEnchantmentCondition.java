package dev.redheris.preventbuild.conditions.impl.items;

import dev.redheris.preventbuild.conditions.ConditionResult;
import dev.redheris.preventbuild.conditions.util.IdentifierTagWrapper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class ItemEnchantmentCondition extends ItemConditionBase {
    private final IdentifierTagWrapper<Enchantment> enchantment;
    private final Integer level;

    public ItemEnchantmentCondition(IdentifierTagWrapper<Enchantment> enchantment, @Nullable Integer level) {
        this.enchantment = enchantment;
        this.level = level;
    }

    @Override
    protected ConditionResult checkItem(Player player, Level level, ItemStack itemStack) {
        //~ if >1.21.1 'registryOrThrow' -> 'lookupOrThrow'
        var registry = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        ItemEnchantments enchantments = itemStack.getEnchantments();

        for (var entry : enchantments.keySet()) {
            Enchantment enchantment = entry.value();
            Identifier enchantmentId = registry.getKey(enchantment);
            if (this.enchantment.isIn(enchantment, enchantmentId)) {
                if (this.level != null) {
                    return enchantments.getLevel(entry) == this.level ? ConditionResult.CANCEL : ConditionResult.PASS;
                }
                return ConditionResult.CANCEL;
            }
        }
        return ConditionResult.PASS;
    }
}
