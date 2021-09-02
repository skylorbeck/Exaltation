package website.skylorbeck.minecraft.apotheosis.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import website.skylorbeck.minecraft.apotheosis.Declarar;
import website.skylorbeck.minecraft.apotheosis.enchantment.EnchantmentHelper;
import website.skylorbeck.minecraft.apotheosis.powers.RangerDamagePower;

import static website.skylorbeck.minecraft.apotheosis.cardinal.ApotheosisComponents.APOXP;

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
    @ModifyVariable(name = "f",at = @At(value = "INVOKE",target = "Lnet/minecraft/entity/Entity;getVelocity()Lnet/minecraft/util/math/Vec3d;"),method = "attack")
    private float injectedAttack(float f,Entity target){
        if (target instanceof LivingEntity){
            if (((LivingEntity)target).hasStatusEffect(Declarar.WOLFMARK)){
                f+=f/2;
            }
        }
        return f;
    }
}
