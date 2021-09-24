package website.skylorbeck.minecraft.apotheosis;

import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.registry.Registry;
import website.skylorbeck.minecraft.apotheosis.blocks.screens.AltarHandledScreen;

import java.lang.reflect.Method;

import static website.skylorbeck.minecraft.apotheosis.Declarar.MODID;
import static website.skylorbeck.minecraft.skylorlib.Registrar.regBlock;
import static website.skylorbeck.minecraft.skylorlib.Registrar.regItem;

public class Registrar {
    public static void Register(){
        regBlock("stonealtar",Declarar.stonealtar,MODID);
        regItem("stonealtar",Declarar.stonealtarItem,MODID);
        regBlock("ironaltar",Declarar.ironaltar,MODID);
        regItem("ironaltar",Declarar.ironaltarItem,MODID);
        regBlock("goldaltar",Declarar.goldaltar,MODID);
        regItem("goldaltar",Declarar.goldaltarItem,MODID);
        regBlock("diamondaltar",Declarar.diamondaltar,MODID);
        regItem("diamondaltar",Declarar.diamondaltarItem,MODID);
        regBlock("netheritealtar",Declarar.netheritealtar,MODID);
        regItem("netheritealtar",Declarar.netheritealtarItem,MODID);
        regItem("bonefragment",Declarar.boneFragment,MODID);
        Registry.register(Registry.STATUS_EFFECT, Declarar.getIdentifier("wolfmark"),Declarar.WOLFMARK);
        Registry.register(Registry.STATUS_EFFECT, Declarar.getIdentifier("hemorrhaging"),Declarar.HEMORRHAGING);
    }
    public static void ClientRegister(){
        ScreenRegistry.register(Declarar.ALTARSCREENHANDLER, AltarHandledScreen::new);
    }
}
