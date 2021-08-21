package website.skylorbeck.minecraft.apotheosis.cardinal;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.util.Identifier;

public class ApotheosisComponents implements EntityComponentInitializer{
    public static final ComponentKey<XPComponent> APOXP =
            ComponentRegistry.getOrCreate(new Identifier("apotheosis:xp"), XPComponent.class);
    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(APOXP, XPComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
    }
}
