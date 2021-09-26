package website.skylorbeck.minecraft.exaltation.powers;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import net.minecraft.entity.LivingEntity;

public class RangerRangedItemAccuracyPower extends Power {
    float accuracy;
    int scale;
    boolean bow;
    boolean crossbow;
    public RangerRangedItemAccuracyPower(PowerType<?> type, LivingEntity entity, float accuracy, boolean bow, boolean crossbow, int scale) {
        super(type, entity);
        this.accuracy = accuracy;
        this.scale = scale;
        this.bow = bow;
        this.crossbow = crossbow;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public int getScale() {
        return scale;
    }

    public boolean isBow() {
        return bow;
    }

    public boolean isCrossbow() {
        return crossbow;
    }
}
