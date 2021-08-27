package website.skylorbeck.minecraft.apotheosis.client;

import com.terraformersmc.modmenu.util.mod.Mod;
import io.github.apace100.apoli.mixin.ClientAdvancementManagerAccessor;
import io.github.apace100.origins.component.OriginComponent;
import io.github.apace100.origins.mixin.OriginUpgradeMixin;
import io.github.apace100.origins.origin.*;
import io.github.apace100.origins.registry.ModComponents;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.impl.client.keybinding.KeyBindingRegistryImpl;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientAdvancementManager;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerAdvancementLoader;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;
import website.skylorbeck.minecraft.apotheosis.Registrar;
import website.skylorbeck.minecraft.apotheosis.hud.ApoHud;
import website.skylorbeck.minecraft.apotheosis.powers.PowerFactories;

import java.util.logging.Level;
import java.util.logging.Logger;

import static website.skylorbeck.minecraft.apotheosis.cardinal.ApotheosisComponents.APOXP;

@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
public class ApotheosisClient implements ClientModInitializer{
    public static ApoHud apoHud;
    public static KeyBinding testbind = KeyBindingHelper.registerKeyBinding(new KeyBinding("test",InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_Z,"test"));
    public static KeyBinding testbind2 = KeyBindingHelper.registerKeyBinding(new KeyBinding("test2",InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_C,"test"));
    @Override
    public void onInitializeClient() {
        Registrar.ClientRegister();
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
