package website.skylorbeck.minecraft.apotheosis.powers;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import net.minecraft.entity.LivingEntity;

public class SmithingWeaponPower extends Power {
    private int scale;
    public SmithingWeaponPower(PowerType<?> type, LivingEntity entity, int scale) {
        super(type, entity);
        this.scale = scale;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }
}
