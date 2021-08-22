package website.skylorbeck.minecraft.apotheosis;

import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import website.skylorbeck.minecraft.apotheosis.blocks.screens.AltarHandledScreen;
import website.skylorbeck.minecraft.skylorlib.SkylorLib;

import static website.skylorbeck.minecraft.apotheosis.Declarar.MODID;
import static website.skylorbeck.minecraft.skylorlib.Registrar.regBlock;
import static website.skylorbeck.minecraft.skylorlib.Registrar.regItem;

public class Registrar {
    public static void Register(){
        regBlock("altar",Declarar.altar,MODID);
        regItem("altar",Declarar.altarItem,MODID);
    }
    public static void ClientRegister(){
        ScreenRegistry.register(Declarar.ALTARSCREENHANDLER, AltarHandledScreen::new);
    }
}
