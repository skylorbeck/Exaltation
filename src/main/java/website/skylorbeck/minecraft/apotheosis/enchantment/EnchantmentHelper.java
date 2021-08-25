package website.skylorbeck.minecraft.apotheosis.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

import java.util.Iterator;

import static net.minecraft.enchantment.EnchantmentHelper.getLevel;

public class EnchantmentHelper {
    public static int getEquipmentLevel(Enchantment enchantment, LivingEntity entity) {
        Iterable<ItemStack> iterable = enchantment.getEquipment(entity).values();
        if (iterable == null) {
            return 0;
        } else {
            int i = 0;
            Iterator var4 = iterable.iterator();

            while(var4.hasNext()) {
                ItemStack itemStack = (ItemStack)var4.next();
                int j = getLevel(enchantment, itemStack);
                if (j > 0) {
                    i += j;
                }
            }

            return i;
        }
    }
}
