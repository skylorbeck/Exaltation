package website.skylorbeck.minecraft.apotheosis.powers;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.apoli.util.AttributedEntityAttributeModifier;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.List;

public class PowerFactories {
    public static void register(){
        register(new PowerFactory<>(new Identifier("apotheosis","leveled_attribute"),
                new SerializableData()
                        .add("modifier", ApoliDataTypes.ATTRIBUTED_ATTRIBUTE_MODIFIER, null)
                        .add("modifiers", ApoliDataTypes.ATTRIBUTED_ATTRIBUTE_MODIFIERS, null)
                        .add("tick_rate", SerializableDataTypes.INT, 20),
                data ->
                        (type, player) -> {
                            LeveledAttributePower ap = new LeveledAttributePower(type, player, data.getInt("tick_rate"));
                            if(data.isPresent("modifier")) {
                                ap.addModifier((AttributedEntityAttributeModifier)data.get("modifier"));
                            }
                            if(data.isPresent("modifiers")) {
                                List<AttributedEntityAttributeModifier> modifierList = (List<AttributedEntityAttributeModifier>)data.get("modifiers");
                                modifierList.forEach(ap::addModifier);
                            }
                            return ap;
                        }).allowCondition());
    }
    private static void register(PowerFactory serializer) {
        Registry.register(ApoliRegistries.POWER_FACTORY, serializer.getSerializerId(), serializer);
    }
}
