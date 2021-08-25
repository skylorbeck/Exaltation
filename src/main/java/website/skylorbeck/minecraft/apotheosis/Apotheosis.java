package website.skylorbeck.minecraft.apotheosis;

import net.fabricmc.api.ModInitializer;
import website.skylorbeck.minecraft.apotheosis.conditions.ApoEntityCondition;
import website.skylorbeck.minecraft.apotheosis.conditions.ApoItemCondition;
import website.skylorbeck.minecraft.apotheosis.powers.PowerFactories;

public class Apotheosis implements ModInitializer {
    @Override
    public void onInitialize() {
        Registrar.Register();
        ApoEntityCondition.register();
        ApoItemCondition.register();
        PowerFactories.register();
    }
}

//todo powers
// --------------
// WARSMITH
// equipment repair - Field Repair
// armor damage increase - True Symmetry
// cruel art - blade damage increase
// war smiths art - repair all equipment
// --------------
// ARCANE SMITH
// +5 luck
// arcane forge - health
// whimsical design - move speed on boots
// imbued blades - random potion effect - this will suck
// refined arcana - 50% reduced enchant costs
// magic metal - 25% double enchant level
// purity of design - enchant no longer needs lapis
// unearthed arcana