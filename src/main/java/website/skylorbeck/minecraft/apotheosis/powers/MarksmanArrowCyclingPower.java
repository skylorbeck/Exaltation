package website.skylorbeck.minecraft.apotheosis.powers;

import io.github.apace100.apoli.power.Active;
import io.github.apace100.apoli.power.ActiveCooldownPower;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.util.HudRender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Potion;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.function.Consumer;

public class MarksmanArrowCyclingPower extends Power implements Active {
    private final Potion[][] potions;
    private final boolean[] doDamage;
    private int activePotion = 0;
    private int activeQuiver = 0;

    public MarksmanArrowCyclingPower(PowerType<?> type, LivingEntity entity, Potion[][] potions) {
        this(type,entity,potions,new boolean[]{false});
    }

    public MarksmanArrowCyclingPower(PowerType<?> type, LivingEntity entity, Potion[][] potions, boolean[] doDamage) {
        super(type, entity);
        this.potions = potions;
        this.activePotion = potions[0].length;
        this.doDamage = doDamage;
    }

    @Override
    public boolean isActive() {
        return activePotion != potions[activeQuiver].length;
    }

    public void onUse() {
        if (entity.isSneaking()) {
            activeQuiver++;
            if (activeQuiver >= potions.length) {
                activeQuiver = 0;
            }
            activePotion = potions[activeQuiver].length;
            if (potions.length>1) {
                ((PlayerEntity) entity).sendMessage(new TranslatableText("apotheosis.quiver_swap"), true);
            } else {
                ((PlayerEntity) entity).sendMessage(new TranslatableText("apotheosis.disabled"), true);
            }

        } else {
            activePotion++;
            if (activePotion > potions[activeQuiver].length) {
                activePotion = 0;
            }
            if (!isActive()) {
                ((PlayerEntity) entity).sendMessage(new TranslatableText("apotheosis.disabled"), true);
            } else {
                ((PlayerEntity) entity).sendMessage(potions[activeQuiver][activePotion].getEffects().get(0).getEffectType().getName(), true);
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

    public Potion getPotion(){
        return this.potions[activeQuiver][activePotion];
    }
    public boolean doDamage(){
        return this.doDamage[activeQuiver];
    }
}