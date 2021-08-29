package website.skylorbeck.minecraft.apotheosis;

import net.fabricmc.api.ModInitializer;
import website.skylorbeck.minecraft.apotheosis.conditions.ApoEntityActions;
import website.skylorbeck.minecraft.apotheosis.conditions.ApoEntityCondition;
import website.skylorbeck.minecraft.apotheosis.conditions.ApoItemCondition;
import website.skylorbeck.minecraft.apotheosis.powers.PowerFactories;

public class Apotheosis implements ModInitializer {
    @Override
    public void onInitialize() {
        Registrar.Register();
        ApoEntityCondition.register();
        ApoItemCondition.register();
        ApoEntityActions.register();
        PowerFactories.register();
    }
}

//todo powers
// --------------
// ARCANE SMITH
// purity of design - enchant no longer needs lapis
// unearthed arcana - 24000 t - free tome of knowledge