package website.skylorbeck.minecraft.apotheosis.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import website.skylorbeck.minecraft.apotheosis.PlayerEntityInterface;

@Mixin(AbstractClientPlayerEntity.class)
public class AbstractClientPlayerEntityMixin {
    @Inject(at = @At(value = "RETURN"),method = "getSpeed", cancellable = true)
    private void spyGlassOverride(CallbackInfoReturnable<Float> cir){
        if (((Object)this) instanceof PlayerEntity && ((PlayerEntityInterface) this).getSpyGlassOverride()){
            cir.setReturnValue(0.1F);
        }
    }
}
