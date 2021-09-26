package website.skylorbeck.minecraft.exaltation.statuseffects;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class WolfMarkEffect extends StatusEffect {
    public WolfMarkEffect(StatusEffectCategory type, int color) {
        super(type, color);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
