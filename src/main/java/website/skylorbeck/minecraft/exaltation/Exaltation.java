package website.skylorbeck.minecraft.exaltation;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import software.bernie.example.GeckoLibMod;
import software.bernie.geckolib3.GeckoLib;
import website.skylorbeck.minecraft.exaltation.conditions.ApoEntityActions;
import website.skylorbeck.minecraft.exaltation.conditions.ApoEntityCondition;
import website.skylorbeck.minecraft.exaltation.conditions.ApoItemCondition;
import website.skylorbeck.minecraft.exaltation.powers.PowerFactories;

public class Exaltation implements ModInitializer {
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
//todo world gen structures