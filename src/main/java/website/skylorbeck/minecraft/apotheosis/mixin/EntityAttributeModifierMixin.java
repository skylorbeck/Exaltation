package website.skylorbeck.minecraft.apotheosis.mixin;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityAttributeModifier.class)
public interface EntityAttributeModifierMixin {
    @Mutable
    @Accessor
    void setValue(double value);
}
