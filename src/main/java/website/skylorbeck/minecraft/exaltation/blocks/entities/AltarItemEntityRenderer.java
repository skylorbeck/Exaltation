package website.skylorbeck.minecraft.exaltation.blocks.entities;

import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;
import website.skylorbeck.minecraft.exaltation.blocks.AltarBlockItem;

public class AltarItemEntityRenderer extends GeoItemRenderer<AltarBlockItem> {
    public AltarItemEntityRenderer(){
        super(new AltarItemModel());
    }
}
