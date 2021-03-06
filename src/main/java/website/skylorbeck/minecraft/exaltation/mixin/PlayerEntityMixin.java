package website.skylorbeck.minecraft.exaltation.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import website.skylorbeck.minecraft.exaltation.Declarar;
import website.skylorbeck.minecraft.exaltation.PlayerEntityInterface;
import website.skylorbeck.minecraft.exaltation.enchantment.EnchantmentHelper;
import website.skylorbeck.minecraft.exaltation.powers.DruidWolfBondPower;

import static website.skylorbeck.minecraft.exaltation.cardinal.ExaltationComponents.EXALXP;
import static website.skylorbeck.minecraft.exaltation.cardinal.ExaltationComponents.PETKEY;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements PlayerEntityInterface {
    private boolean spyGlassOverride = false;
    private boolean dracoSlam = false;
    private boolean dracoFallImmune = false;

    @Inject(at = @At(value = "INVOKE",target = "Lnet/minecraft/enchantment/EnchantmentHelper;getFireAspect(Lnet/minecraft/entity/LivingEntity;)I"),method = "attack",cancellable = true)
    public void injectedHasEnchantments(Entity target, CallbackInfo ci){
        int aspect = EnchantmentHelper.getWitherAspect(((PlayerEntity) (Object) this));
        if (target instanceof LivingEntity) {
            if (aspect > 0 && !((LivingEntity)target).hasStatusEffect(StatusEffects.WITHER)) {
                ((LivingEntity) target).addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER,aspect*60));
            }
        }
        aspect = EnchantmentHelper.getPoisonAspect(((PlayerEntity) (Object) this));
        if (target instanceof LivingEntity) {
            if (aspect > 0 && !((LivingEntity)target).hasStatusEffect(StatusEffects.POISON)) {
                ((LivingEntity) target).addStatusEffect(new StatusEffectInstance(StatusEffects.POISON,aspect*60));
                ((LivingEntity) target).addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER,aspect*60));
            }
        }
        aspect = EnchantmentHelper.getFrostAspect(((PlayerEntity) (Object) this));
        if (target instanceof LivingEntity) {
            if (aspect > 0 && ((LivingEntity)target).canFreeze()) {
                ((LivingEntity) target).addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS,aspect*60));
                ((LivingEntity) target).setFrozenTicks(aspect*100);
            }
        }
    }

    @Redirect(at = @At(value = "INVOKE",target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"),method = "attack")
    private boolean injectedAttack(Entity entity, DamageSource source, float amount){
        if (entity instanceof LivingEntity && ((LivingEntity)entity).hasStatusEffect(Declarar.WOLFMARK))
            return entity.damage(source, (float) (amount*1.5));
        else return entity.damage(source,amount);
    }

    @Inject(at = @At("TAIL"),method = "tick")
    public void healthBoostCheck(CallbackInfo ci) {
        PlayerEntity entity = ((PlayerEntity) (Object) this);
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
        if (PowerHolderComponent.hasPower(entity, DruidWolfBondPower.class) && EXALXP.get(entity).getPetUUID() != null) {
            TargetPredicate predicate = TargetPredicate.DEFAULT;
            predicate.setPredicate((pet -> pet instanceof WolfEntity && PETKEY.get(pet).getOwnerUUID() == entity.getUuid()));
            WolfEntity oldPet = entity.world.getClosestEntity(WolfEntity.class, predicate, (LivingEntity) entity, entity.getX(), entity.getY(), entity.getZ(), entity.getBoundingBox().expand(5D));
            if (oldPet != null) {
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 20));
            }
        }
        if (getSpyGlassOverride() && !entity.getStackInHand(Hand.MAIN_HAND).isOf(Items.CROSSBOW)) {
            setSpyGlassOveride(false);
        }
    }


    @Inject(at = @At(value = "RETURN"),method = "isUsingSpyglass", cancellable = true)
    private void spyGlassOverride(CallbackInfoReturnable<Boolean> cir){
        if (spyGlassOverride)
        cir.setReturnValue(true);
    }
    @Inject(at = @At(value = "INVOKE"),method = "handleFallDamage", cancellable = true)
    private void dracoSlam(float fallDistance, float damageMultiplier, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        if (dracoFallImmune) {
            if (dracoSlam) {
                PlayerEntity entity = ((PlayerEntity) (Object) this);
                setDracoSlam(false);
                entity.world.createExplosion(entity, entity.getX(), entity.getY(), entity.getZ(), 5, false, Explosion.DestructionType.NONE);
            }
            setDracoFallImmune(false);
            cir.cancel();
        }
    }

    @Override
    public void setSpyGlassOveride(boolean bool) {
        this.spyGlassOverride = bool;
    }

    @Override
    public boolean getSpyGlassOverride() {
        return this.spyGlassOverride;
    }

    @Override
    public void setDracoSlam(boolean bool) {
        this.dracoSlam = bool;
    }

    @Override
    public void setDracoFallImmune(boolean bool) {
        this.dracoFallImmune = bool;
    }

    @Override
    public boolean getDracoslam() {
        return this.dracoSlam;
    }
}
