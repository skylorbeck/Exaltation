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
    private final double originalValue;
    private int playerLevelLast = 0;

    public LeveledAttributePower(PowerType<?> type, LivingEntity entity, int tickRate,int scale,double originalValue) {
        super(type, entity);
        this.setTicking(true);
        this.tickRate = tickRate;
        this.scale = scale;
        this.originalValue =originalValue;

    }

    @Override
    public void tick() {
        if(entity.age % tickRate == 0 && entity instanceof PlayerEntity) {
            float previousMaxHealth = entity.getMaxHealth();
            float previousHealthPercent = entity.getHealth() / previousMaxHealth;
            int lvl = APOXP.get(entity).getLevel();
            if (playerLevelLast != lvl){
                playerLevelLast = lvl;
                removeMods();
                addMods();
            }
            float afterMaxHealth = entity.getMaxHealth();
            if(afterMaxHealth != previousMaxHealth) {
                entity.setHealth(afterMaxHealth * previousHealthPercent);
            }
        }
    }

    @Override
    public void onRemoved() {
        removeMods();
    }

    public LeveledAttributePower addModifier(AttributedEntityAttributeModifier modifier) {
        this.modifiers.add(modifier);
        return this;
    }

    public void addMods() {
        modifiers.forEach(mod -> {
            if(entity.getAttributes().hasAttribute(mod.getAttribute())) {
                EntityAttributeInstance instance = entity.getAttributeInstance(mod.getAttribute());
                if(instance != null) {
                    if(!instance.hasModifier(mod.getModifier())) {
                        EntityAttributeModifier modifier = mod.getModifier();
                        if (entity instanceof PlayerEntity) {
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
        float previousMaxHealth = entity.getMaxHealth();
        float previousHealthPercent = entity.getHealth() / previousMaxHealth;
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
        float afterMaxHealth = entity.getMaxHealth();
        if(afterMaxHealth != previousMaxHealth) {
            entity.setHealth(afterMaxHealth * previousHealthPercent);
        }
    }
}
