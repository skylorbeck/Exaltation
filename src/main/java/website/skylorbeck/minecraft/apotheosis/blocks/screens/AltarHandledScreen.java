package website.skylorbeck.minecraft.apotheosis.blocks.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.ingame.EnchantingPhrases;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import website.skylorbeck.minecraft.apotheosis.Declarar;

import java.util.Optional;

public class AltarHandledScreen extends HandledScreen<ScreenHandler> {
    private static final Identifier TEXTURE = Declarar.getIdentifier("textures/gui/altarbg.png");

    public AltarHandledScreen(ScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;
        this.drawTexture(matrices, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);

    }
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;

        for(int o = 0; o < 2; ++o) {
            int p = i + 60;
            int q = p + 20;
            this.setZOffset(0);
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, TEXTURE);
            int r = ((EnchantmentScreenHandler)this.handler).enchantmentPower[o];
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            if (r == 0) {
                this.drawTexture(matrices, p, j + 14 + 19 * o, 0, 185, 108, 19);
            } else {
                String string = r.makeConcatWithConstants<invokedynamic>(r);
                int s = 86 - this.textRenderer.getWidth(string);
                StringVisitable stringVisitable = EnchantingPhrases.getInstance().generatePhrase(this.textRenderer, s);
                int t = 6839882;
                if ((n < o + 1 || this.client.player.experienceLevel < r) && !this.client.player.getAbilities().creativeMode) {
                    this.drawTexture(matrices, p, j + 14 + 19 * o, 0, 185, 108, 19);
                    this.drawTexture(matrices, p + 1, j + 15 + 19 * o, 16 * o, 239, 16, 16);
                    this.textRenderer.drawTrimmed(stringVisitable, q, j + 16 + 19 * o, s, (t & 16711422) >> 1);
                    t = 4226832;
                } else {
                    int u = mouseX - (i + 60);
                    int v = mouseY - (j + 14 + 19 * o);
                    if (u >= 0 && v >= 0 && u < 108 && v < 19) {
                        this.drawTexture(matrices, p, j + 14 + 19 * o, 0, 204, 108, 19);
                        t = 16777088;
                    } else {
                        this.drawTexture(matrices, p, j + 14 + 19 * o, 0, 166, 108, 19);
                    }

                    this.drawTexture(matrices, p + 1, j + 15 + 19 * o, 16 * o, 223, 16, 16);
                    this.textRenderer.drawTrimmed(stringVisitable, q, j + 16 + 19 * o, s, t);
                    t = 8453920;
                }

                this.textRenderer.drawWithShadow(matrices, string, (float)(q + 86 - this.textRenderer.getWidth(string)), (float)(j + 16 + 19 * o + 7), t);
            }
        }
    }

    @Override
    public Optional<Element> hoveredElement(double mouseX, double mouseY) {
        return super.hoveredElement(mouseX, mouseY);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        super.mouseMoved(mouseX, mouseY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        return super.charTyped(chr, modifiers);
    }

    @Override
    public void setInitialFocus(@Nullable Element element) {
        super.setInitialFocus(element);
    }

    @Override
    public void focusOn(@Nullable Element element) {
        super.focusOn(element);
    }

    @Override
    public boolean changeFocus(boolean lookForwards) {
        return super.changeFocus(lookForwards);
    }
}
