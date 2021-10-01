package website.skylorbeck.minecraft.exaltation.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;
import website.skylorbeck.minecraft.exaltation.Declarar;
import website.skylorbeck.minecraft.exaltation.Registrar;
import website.skylorbeck.minecraft.exaltation.blocks.entities.AltarEntityRenderer;
import website.skylorbeck.minecraft.exaltation.blocks.entities.AltarItemEntityRenderer;
import website.skylorbeck.minecraft.exaltation.blocks.screens.AltarScreenHandler;
import website.skylorbeck.minecraft.exaltation.hud.ExaltationHud;

import static website.skylorbeck.minecraft.exaltation.Declarar.MODID;
import static website.skylorbeck.minecraft.skylorlib.Registrar.regClientSidePacket;
import static website.skylorbeck.minecraft.skylorlib.Registrar.regServerSidePacket;

@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
public class ExaltationClient implements ClientModInitializer{
    public static ExaltationHud exaltationHud;
    public static KeyBinding bind1 = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.exaltation.primary_active",InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_V,"exaltation.category"));
    public static KeyBinding bind2 = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.exaltation.secondary_active",InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_B,"exaltation.category"));
    @Override
    public void onInitializeClient() {
        Registrar.ClientRegister();
        BlockEntityRendererRegistry.INSTANCE.register(Declarar.ALTARENTITY,
                (BlockEntityRendererFactory.Context rendererDispatcherIn) -> new AltarEntityRenderer());
        GeoItemRenderer.registerItemRenderer(Declarar.stonealtarItem,new AltarItemEntityRenderer());
        GeoItemRenderer.registerItemRenderer(Declarar.ironaltarItem,new AltarItemEntityRenderer());
        GeoItemRenderer.registerItemRenderer(Declarar.goldaltarItem,new AltarItemEntityRenderer());
        GeoItemRenderer.registerItemRenderer(Declarar.diamondaltarItem,new AltarItemEntityRenderer());
        GeoItemRenderer.registerItemRenderer(Declarar.netheritealtarItem,new AltarItemEntityRenderer());
        exaltationHud = new ExaltationHud();

        regClientSidePacket("sendadvancementpacket",MODID,((context, buffer) ->{
            Identifier identifier = buffer.readIdentifier();
            int slot = buffer.readInt();
                context.getTaskQueue().execute(() -> ((AltarScreenHandler)context.getPlayer().currentScreenHandler).sendAdvancement(identifier,slot));
        }));
    }
}
