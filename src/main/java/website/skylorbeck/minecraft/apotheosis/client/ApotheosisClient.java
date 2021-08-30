package website.skylorbeck.minecraft.apotheosis.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import org.lwjgl.glfw.GLFW;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;
import website.skylorbeck.minecraft.apotheosis.Declarar;
import website.skylorbeck.minecraft.apotheosis.Registrar;
import website.skylorbeck.minecraft.apotheosis.blocks.AltarBlockItem;
import website.skylorbeck.minecraft.apotheosis.blocks.entities.AltarEntityRenderer;
import website.skylorbeck.minecraft.apotheosis.blocks.entities.AltarItemEntityRenderer;
import website.skylorbeck.minecraft.apotheosis.hud.ApoHud;

import static website.skylorbeck.minecraft.apotheosis.cardinal.ApotheosisComponents.APOXP;

@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
public class ApotheosisClient implements ClientModInitializer{
    public static ApoHud apoHud;
    public static KeyBinding testbind = KeyBindingHelper.registerKeyBinding(new KeyBinding("test",InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_Z,"test"));
    public static KeyBinding testbind2 = KeyBindingHelper.registerKeyBinding(new KeyBinding("test2",InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_C,"test"));
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
        apoHud = new ApoHud();


        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (testbind.wasPressed()) {
                PlayerEntity playerEntity = MinecraftClient.getInstance().getServer().getPlayerManager().getPlayer(client.player.getUuid());
                APOXP.get(playerEntity).setLevel(1);
                client.player.sendChatMessage("Level: "+ APOXP.get(playerEntity).getLevel());
                APOXP.get(playerEntity).setAscended(false);
                APOXP.sync(playerEntity);
            }
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (testbind2.wasPressed()) {
                PlayerEntity playerEntity = MinecraftClient.getInstance().getServer().getPlayerManager().getPlayer(client.player.getUuid());
                APOXP.get(playerEntity).setLevel(50);
                client.player.sendChatMessage("Level: "+ APOXP.get(playerEntity).getLevel());
                APOXP.sync(playerEntity);
            }
        });


    }
}
