package website.skylorbeck.minecraft.apotheosis.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
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
    public void healthBoostCheck(CallbackInfo ci) {
        LivingEntity entity = ((LivingEntity) (Object) this);

        if (((Object)this) instanceof WolfEntity) {
            PetComponent petComponent = PETKEY.get(((LivingEntity) (Object) this));
            if (petComponent.getTimeLeft() >= 0) {
                petComponent.setTimeLeft(petComponent.getTimeLeft() - 1);
                if (petComponent.getTimeLeft() <= 0) {
                    LivingEntity e = ((LivingEntity) (Object) this);
                    if (e instanceof WolfEntity) {
                        try {
                        ((PlayerEntity) ((WolfEntity) e).getOwner()).sendMessage(Text.of("Pet Expired"), true);
                        e.world.playSound(null, e.getBlockPos(), SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.PLAYERS, 1.0F, e.world.random.nextFloat() * 0.1F + 0.9F);
                        } catch (Exception ignored){}
                    }
                    ((LivingEntity) (Object) this).discard();
                }
                if (petComponent.getTimeLeft() % 20 == 0) {
                    PETKEY.sync(((LivingEntity) (Object) this));
                }
            }
        }
    }

    @Inject(at = @At(value = "RETURN"),method = "isBlocking", cancellable = true)
    public void injectedIsBlocking(CallbackInfoReturnable<Boolean> cir) {
        if (PowerHolderComponent.hasPower(((LivingEntity)(Object)this), DracoKnightShieldPower.class)){
            cir.setReturnValue(true);
        }
    }

}
