package website.skylorbeck.minecraft.apotheosis.powers;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import net.minecraft.command.argument.ItemStringReader;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ConsumingItemPower extends Power {
    private final Item item;
    public ConsumingItemPower(PowerType<?> type, LivingEntity entity, String itemID) {
        super(type, entity);
        this.item = Registry.ITEM.get(new Identifier(itemID));
    }

    public Item getItem() {
        return item;
    }
}
