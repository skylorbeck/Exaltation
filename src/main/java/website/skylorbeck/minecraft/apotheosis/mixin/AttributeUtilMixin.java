package website.skylorbeck.minecraft.apotheosis.mixin;

import io.github.apace100.apoli.util.AttributeUtil;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Mixin(AttributeUtil.class)
public class AttributeUtilMixin {
    @Inject(at = @At(value = "RETURN"),method = "applyModifiers", cancellable = true)
    private static void minDamageInject(List<EntityAttributeModifier> modifiers, double baseValue, CallbackInfoReturnable<Double> cir){
        if(modifiers != null) {
            for (EntityAttributeModifier modifier : modifiers) {
                if (modifier.getName().contains("apoMinDamage")){
                    if (cir.getReturnValue()<=0.5){
                        cir.setReturnValue(0.5D);
                    }
                }
            }
        }
    }

}
