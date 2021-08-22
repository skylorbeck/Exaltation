package website.skylorbeck.minecraft.apotheosis.mixin;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Inject(at = @At("RETURN"),method = "isEnchantable",cancellable = true)
    public void injectedHasEnchantments(CallbackInfoReturnable<Boolean> cir){
        if (((ItemStack)(Object)this).getOrCreateNbt().getBoolean("ApoSmith")){
            cir.setReturnValue(true);
        }
    }
}
