package rh.preventbuild.conditions.items;

import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import rh.preventbuild.conditions.ConditionCategory;
import rh.preventbuild.conditions.ICondition;

import java.util.Set;

public class ItemEnchantmentCondition implements ICondition {
    private final String enchantmentId;
    private final int enchantmentLevel;

    public ItemEnchantmentCondition (String enchantmentId, int enchantmentLevel) {
        this.enchantmentId = enchantmentId;
        this.enchantmentLevel = enchantmentLevel;
    }

    public ItemEnchantmentCondition (String enchantmentId) {
        this(enchantmentId, -1);
    }

    @Override
    public ConditionCategory getCategory() {
        return ConditionCategory.OTHER;
    }

    @Override
    public ActionResult attackBlockCheck(PlayerEntity player, Hand hand, int x, int y, int z) {
        ItemStack itemStack = player.getStackInHand(hand);
        ItemEnchantmentsComponent enchantComponents = itemStack.getEnchantments();
        Set<RegistryEntry<Enchantment>> enchants = enchantComponents.getEnchantments();

        boolean res = enchants.stream().anyMatch(p -> {
            if (p.getIdAsString().equals(enchantmentId)) {
                if (enchantmentLevel == -1)
                    return true;
                return enchantComponents.getLevel(p) == enchantmentLevel;
            }
            return false;
        });

        return res ? ActionResult.FAIL : ActionResult.PASS;

    }
}
