package website.skylorbeck.minecraft.apotheosis.hud;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.DyeColor;

import static website.skylorbeck.minecraft.apotheosis.cardinal.ApotheosisComponents.APOXP;

public class ApoHud {
    public ApoHud() {
        HudRenderCallback.EVENT.register(this::render);
    }
    private void render(MatrixStack matrixStack, float delta) {
        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer textRenderer = client.inGameHud.getTextRenderer();
        int scaledWidth = client.getWindow().getScaledWidth();//these are CRITICAL for dynamically scaled ui elements
        int scaledHeight = client.getWindow().getScaledHeight();
        String string = String.valueOf(APOXP.get(client.player).getLevel());
        int x = (scaledWidth - textRenderer.getWidth(string)) / 2;
        int y = scaledHeight - 31 - 14;
        textRenderer.draw(matrixStack, (String)string, (float)(x + 1), (float)y, 0);
        textRenderer.draw(matrixStack, (String)string, (float)(x - 1), (float)y, 0);
        textRenderer.draw(matrixStack, (String)string, (float)x, (float)(y + 1), 0);
        textRenderer.draw(matrixStack, (String)string, (float)x, (float)(y - 1), 0);
        textRenderer.draw(matrixStack, string, (float)x, (float)y, DyeColor.ORANGE.getSignColor());
    }
}
