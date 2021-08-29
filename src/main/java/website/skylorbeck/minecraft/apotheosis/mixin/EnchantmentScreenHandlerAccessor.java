package website.skylorbeck.minecraft.apotheosis.mixin;

import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.EnchantmentScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EnchantmentScreenHandler.class)
public interface EnchantmentScreenHandlerAccessor {
    @Accessor("inventory")
    Inventory getInvetory();
}
