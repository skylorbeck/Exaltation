package website.skylorbeck.minecraft.apotheosis.powers;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.util.AttributedEntityAttributeModifier;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.TypeFilter;

import java.util.LinkedList;
import java.util.List;

import static website.skylorbeck.minecraft.apotheosis.cardinal.ApotheosisComponents.PETKEY;

public class AoEPower extends Power {

    private final List<StatusEffectInstance>  effectInstances;

    private final int distance;
    private final int tickRate;

    public AoEPower(PowerType<?> type, LivingEntity entity, int tickRate,int distance, List<StatusEffectInstance> effectInstances) {
        super(type, entity);
        this.setTicking(true);
        this.tickRate = tickRate;
        this.distance = distance;
        this.effectInstances = effectInstances;
    }

    @Override
    public void tick() {
        List<MobEntity> entityList =  entity.world.getEntitiesByType(TypeFilter.instanceOf(MobEntity.class),entity.getBoundingBox().expand(distance),(LivingEntity::isAlive));
        for (MobEntity e:entityList) {
            if(entity.age % tickRate == 0) {
                if (PETKEY.maybeGet(e).isPresent() && PETKEY.get(e).getOwnerUUID()!=null && PETKEY.get(e).getOwnerUUID().equals(entity.getUuid())){
                    continue;
                }
                    for (StatusEffectInstance effect:effectInstances) {
                    e.addStatusEffect(new StatusEffectInstance(effect));
                }
            }
        }
    }

    @Override
    public boolean isActive() {
        return true;
    }
}
