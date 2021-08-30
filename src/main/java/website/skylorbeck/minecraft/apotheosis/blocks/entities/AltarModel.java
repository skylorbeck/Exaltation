package website.skylorbeck.minecraft.apotheosis.blocks.entities;

import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import website.skylorbeck.minecraft.apotheosis.Declarar;

public class AltarModel extends AnimatedGeoModel<AltarEntity> {
    @Override
    public Identifier getModelLocation(AltarEntity object) {
        return Declarar.getIdentifier("geo/altar.geo.json");
    }

    @Override
    public Identifier getTextureLocation(AltarEntity object) {
        Identifier id;
        switch (object.tier){
            default -> id = Declarar.getIdentifier("textures/block/stonealtarentity.png");
            case 1 -> id = Declarar.getIdentifier("textures/block/ironaltarentity.png");
            case 2 -> id = Declarar.getIdentifier("textures/block/goldaltarentity.png");
            case 3 -> id = Declarar.getIdentifier("textures/block/diamondaltarentity.png");
            case 4 -> id = Declarar.getIdentifier("textures/block/netheritealtarentity.png");
        }
        return id;
    }

    @Override
    public Identifier getAnimationFileLocation(AltarEntity animatable) {
        return null;
    }
}
