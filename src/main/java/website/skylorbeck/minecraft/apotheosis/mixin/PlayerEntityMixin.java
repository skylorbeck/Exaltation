package website.skylorbeck.minecraft.apotheosis.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import website.skylorbeck.minecraft.apotheosis.powers.ArcanesmithCheapEnchantingPower;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
   /* @Inject(at = @At("HEAD"),method = "applyEnchantmentCosts",cancellable = true)
    public void injectedHasEnchantments(ItemStack enchantedItem, int experienceLevels, CallbackInfo ci){
        if (PowerHolderComponent.hasPower(((PlayerEntity)(Object)this), ArcanesmithCheapEnchantingPower.class)){
            experienceLevels-=experienceLevels/2;
        }
    }*/
}
