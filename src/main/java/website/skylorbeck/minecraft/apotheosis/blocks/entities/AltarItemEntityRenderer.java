package website.skylorbeck.minecraft.apotheosis.blocks.entities;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;
import website.skylorbeck.minecraft.apotheosis.blocks.AltarBlockItem;

public class AltarItemEntityRenderer extends GeoItemRenderer<AltarBlockItem> {
    public AltarItemEntityRenderer(){
        super(new AltarItemModel());
    }
}
