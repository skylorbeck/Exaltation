package website.skylorbeck.minecraft.apotheosis.powers;

import io.github.apace100.apoli.power.Active;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Potion;
import net.minecraft.text.TranslatableText;

public class MarksmanArrowCyclingPower extends Power implements Active {
    private final StatusEffectInstance[][] statusEffectInstances;
    private final boolean[] doDamage;
    private int activePotion = 0;
    private int activeQuiver = 0;

    public MarksmanArrowCyclingPower(PowerType<?> type, LivingEntity entity, StatusEffectInstance[][] statusEffectInstances) {
        this(type,entity,statusEffectInstances,new boolean[]{false});
    }

    public MarksmanArrowCyclingPower(PowerType<?> type, LivingEntity entity, StatusEffectInstance[][] statusEffectInstances, boolean[] doDamage) {
        super(type, entity);
        this.statusEffectInstances = statusEffectInstances;
        this.activePotion = statusEffectInstances[0].length;
        this.doDamage = doDamage;
    }

    @Override
    public boolean isActive() {
        return activePotion != statusEffectInstances[activeQuiver].length;
    }

    public void onUse() {
        if (entity.isSneaking()) {
            activeQuiver++;
            if (activeQuiver >= statusEffectInstances.length) {
                activeQuiver = 0;
            }
            activePotion = statusEffectInstances[activeQuiver].length;
            if (statusEffectInstances.length>1) {
                ((PlayerEntity) entity).sendMessage(new TranslatableText("apotheosis.quiver_swap"), true);
            } else {
                ((PlayerEntity) entity).sendMessage(new TranslatableText("apotheosis.disabled"), true);
            }

        } else {
            activePotion++;
            if (activePotion > statusEffectInstances[activeQuiver].length) {
                activePotion = 0;
            }
            if (!isActive()) {
                ((PlayerEntity) entity).sendMessage(new TranslatableText("apotheosis.disabled"), true);
            } else {
                ((PlayerEntity) entity).sendMessage(statusEffectInstances[activeQuiver][activePotion].getEffectType().getName(), true);
            }
        }
    }

    private Active.Key key;

    @Override
    public Active.Key getKey() {
        return key;
    }

    @Override
    public void setKey(Active.Key key) {
        this.key = key;
    }

    public StatusEffectInstance getStatusEffect(){
        return this.statusEffectInstances[activeQuiver][activePotion];
    }
    public boolean doDamage(){
        return this.doDamage[activeQuiver];
    }
}