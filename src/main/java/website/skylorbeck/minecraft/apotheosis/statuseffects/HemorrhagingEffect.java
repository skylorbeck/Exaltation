package website.skylorbeck.minecraft.apotheosis.statuseffects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.effect.StatusEffects;
import website.skylorbeck.minecraft.apotheosis.Declarar;

public class HemorrhagingEffect extends StatusEffect {
    public HemorrhagingEffect(StatusEffectType type, int color) {
        super(type, color);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        int k = 20 >> amplifier;
        if (k > 0) {
            return duration % k == 0;
        } else {
            return true;
        }
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (this == Declarar.HEMORRHAGING) {
            entity.damage(DamageSource.WITHER, 1.0F);
        }
    }
}