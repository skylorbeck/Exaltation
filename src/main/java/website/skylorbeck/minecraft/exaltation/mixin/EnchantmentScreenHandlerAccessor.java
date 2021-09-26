package website.skylorbeck.minecraft.exaltation.mixin;

import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.EnchantmentScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(EnchantmentScreenHandler.class)
public interface EnchantmentScreenHandlerAccessor {
    @Accessor("inventory")
    Inventory getInvetory();
    @Invoker("generateEnchantments")
    List<EnchantmentLevelEntry> invokeGenerateEnchantments(ItemStack stack, int slot, int level);
}
