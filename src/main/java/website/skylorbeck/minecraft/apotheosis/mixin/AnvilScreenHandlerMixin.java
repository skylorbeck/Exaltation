package website.skylorbeck.minecraft.apotheosis.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(AnvilScreenHandler.class)
public class AnvilScreenHandlerMixin {
   @Redirect(at = @At(value = "INVOKE",target = "Lnet/minecraft/inventory/CraftingResultInventory;setStack(ILnet/minecraft/item/ItemStack;)V"),method = "updateResult",slice = @Slice(from = @At(value = "INVOKE",target = "Lnet/minecraft/item/ItemStack;setRepairCost(I)V"),to = @At(value = "INVOKE",target = "Lnet/minecraft/screen/AnvilScreenHandler;sendContentUpdates()V") ))
   private void injectedUpdateResult(CraftingResultInventory craftingResultInventory, int slot, ItemStack stack){
       stack.getOrCreateNbt().putBoolean("ApoSmith",false);
           craftingResultInventory.setStack(slot,stack);
   }
}
