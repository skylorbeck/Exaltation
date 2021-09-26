package website.skylorbeck.minecraft.exaltation.hud;

import io.github.apace100.apoli.component.PowerHolderComponent;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.util.DyeColor;
import website.skylorbeck.minecraft.exaltation.powers.MarksmanArrowCyclingPower;

import static website.skylorbeck.minecraft.exaltation.cardinal.ExaltationComponents.EXALXP;

public class ExaltationHud {
    public ExaltationHud() {
        HudRenderCallback.EVENT.register(this::render);
    }
    private void render(MatrixStack matrixStack, float delta) {
        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer textRenderer = client.inGameHud.getTextRenderer();
        ItemRenderer itemRenderer = client.getItemRenderer();
        int scaledWidth = client.getWindow().getScaledWidth();//these are CRITICAL for dynamically scaled ui elements
        int scaledHeight = client.getWindow().getScaledHeight();
        String string = String.valueOf(EXALXP.get(client.player).getLevel());
        int x = (scaledWidth - textRenderer.getWidth(string)) / 2;
        int y = scaledHeight - 45;
        textRenderer.draw(matrixStack, (String)string, (float)(x + 1), (float)y, 0);
        textRenderer.draw(matrixStack, (String)string, (float)(x - 1), (float)y, 0);
        textRenderer.draw(matrixStack, (String)string, (float)x, (float)(y + 1), 0);
        textRenderer.draw(matrixStack, (String)string, (float)x, (float)(y - 1), 0);
        textRenderer.draw(matrixStack, string, (float)x, (float)y, DyeColor.ORANGE.getSignColor());
        PlayerEntity entity = MinecraftClient.getInstance().player;
        if (PowerHolderComponent.hasPower(entity, MarksmanArrowCyclingPower.class)) {
            MarksmanArrowCyclingPower marksmanArrowCyclingPower = PowerHolderComponent.KEY.get(entity).getPowers(MarksmanArrowCyclingPower.class).get(0);
            if (marksmanArrowCyclingPower.isActive()) {
                ItemStack newArrow = Items.TIPPED_ARROW.getDefaultStack();
                PotionUtil.setPotion(newArrow, new Potion(marksmanArrowCyclingPower.getStatusEffect()));
                newArrow.getOrCreateNbt().putInt("CustomPotionColor", marksmanArrowCyclingPower.getStatusEffect().getEffectType().getColor());
                itemRenderer.renderGuiItemIcon(newArrow,scaledWidth-scaledWidth/4-16,scaledHeight-30);
            }
        }
    }
}
