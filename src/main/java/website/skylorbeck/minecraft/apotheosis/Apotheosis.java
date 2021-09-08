package website.skylorbeck.minecraft.apotheosis;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import software.bernie.example.GeckoLibMod;
import software.bernie.geckolib3.GeckoLib;
import website.skylorbeck.minecraft.apotheosis.conditions.ApoEntityActions;
import website.skylorbeck.minecraft.apotheosis.conditions.ApoEntityCondition;
import website.skylorbeck.minecraft.apotheosis.conditions.ApoItemCondition;
import website.skylorbeck.minecraft.apotheosis.powers.PowerFactories;

public class Apotheosis implements ModInitializer {
    @Override
    public void onInitialize() {
        GeckoLibMod.DISABLE_IN_DEV = true;
        GeckoLib.initialize();
        Registrar.Register();
        ApoEntityCondition.register();
        ApoItemCondition.register();
        ApoEntityActions.register();
        PowerFactories.register();
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            CommandCenter.register(dispatcher);
        });
    }
}

//todo config
//todo marksman 45 powers and ultimate
// Big Game Hunter:
// If a target's health is >50% you deal additional damage (My brain goes to double however that could break easily for now we can set it to like 5).
// Additionally shot targets are blinded temporarily 2 seconds.