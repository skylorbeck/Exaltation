package website.skylorbeck.minecraft.apotheosis.statuseffects;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;

public class WolfMarkEffect extends StatusEffect {
    public WolfMarkEffect(StatusEffectType type, int color) {
        super(type, color);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
