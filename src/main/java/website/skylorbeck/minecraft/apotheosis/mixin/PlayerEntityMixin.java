package website.skylorbeck.minecraft.apotheosis.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import website.skylorbeck.minecraft.apotheosis.Declarar;
import website.skylorbeck.minecraft.apotheosis.enchantment.EnchantmentHelper;
import website.skylorbeck.minecraft.apotheosis.powers.DruidWolfBondPower;

import static website.skylorbeck.minecraft.apotheosis.cardinal.ApotheosisComponents.APOXP;
import static website.skylorbeck.minecraft.apotheosis.cardinal.ApotheosisComponents.PETKEY;

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

    @Inject(at = @At("TAIL"),method = "tick")
    public void healthBoostCheck(CallbackInfo ci) {
        LivingEntity entity = ((PlayerEntity) (Object) this);
        //todo config for time checked
        if (entity.age % 20 == 0) {
            float previousMaxHealth = entity.getMaxHealth();
            float previousHealthPercent = entity.getHealth() / previousMaxHealth;

            if (net.minecraft.enchantment.EnchantmentHelper.getEquipmentLevel(Declarar.HEALTHBOOST, entity) > 0) {
                if (entity.getAttributes().hasAttribute(EntityAttributes.GENERIC_MAX_HEALTH)) {
                    EntityAttributeInstance instance = entity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
                    if (instance != null) {
                        if (!instance.hasModifier(Declarar.healthBoostEAM(entity))) {
                            instance.addTemporaryModifier(Declarar.healthBoostEAM(entity));
                        } else if (instance.getModifier(Declarar.healthBoostUUID).getValue() != Declarar.healthBoostEAM(entity).getValue()) {
                            instance.removeModifier(Declarar.healthBoostUUID);
                            instance.addTemporaryModifier(Declarar.healthBoostEAM(entity));
                        }
                    }
                }
            } else {
                if (entity.getAttributes().hasAttribute(EntityAttributes.GENERIC_MAX_HEALTH)) {
                    EntityAttributeInstance instance = entity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
                    if (instance != null) {
                        if (instance.hasModifier(Declarar.healthBoostEAM(entity))) {
                            instance.removeModifier(Declarar.healthBoostUUID);
                        }
                    }
                }
            }

            float afterMaxHealth = entity.getMaxHealth();
            if (afterMaxHealth != previousMaxHealth) {
                entity.setHealth(afterMaxHealth * previousHealthPercent);
            }


            if (net.minecraft.enchantment.EnchantmentHelper.getEquipmentLevel(Declarar.KNOCKBACKRESIST, entity) > 0) {
                if (entity.getAttributes().hasAttribute(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE)) {
                    EntityAttributeInstance instance = entity.getAttributeInstance(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE);
                    if (instance != null) {
                        if (!instance.hasModifier(Declarar.knockbackResistEAM(entity))) {
                            instance.addTemporaryModifier(Declarar.knockbackResistEAM(entity));
                        } else if (instance.getModifier(Declarar.knockbackResistUUID).getValue() != Declarar.knockbackResistEAM(entity).getValue()) {
                            instance.removeModifier(Declarar.knockbackResistUUID);
                            instance.addTemporaryModifier(Declarar.knockbackResistEAM(entity));
                        }
                    }
                }
            } else {
                if (entity.getAttributes().hasAttribute(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE)) {
                    EntityAttributeInstance instance = entity.getAttributeInstance(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE);
                    if (instance != null) {
                        if (instance.hasModifier(Declarar.knockbackResistEAM(entity))) {
                            instance.removeModifier(Declarar.knockbackResistUUID);
                        }
                    }
                }
            }


            if (net.minecraft.enchantment.EnchantmentHelper.getEquipmentLevel(Declarar.ARMORSHARPNESS, entity) > 0) {
                if (entity.getAttributes().hasAttribute(EntityAttributes.GENERIC_ATTACK_DAMAGE)) {
                    EntityAttributeInstance instance = entity.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE);
                    if (instance != null) {
                        if (!instance.hasModifier(Declarar.armorsharpnessEAM(entity))) {
                            instance.addTemporaryModifier(Declarar.armorsharpnessEAM(entity));
                        } else if (instance.getModifier(Declarar.armorsharpnessUUID).getValue() != Declarar.armorsharpnessEAM(entity).getValue()) {
                            instance.removeModifier(Declarar.armorsharpnessUUID);
                            instance.addTemporaryModifier(Declarar.armorsharpnessEAM(entity));
                        }
                    }
                }
            } else {
                if (entity.getAttributes().hasAttribute(EntityAttributes.GENERIC_ATTACK_DAMAGE)) {
                    EntityAttributeInstance instance = entity.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE);
                    if (instance != null) {
                        if (instance.hasModifier(Declarar.armorsharpnessEAM(entity))) {
                            instance.removeModifier(Declarar.armorsharpnessUUID);
                        }
                    }
                }
            }


            if (net.minecraft.enchantment.EnchantmentHelper.getEquipmentLevel(Declarar.SPEEDBOOSTER, entity) > 0) {
                if (entity.getAttributes().hasAttribute(EntityAttributes.GENERIC_MOVEMENT_SPEED)) {
                    EntityAttributeInstance instance = entity.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
                    if (instance != null) {
                        if (!instance.hasModifier(Declarar.speedboosterEAM(entity))) {
                            instance.addTemporaryModifier(Declarar.speedboosterEAM(entity));
                        } else if (instance.getModifier(Declarar.speedboosterUUID).getValue() != Declarar.speedboosterEAM(entity).getValue()) {
                            instance.removeModifier(Declarar.speedboosterUUID);
                            instance.addTemporaryModifier(Declarar.speedboosterEAM(entity));
                        }
                    }
                }
            } else {
                if (entity.getAttributes().hasAttribute(EntityAttributes.GENERIC_MOVEMENT_SPEED)) {
                    EntityAttributeInstance instance = entity.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
                    if (instance != null) {
                        if (instance.hasModifier(Declarar.speedboosterEAM(entity))) {
                            instance.removeModifier(Declarar.speedboosterUUID);
                        }
                    }
                }
            }
        }
        if (PowerHolderComponent.hasPower(entity, DruidWolfBondPower.class) && APOXP.get(entity).getPetUUID() != null) {
            TargetPredicate predicate = TargetPredicate.DEFAULT;
            predicate.setPredicate((pet -> pet instanceof WolfEntity && PETKEY.get(pet).getOwnerUUID() == entity.getUuid()));
            WolfEntity oldPet = entity.world.getClosestEntity(WolfEntity.class, predicate, (LivingEntity) entity, entity.getX(), entity.getY(), entity.getZ(), entity.getBoundingBox().expand(5D));
            if (oldPet != null) {
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE,20));
            }
        }
    }
}
