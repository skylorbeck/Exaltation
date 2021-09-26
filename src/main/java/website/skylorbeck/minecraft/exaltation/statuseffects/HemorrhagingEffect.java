package website.skylorbeck.minecraft.exaltation.statuseffects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import website.skylorbeck.minecraft.exaltation.Declarar;

public class HemorrhagingEffect extends StatusEffect {
    public HemorrhagingEffect(StatusEffectCategory type, int color) {
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
            entity.damage(Declarar.HEMORRHAGE, 1+(1.0F*entity.world.getDifficulty().getId()));
        }
    }
}
