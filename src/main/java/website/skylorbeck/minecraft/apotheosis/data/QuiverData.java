package website.skylorbeck.minecraft.apotheosis.data;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;

import java.util.List;

public class QuiverData {
    boolean doDamage = false;
    List<StatusEffectInstance> effects;

    public QuiverData(boolean doDamage,  List<StatusEffectInstance> effects) {
        this.doDamage = doDamage;
        this.effects = effects;
    }

    public boolean isDoDamage() {
        return doDamage;
    }

    public  List<StatusEffectInstance> getEffects() {
        return effects;
    }
}
