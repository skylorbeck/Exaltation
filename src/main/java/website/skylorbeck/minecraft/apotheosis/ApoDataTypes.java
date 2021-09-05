package website.skylorbeck.minecraft.apotheosis;

import io.github.apace100.apoli.util.AttributedEntityAttributeModifier;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;

import java.util.List;

public class ApoDataTypes {
    public static final SerializableDataType<List<String>> STRINGS =
            SerializableDataType.list(SerializableDataTypes.STRING);
}
