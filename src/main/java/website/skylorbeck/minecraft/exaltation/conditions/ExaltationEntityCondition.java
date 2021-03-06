package website.skylorbeck.minecraft.exaltation.conditions;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.registry.Registry;
import website.skylorbeck.minecraft.exaltation.Declarar;

import static website.skylorbeck.minecraft.exaltation.cardinal.ExaltationComponents.EXALXP;

public class ExaltationEntityCondition {
    @SuppressWarnings("unchecked")
    public static void register() {
        register(new ConditionFactory<>(Declarar.getIdentifier("level"), new SerializableData()
                        .add("comparison", ApoliDataTypes.COMPARISON)
                        .add("compare_to", SerializableDataTypes.INT),
                (data, entity) -> {
                    if(entity.isPlayer()) {
                        return ((Comparison)data.get("comparison")).compare(EXALXP.get(((PlayerEntity)entity)).getLevel(), data.getInt("compare_to"));
                    }
                    return false;
                }
        ));
    }
    private static void register(ConditionFactory<LivingEntity> conditionFactory) {
        Registry.register(ApoliRegistries.ENTITY_CONDITION, conditionFactory.getSerializerId(), conditionFactory);
    }
}
