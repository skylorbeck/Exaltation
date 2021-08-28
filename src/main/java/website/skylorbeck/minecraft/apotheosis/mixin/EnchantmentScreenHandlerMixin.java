package website.skylorbeck.minecraft.apotheosis.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.screen.EnchantmentScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(EnchantmentScreenHandler.class)
public class EnchantmentScreenHandlerMixin {
    @ModifyVariable(method = "onButtonClick",name = "itemStack", at = @At(value = "RETURN"))
    private ItemStack injectedButtonClick(ItemStack itemStack){
        if (itemStack.getOrCreateNbt().getBoolean("ApoSmith")) {
            assert itemStack.getNbt() != null;
            itemStack.getNbt().putBoolean("ApoSmith",false);
        }
        return itemStack;
    }
}
