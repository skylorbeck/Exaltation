package website.skylorbeck.minecraft.apotheosis.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import website.skylorbeck.minecraft.apotheosis.cardinal.PetComponent;
import website.skylorbeck.minecraft.apotheosis.powers.DracoKnightShieldPower;

import static website.skylorbeck.minecraft.apotheosis.cardinal.ApotheosisComponents.PETKEY;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(at = @At("TAIL"),method = "tick")
    public void injectedTick(CallbackInfo ci) {
        LivingEntity entity = ((LivingEntity) (Object) this);
        if (((Object)this) instanceof MobEntity) {
            PetComponent petComponent = PETKEY.get(entity);
            if (petComponent.getTimeLeft() >= 0 && petComponent.getOwnerUUID() !=null) {
                petComponent.setTimeLeft(petComponent.getTimeLeft() - 1);
                if (petComponent.getTimeLeft() <= 0) {
                        try {
                            entity.world.getPlayerByUuid(petComponent.getOwnerUUID()).sendMessage(Text.of("Pet Expired"), true);
                            entity.world.playSound(null, entity.getBlockPos(), SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.PLAYERS, 1.0F, entity.world.random.nextFloat() * 0.1F + 0.9F);
                        } catch (Exception ignored){}
                    entity.discard();
                }
                if (petComponent.getTimeLeft() % 20 == 0) {
                    PETKEY.sync(entity);
                }
            }
        }
        if (entity.isPlayer() && entity.getGroup()== EntityGroup.UNDEAD && entity.world.isDay() && !entity.world.isClient) {
            float f = entity.getBrightnessAtEyes();
            BlockPos blockPos = new BlockPos(entity.getX(), entity.getEyeY(), entity.getZ());
            boolean bl = entity.isWet() || entity.inPowderSnow || entity.wasInPowderSnow;
            if (f > 0.5F && entity.world.random.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && !bl && entity.world.isSkyVisible(blockPos)) {
                entity.setOnFireFor(8);
            }
        }
    }

    @Inject(at = @At(value = "RETURN"),method = "isBlocking", cancellable = true)
    public void injectedIsBlocking(CallbackInfoReturnable<Boolean> cir) {
        if (PowerHolderComponent.hasPower(((LivingEntity)(Object)this), DracoKnightShieldPower.class)){
            if (!PowerHolderComponent.KEY.get(((LivingEntity)(Object)this)).getPowers(DracoKnightShieldPower.class).get(0).canUse()){
                cir.setReturnValue(true);
            }
        }
    }

}
