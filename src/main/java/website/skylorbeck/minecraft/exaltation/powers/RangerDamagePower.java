package website.skylorbeck.minecraft.exaltation.powers;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import net.minecraft.entity.LivingEntity;

public class RangerDamagePower extends Power {
    float damage;
    float damageScaled;
    int scale;

    public RangerDamagePower(PowerType<?> type, LivingEntity entity, float damage, float damage_scaled, int scale) {
        super(type, entity);
        this.damage = damage;
        this.damageScaled = damage_scaled;
        this.scale = scale;
    }

    public float getDamage() {
        return damage;
    }

    public float getDamageScaled() {
        return damageScaled;
    }

    public int getScale() {
        return scale;
    }
}