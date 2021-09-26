package website.skylorbeck.minecraft.exaltation.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import website.skylorbeck.minecraft.exaltation.PlayerEntityInterface;

@Mixin(AbstractClientPlayerEntity.class)
public class AbstractClientPlayerEntityMixin {
    @Inject(at = @At(value = "RETURN"),method = "getSpeed", cancellable = true)
    private void spyGlassOverride(CallbackInfoReturnable<Float> cir){
        if (((AbstractClientPlayerEntity)(Object)this).isPlayer() && ((PlayerEntityInterface) this).getSpyGlassOverride()){
            cir.setReturnValue(0.1F);
        }
    }
}
