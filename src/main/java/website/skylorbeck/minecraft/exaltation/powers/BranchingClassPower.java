package website.skylorbeck.minecraft.exaltation.powers;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import net.minecraft.entity.LivingEntity;

public class BranchingClassPower extends Power {
    private int level;
    public BranchingClassPower(PowerType<?> type, LivingEntity entity, int level) {
        super(type, entity);
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int scale) {
        this.level = scale;
    }
}
