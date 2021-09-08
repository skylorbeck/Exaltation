package website.skylorbeck.minecraft.apotheosis.data;

import io.github.apace100.apoli.util.AttributedEntityAttributeModifier;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;

import java.util.List;

public class ApoDataTypes {
    public static final SerializableDataType<List<String>> STRINGS =
            SerializableDataType.list(SerializableDataTypes.STRING);

    public static final SerializableDataType<QuiverData> QUIVER  = SerializableDataType.compound(
            QuiverData.class,
            new SerializableData()
                    .add("doDamage", SerializableDataTypes.BOOLEAN,true)
                    .add("potions", ApoDataTypes.STRINGS),

            dataInst -> new QuiverData(dataInst.getBoolean("doDamage"),(List<String>) dataInst.get("potions") )
            ,
            (data, inst) -> {
                SerializableData.Instance dataInst = data.new Instance();
                dataInst.set("doDamage",inst.isDoDamage());
                dataInst.set("potions",inst.getPotions());
                return dataInst;
            });

    public static final SerializableDataType<List<QuiverData>> QUIVERS =
            SerializableDataType.list(QUIVER);
}
