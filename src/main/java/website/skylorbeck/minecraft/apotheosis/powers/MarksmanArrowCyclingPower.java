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
    Potion[] potions;
    private int activePotion = 0;
    public MarksmanArrowCyclingPower(PowerType<?> type, LivingEntity entity, Potion[] potions) {
        super(type, entity);
        this.potions = potions;
    }

    @Override
    public boolean isActive() {
        return activePotion != potions.length;
    }

    public void onUse() {
        activePotion++;
        if (activePotion > potions.length) {
            activePotion = 0;
        }
        if (!isActive()) {
            ((PlayerEntity) entity).sendMessage(new TranslatableText("apotheosis.disabled"), true);
        } else {
            ((PlayerEntity) entity).sendMessage(potions[activePotion].getEffects().get(0).getEffectType().getName(), true);
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
        return this.potions[activePotion];
    }
}