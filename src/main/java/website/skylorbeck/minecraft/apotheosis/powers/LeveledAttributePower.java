package website.skylorbeck.minecraft.apotheosis.powers;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.util.AttributedEntityAttributeModifier;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import website.skylorbeck.minecraft.apotheosis.mixin.EntityAttributeModifierMixin;

import java.util.LinkedList;
import java.util.List;

import static website.skylorbeck.minecraft.apotheosis.cardinal.ApotheosisComponents.APOXP;

public class LeveledAttributePower extends Power {

    private final List<AttributedEntityAttributeModifier> modifiers = new LinkedList<>();
    private final int tickRate;
    private final int scale;
    private double originalValue;

    public LeveledAttributePower(PowerType<?> type, LivingEntity entity, int tickRate,int scale,double originalValue) {
        super(type, entity);
        this.setTicking(true);
        this.tickRate = tickRate;
        this.scale = scale;
        this.originalValue =originalValue;
    }
    public LeveledAttributePower(PowerType<?> type, LivingEntity entity, int tickRate,int scale) {
        super(type, entity);
        this.setTicking(true);
        this.tickRate = tickRate;
        this.scale = scale;
    }

    @Override
    public void tick() {
        if (entity.age % tickRate == 0 && entity.isPlayer()) {
            float previousMaxHealth = entity.getMaxHealth();
            float previousHealthPercent = entity.getHealth() / previousMaxHealth;
            if (this.isActive()) {
                removeMods();
                addMods();
            } else {
                removeMods();
            }
            float afterMaxHealth = entity.getMaxHealth();
            if (afterMaxHealth != previousMaxHealth) {
                entity.setHealth(afterMaxHealth * previousHealthPercent);
            }
        }
    }

   /* @Override
    public void onAdded() {
        if(!entity.world.isClient) {
            float previousMaxHealth = entity.getMaxHealth();
            float previousHealthPercent = entity.getHealth() / previousMaxHealth;
            modifiers.forEach(mod -> {
                if(entity.getAttributes().hasAttribute(mod.getAttribute())) {
                    entity.getAttributeInstance(mod.getAttribute()).addTemporaryModifier(mod.getModifier());
                }
            });
            float afterMaxHealth = entity.getMaxHealth();
            if(afterMaxHealth != previousMaxHealth) {
                entity.setHealth(afterMaxHealth * previousHealthPercent);
            }
        }
    }*/

    @Override
    public void onRemoved() {
        if(!entity.world.isClient) {
            float previousMaxHealth = entity.getMaxHealth();
            float previousHealthPercent = entity.getHealth() / previousMaxHealth;
            modifiers.forEach(mod -> {
                if (entity.getAttributes().hasAttribute(mod.getAttribute())) {
                    entity.getAttributeInstance(mod.getAttribute()).removeModifier(mod.getModifier());
                }
                EntityAttributeModifier modifier = mod.getModifier();
                ((EntityAttributeModifierMixin) modifier).setValue(originalValue);
            });
            float afterMaxHealth = entity.getMaxHealth();
            if( afterMaxHealth != previousMaxHealth) {
                entity.setHealth(afterMaxHealth * previousHealthPercent);
            }
        }
    }
    @Override
    public void onLost() {
        modifiers.forEach(mod -> {
            EntityAttributeModifier modifier = mod.getModifier();
            ((EntityAttributeModifierMixin) modifier).setValue(originalValue);
        });
    }

    public LeveledAttributePower addModifier(AttributedEntityAttributeModifier modifier) {
        this.modifiers.add(modifier);
        this.originalValue = modifier.getModifier().getValue();
        return this;
    }

    public void addMods() {
        modifiers.forEach(mod -> {
            if(entity.getAttributes().hasAttribute(mod.getAttribute())) {
                EntityAttributeInstance instance = entity.getAttributeInstance(mod.getAttribute());
                if(instance != null) {
                    if(!instance.hasModifier(mod.getModifier())) {
                        EntityAttributeModifier modifier = mod.getModifier();
                        if (entity.isPlayer()) {
                            ((EntityAttributeModifierMixin) modifier).setValue(originalValue * Math.floorDiv(APOXP.get(entity).getLevel(),scale));
                        }
                        instance.addTemporaryModifier(modifier);
                        super.onAdded();
                    }
                }
            }
        });
    }

    public void removeMods() {
        modifiers.forEach(mod -> {
            if (entity.getAttributes().hasAttribute(mod.getAttribute())) {
                EntityAttributeInstance instance = entity.getAttributeInstance(mod.getAttribute());
                if(instance != null) {
                    if(instance.hasModifier(mod.getModifier())) {
                        instance.removeModifier(mod.getModifier());
                    }
                }
            }
        });
    }
}
