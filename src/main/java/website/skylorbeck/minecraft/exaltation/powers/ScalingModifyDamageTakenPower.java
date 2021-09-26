package website.skylorbeck.minecraft.exaltation.powers;


import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.ValueModifyingPower;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.Pair;
import website.skylorbeck.minecraft.exaltation.mixin.EntityAttributeModifierMixin;

import java.util.function.Consumer;
import java.util.function.Predicate;

import static website.skylorbeck.minecraft.exaltation.cardinal.ExaltationComponents.EXALXP;

public class ScalingModifyDamageTakenPower extends ValueModifyingPower {

    private final Predicate<Pair<DamageSource, Float>> condition;
    private final int scale;
    public final double originalValue;

    private Consumer<LivingEntity> attackerAction;
    private Consumer<LivingEntity> selfAction;

    public ScalingModifyDamageTakenPower(int scale, EntityAttributeModifier modifier, PowerType<?> type, LivingEntity entity, Predicate<Pair<DamageSource, Float>> condition) {
        super(type, entity);
        this.scale = scale;
        this.condition = condition;
        this.addModifier(modifier);
        this.originalValue = modifier.getValue();
    }

    public boolean doesApply(DamageSource source, float damageAmount) {
        return condition.test(new Pair(source, damageAmount));
    }

    public void setAttackerAction(Consumer<LivingEntity> attackerAction) {
        this.attackerAction = attackerAction;
    }

    public void setSelfAction(Consumer<LivingEntity> selfAction) {
        this.selfAction = selfAction;
    }

    public void executeActions(Entity attacker) {
        ((EntityAttributeModifierMixin)this.getModifiers().get(0)).setValue(originalValue*Math.floorDiv(EXALXP.get(this.entity).getLevel(),scale));
        if(selfAction != null) {
            selfAction.accept(entity);
        }
        if(attackerAction != null && attacker instanceof LivingEntity) {
            attackerAction.accept((LivingEntity)attacker);
        }
    }

    public int getScale() {
        return scale;
    }
}
