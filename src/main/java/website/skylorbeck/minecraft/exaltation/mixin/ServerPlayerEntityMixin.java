package website.skylorbeck.minecraft.exaltation.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import website.skylorbeck.minecraft.exaltation.powers.ScalingModifyDamageTakenPower;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    @ModifyArg(
            method = {"damage"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"
            )
    )
    private float modifyDamageAmountApo(DamageSource source, float originalAmount) {
        return PowerHolderComponent.modify((ServerPlayerEntity)(Object)this, ScalingModifyDamageTakenPower.class, originalAmount,(p) -> {
            return p.doesApply(source, originalAmount);
        }, (p) -> {
            p.executeActions(source.getAttacker());
        });
    }
}
