package website.skylorbeck.minecraft.apotheosis.blocks.entities;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class AltarEntityRenderer extends GeoBlockRenderer<AltarEntity> {
    public AltarEntityRenderer() {
        super(new AltarModel());
    }
    @Override
    public RenderLayer getRenderType(AltarEntity animatable, float partialTicks, MatrixStack stack,
                                     VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                     Identifier textureLocation) {
        return RenderLayer.getEntityCutout(getTextureLocation(animatable));
    }
}
