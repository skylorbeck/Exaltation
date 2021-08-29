package website.skylorbeck.minecraft.apotheosis.mixin;

import net.minecraft.enchantment.EnchantmentLevelEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EnchantmentLevelEntry.class)
public interface EnchantmentLevelEntryAccessor {
    @Mutable
    @Accessor("level")
    void setLevel(int level);
}
