package website.skylorbeck.minecraft.apotheosis.blocks.entities;

import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import website.skylorbeck.minecraft.apotheosis.Declarar;
import website.skylorbeck.minecraft.apotheosis.blocks.AltarAbstract;
import website.skylorbeck.minecraft.apotheosis.blocks.AltarBlockItem;

public class AltarItemModel extends AnimatedGeoModel<AltarBlockItem> {
    @Override
    public Identifier getModelLocation(AltarBlockItem object) {
        return Declarar.getIdentifier("geo/altar.geo.json");
    }

    @Override
    public Identifier getTextureLocation(AltarBlockItem object) {
        Identifier id;
        switch (((AltarAbstract)object.getBlock()).getTier()){
            default -> id = Declarar.getIdentifier("textures/block/stonealtarentity.png");
            case 1 -> id = Declarar.getIdentifier("textures/block/ironaltarentity.png");
            case 2 -> id = Declarar.getIdentifier("textures/block/goldaltarentity.png");
            case 3 -> id = Declarar.getIdentifier("textures/block/diamondaltarentity.png");
            case 4 -> id = Declarar.getIdentifier("textures/block/netheritealtarentity.png");
        }
        return id;
    }

    @Override
    public Identifier getAnimationFileLocation(AltarBlockItem animatable) {
        return null;
    }
}
