package website.skylorbeck.minecraft.apotheosis.mixin;

import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
   /* @Inject(at = @At("HEAD"),method = "applyEnchantmentCosts",cancellable = true)
    public void injectedHasEnchantments(ItemStack enchantedItem, int experienceLevels, CallbackInfo ci){
        if (PowerHolderComponent.hasPower(((PlayerEntity)(Object)this), ArcanesmithCheapEnchantingPower.class)){
            experienceLevels-=experienceLevels/2;
        }
    }*/
}
