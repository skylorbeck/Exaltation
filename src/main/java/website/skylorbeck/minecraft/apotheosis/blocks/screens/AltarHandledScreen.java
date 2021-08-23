package website.skylorbeck.minecraft.apotheosis.blocks.screens;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.apace100.origins.Origins;
import io.github.apace100.origins.origin.*;
import io.github.apace100.origins.registry.ModComponents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerAdvancementLoader;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import website.skylorbeck.minecraft.apotheosis.Declarar;

import java.util.List;
import java.util.Optional;

public class AltarHandledScreen extends HandledScreen<ScreenHandler> {
    private static final Identifier TEXTURE = Declarar.getIdentifier("textures/gui/altarbg.png");
    private PlayerEntity player;
    private Origin origin;
    private Origin[] originUpgrades = new Origin[2];
    private OriginLayer originLayer;
    private Identifier[] advancementID = new Identifier[2];
    private MinecraftServer server;
    private ServerWorld serverWorld;
    private ServerAdvancementLoader advancementLoader;

    public AltarHandledScreen(ScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.client = MinecraftClient.getInstance();
        server = client.getServer();
        assert server != null;
        advancementLoader = server.getAdvancementLoader();
        player = server.getPlayerManager().getPlayer(client.player.getUuid());
        originLayer = OriginLayers.getLayer(new Identifier("apotheosis", "class"));
        origin = ModComponents.ORIGIN.get(player).getOrigin(originLayer);
        advancementID[0] = new Identifier(origin.getIdentifier()+"_upgrade_a");
        advancementID[1] = new Identifier(origin.getIdentifier()+"_upgrade_b");
        try {
            originUpgrades[0] =OriginRegistry.get(origin.getUpgrade(advancementLoader.get(advancementID[0])).get().getUpgradeToOrigin());
        } catch (Exception ignored){
            originUpgrades[0]=null;
        }
        try {
            originUpgrades[1] = OriginRegistry.get(origin.getUpgrade(advancementLoader.get(advancementID[1])).get().getUpgradeToOrigin());
        } catch (Exception ignored){
            originUpgrades[1]=null;
        }
        serverWorld = server.getOverworld();
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;
        this.drawTexture(matrices, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);
        x = x + 60;
        y = y + 14;
        for (int i = 0; i < 2; ++i) {
            this.setZOffset(0);
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, TEXTURE);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            if (origin.hasUpgrade()) {
                int u = mouseX - (x+(54*i));
                int v = mouseY - y;
                if (u >= 0 && v >= 0 && u < 54 && v < 57) {
                    this.drawTexture(matrices, x + (54 * i), y, 108, 166, 54, 57);//highlighted
                } else {
                    this.drawTexture(matrices, x + (54 * i), y, 0, 166, 54, 57);//has upgrade
                }
            } else {
                this.drawTexture(matrices, x + (54 * i), y, 54, 166, 54, 57);//no upgrade
            }
            if (originUpgrades[i] != null) {
                Origin up = originUpgrades[i];
                this.textRenderer.drawTrimmed(up.getName(),x+27+(54 * i)-this.textRenderer.getWidth(up.getName())/2,y+3,54, DyeColor.WHITE.getSignColor());
                Impact impact = up.getImpact();
                int impactValue = impact.getImpactValue();
                int offset = impactValue*8;
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderTexture(0, TEXTURE);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                for(int j = 0; j < 3; j++) {
                    if(j < impactValue) {
                        this.drawTexture(matrices, x+13 + (j * 10), y + 15, offset,223, 8, 8);
                    } else {
                        this.drawTexture(matrices, x+13 + (j * 10), y + 15, 0, 223, 8, 8);
                    }
                }
                this.itemRenderer.renderGuiItemIcon(up.getDisplayItem(),x + 21 + (54 * i),y+36);

            }
        }
    }
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        for (int i = 0; i < 2; ++i) {
            //tooltips
            if (this.isPointWithinBounds(60 + (54 * i), 14, 54,57, mouseX, mouseY) ) {
                if (originUpgrades[i] != null) {
                    List<Text> list = Lists.newArrayList();
                    originUpgrades[i].getPowerTypes().forEach(powerType -> {
                       list.add(powerType.getName());
                    });
                    this.renderTooltip(matrices, list, mouseX, mouseY);
                    break;
                } else {
                    this.renderTooltip(matrices, Text.of("ERROR"), mouseX, mouseY);
                }
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

    @Override
    protected void init() {
        super.init();
    }
}
