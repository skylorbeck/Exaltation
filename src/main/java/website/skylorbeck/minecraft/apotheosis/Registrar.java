package website.skylorbeck.minecraft.apotheosis;

import website.skylorbeck.minecraft.skylorlib.SkylorLib;

import static website.skylorbeck.minecraft.apotheosis.Declarar.MODID;
import static website.skylorbeck.minecraft.skylorlib.Registrar.regBlock;
import static website.skylorbeck.minecraft.skylorlib.Registrar.regItem;

public class Registrar {
    public static void Register(){
        regBlock("altar",Declarar.altar,MODID);
        regItem("altar",Declarar.altarItem,MODID);
    }
}
