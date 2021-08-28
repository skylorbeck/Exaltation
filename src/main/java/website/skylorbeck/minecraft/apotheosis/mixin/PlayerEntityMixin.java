package website.skylorbeck.minecraft.apotheosis.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import website.skylorbeck.minecraft.apotheosis.enchantment.EnchantmentHelper;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(at = @At(value = "INVOKE",target = "Lnet/minecraft/enchantment/EnchantmentHelper;getFireAspect(Lnet/minecraft/entity/LivingEntity;)I"),method = "attack",cancellable = true)
    public void injectedHasEnchantments(Entity target, CallbackInfo ci){
        int l = EnchantmentHelper.getWitherAspect(((PlayerEntity) (Object) this));
        if (target instanceof LivingEntity) {
            if (l > 0 && !((LivingEntity)target).hasStatusEffect(StatusEffects.WITHER)) {
                ((LivingEntity) target).addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER,l*60));
            }
        }
        l = EnchantmentHelper.getPoisonAspect(((PlayerEntity) (Object) this));
        if (target instanceof LivingEntity) {
            if (l > 0 && !((LivingEntity)target).hasStatusEffect(StatusEffects.POISON)) {
                ((LivingEntity) target).addStatusEffect(new StatusEffectInstance(StatusEffects.POISON,l*60));
                ((LivingEntity) target).addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER,l*60));
            }
        }
        l = EnchantmentHelper.getFrostAspect(((PlayerEntity) (Object) this));
        if (target instanceof LivingEntity) {
            if (l > 0 && ((LivingEntity)target).canFreeze()) {
                ((LivingEntity) target).addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS,l*60));
                ((LivingEntity) target).setFrozenTicks(l*100);
            }
        }
    }
}
