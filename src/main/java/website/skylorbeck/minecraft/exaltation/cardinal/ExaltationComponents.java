package website.skylorbeck.minecraft.exaltation.cardinal;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.entity.mob.MobEntity;
import website.skylorbeck.minecraft.exaltation.Declarar;

public class ExaltationComponents implements EntityComponentInitializer{
    public static final ComponentKey<XPComponent> APOXP =
            ComponentRegistry.getOrCreate(Declarar.getIdentifier("xp"), XPComponent.class);
    public static final ComponentKey<PetComponent> PETKEY =
            ComponentRegistry.getOrCreate(Declarar.getIdentifier("petkey"),PetComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(APOXP, XPComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerFor(MobEntity.class,PETKEY, PetComponent::new);
    }
}
