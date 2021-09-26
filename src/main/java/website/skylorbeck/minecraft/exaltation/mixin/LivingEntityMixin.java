package website.skylorbeck.minecraft.exaltation.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import website.skylorbeck.minecraft.exaltation.cardinal.PetComponent;
import website.skylorbeck.minecraft.exaltation.cardinal.XPComponent;
import website.skylorbeck.minecraft.exaltation.powers.DracoKnightShieldPower;
import website.skylorbeck.minecraft.exaltation.powers.TotemPetPower;

import java.util.UUID;

import static website.skylorbeck.minecraft.exaltation.cardinal.ExaltationComponents.APOXP;
import static website.skylorbeck.minecraft.exaltation.cardinal.ExaltationComponents.PETKEY;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow
    public abstract EntityGroup getGroup();

    @Inject(at = @At("TAIL"), method = "tick")
    public void injectedTick(CallbackInfo ci) {
        LivingEntity entity = ((LivingEntity) (Object) this);
        if (((Object) this) instanceof MobEntity) {
            PetComponent petComponent = PETKEY.get(entity);
            if (petComponent.getTimeLeft() >= 0 && petComponent.getOwnerUUID() != null) {
                petComponent.setTimeLeft(petComponent.getTimeLeft() - 1);
                if (petComponent.getTimeLeft() <= 0) {
                    try {
                        entity.world.getPlayerByUuid(petComponent.getOwnerUUID()).sendMessage(Text.of("Pet Expired"), true);
                        entity.world.playSound(null, entity.getBlockPos(), SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.PLAYERS, 1.0F, entity.world.random.nextFloat() * 0.1F + 0.9F);
                    } catch (Exception ignored) {
                    }
                    entity.discard();
                }
                if (petComponent.getTimeLeft() % 20 == 0) {
                    PETKEY.sync(entity);
                }
            }
        }
//        if (entity.isPlayer() && entity.getGroup()== EntityGroup.UNDEAD && entity.world.isDay() && !entity.world.isClient) {
//            float f = entity.getBrightnessAtEyes();
//            BlockPos blockPos = new BlockPos(entity.getX(), entity.getEyeY(), entity.getZ());
//            boolean bl = entity.isWet() || entity.inPowderSnow || entity.wasInPowderSnow;
//            if (f > 0.5F && entity.world.random.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && !bl && entity.world.isSkyVisible(blockPos)) {
//                entity.setOnFireFor(8);
//            }
//        }
    }

    @Inject(at = @At(value = "RETURN"), method = "isBlocking", cancellable = true)
    public void injectedIsBlocking(CallbackInfoReturnable<Boolean> cir) {
        if (PowerHolderComponent.hasPower(((LivingEntity) (Object) this), DracoKnightShieldPower.class)) {
            if (!PowerHolderComponent.KEY.get(((LivingEntity) (Object) this)).getPowers(DracoKnightShieldPower.class).get(0).canUse()) {
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(at = @At(value = "RETURN"), method = "canTarget(Lnet/minecraft/entity/LivingEntity;)Z", cancellable = true)
    public void ignoreUndead(LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
        if (target.isPlayer() && ((LivingEntity) (Object) this).getGroup().equals(EntityGroup.UNDEAD) && target.getGroup().equals(EntityGroup.UNDEAD)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(at = @At(value = "RETURN"), method = "onAttacking", cancellable = true)
    public void ignoreUndead(Entity target, CallbackInfo ci) {
        PetComponent petComponent = PETKEY.maybeGet(this).isPresent() ? PETKEY.get(this) : null;
        if (petComponent != null && petComponent.shouldHealOwner()) {
            PlayerEntity pe = target.world.getPlayerByUuid(petComponent.getOwnerUUID());
            if (pe != null)
                pe.heal((float) (((LivingEntity) (Object) this).getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) / 2));
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;tryUseTotem(Lnet/minecraft/entity/damage/DamageSource;)Z"), method = "damage", cancellable = true)
    public void killPetSavePlayer(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source.isOutOfWorld()) {

        } else {
            LivingEntity entity = ((LivingEntity) (Object) this);
            if (entity.isPlayer() && APOXP.get(entity).getPetUUID() != null) {

                XPComponent xpComponent = APOXP.get(entity);
                UUID[] pets = xpComponent.getPetUUID();
                if (pets.length > 0 && xpComponent.getLevel()>=50 && PowerHolderComponent.hasPower(entity, TotemPetPower.class)) {
                    TargetPredicate predicate = TargetPredicate.DEFAULT;
                    predicate.setPredicate((pet -> {
                        for (UUID uuid : pets) {
                            if (pet.getUuid().equals(uuid)) {
                                return true;
                            }
                        }
                        return false;
                    }));
                    try {
                        LivingEntity oldPet = entity.world.getClosestEntity(MobEntity.class, predicate, (LivingEntity) entity, entity.getX(), entity.getY(), entity.getZ(), entity.getBoundingBox().expand(100D));
                        if (oldPet != null) {
                            oldPet.discard();
                        }
                    } catch (Exception ignored) {
                    }
                    xpComponent.setPetUUID(ArrayUtils.remove(APOXP.get(entity).getPetUUID(), 0));

                    entity.setHealth(1.0F);
                    entity.clearStatusEffects();
                    entity.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 900, 1));
                    entity.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 1));
                    entity.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 800, 0));
                    entity.world.sendEntityStatus(entity, (byte) 35);
                    APOXP.sync(entity);
                    cir.cancel();
                }
            }
        }
    }
}
