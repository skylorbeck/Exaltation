package website.skylorbeck.minecraft.exaltation.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class HealthBooster extends Enchantment {
    public HealthBooster() {
        super(Rarity.VERY_RARE, EnchantmentTarget.WEARABLE, new EquipmentSlot[]{EquipmentSlot.CHEST,EquipmentSlot.HEAD,EquipmentSlot.FEET,EquipmentSlot.LEGS,EquipmentSlot.OFFHAND,EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMinLevel() {
        return 1;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public boolean isTreasure() {
        return true;
    }

    @Override
    public boolean isAvailableForEnchantedBookOffer() {
        return true;
    }

    @Override
    public boolean isAvailableForRandomSelection() {
        return true;
    }
}
