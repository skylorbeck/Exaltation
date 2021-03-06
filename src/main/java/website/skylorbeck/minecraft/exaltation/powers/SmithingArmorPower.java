package website.skylorbeck.minecraft.exaltation.powers;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import net.minecraft.entity.LivingEntity;

public class SmithingArmorPower extends Power {
    private int scale;
    public SmithingArmorPower(PowerType<?> type, LivingEntity entity,int scale) {
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
