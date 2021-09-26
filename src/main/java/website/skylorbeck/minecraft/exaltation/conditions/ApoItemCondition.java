package website.skylorbeck.minecraft.exaltation.conditions;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.util.registry.Registry;
import website.skylorbeck.minecraft.exaltation.Declarar;

public class ApoItemCondition {
    public static void register() {
        register(new ConditionFactory<>(Declarar.getIdentifier("durability"), new SerializableData()
                .add("percent", SerializableDataTypes.FLOAT),
                (data, stack) -> {
                    int durability = 0;
                    int maxDurability = 0;
                    if (stack.getItem() instanceof ToolItem) {
                        durability = stack.getMaxDamage() - stack.getDamage();
                        maxDurability = stack.getMaxDamage();
                    }
                    return Comparison.GREATER_THAN_OR_EQUAL.compare(durability, maxDurability * (data.getFloat("percent")));
                }));
    }

    private static void register(ConditionFactory<ItemStack> conditionFactory) {
        Registry.register(ApoliRegistries.ITEM_CONDITION, conditionFactory.getSerializerId(), conditionFactory);
    }
}
