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
import website.skylorbeck.minecraft.apotheosis.powers.PowerFactories;

import java.util.logging.Level;
import java.util.logging.Logger;

import static website.skylorbeck.minecraft.apotheosis.cardinal.ApotheosisComponents.APOXP;

@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
public class ApotheosisClient implements ClientModInitializer{
    public static KeyBinding testbind = KeyBindingHelper.registerKeyBinding(new KeyBinding("test",InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_Z,"test"));
    public static KeyBinding testbind2 = KeyBindingHelper.registerKeyBinding(new KeyBinding("test2",InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_C,"test"));
    @Override
    public void onInitializeClient() {
        Registrar.ClientRegister();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (testbind.wasPressed()) {
                //////////////UPGRADE CODE FOR TOTEMS
                /* PlayerEntity playerEntity = client.player;
                OriginLayer originLayer = OriginLayers.getLayer(new Identifier("apotheosis","class"));
                Origin origin = OriginRegistry.get(originLayer.getOrigins(playerEntity).get(0));
                String string = origin.getIdentifier().getPath()+"_upgrade_a";
                Identifier identifier = new Identifier("apotheosis",string);
                MinecraftServer server = client.getServer();
                ServerWorld world = server.getOverworld();

                server.getCommandManager().execute(new ServerCommandSource(server, new Vec3d(playerEntity.getX(), playerEntity.getY(), playerEntity.getZ()), Vec2f.ZERO, world, 4, "Apotheosis", new LiteralText("Apotheosis"), server, null),
                        String.format("advancement grant "+playerEntity.getEntityName()+" only "+ identifier));*/
                /////////////

//                   Logger.getGlobal().log(Level.SEVERE,string);
//                Logger.getGlobal().log(Level.SEVERE,"");


//              ModComponents.ORIGIN.get(client.player).setOrigin("apotheosis:origin", OriginRegistry.get(new Identifier("apotheosis","blacksmith")).getUpgrade(MinecraftClient.getInstance().getServer().getAdvancementLoader().get(new Identifier("apotheosis","warsmith"))));


                /* PlayerEntity playerEntity = MinecraftClient.getInstance().getServer().getPlayerManager().getPlayer(client.player.getUuid());
                client.player.sendChatMessage("Luck: "+ playerEntity.getAttributeValue(EntityAttributes.GENERIC_LUCK));*/
                /*PlayerEntity playerEntity = MinecraftClient.getInstance().getServer().getPlayerManager().getPlayer(client.player.getUuid());
                client.player.sendChatMessage("XP BEFORE: "+ APOXP.get(playerEntity).getXP());
                APOXP.get(playerEntity).addXP(2.5f);
                client.player.sendChatMessage("XP AFTER: "+ APOXP.get(playerEntity).getXP());
                APOXP.sync(playerEntity);*/
                /*MinecraftServer server = client.getServer();
                assert server != null;
                ServerWorld world = server.getOverworld();
                PlayerEntity user = client.player;
                assert user != null;
                server.getCommandManager().execute(new ServerCommandSource(server, new Vec3d(user.getX(), user.getY(), user.getZ()), Vec2f.ZERO, world, 4, "Apotheosis", new LiteralText("Apotheosis"), server, null),
                String.format("advancement grant "+client.player.getEntityName()+" only apotheosis:warsmith"));*/
            }
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (testbind2.wasPressed()) {
                PlayerEntity playerEntity = MinecraftClient.getInstance().getServer().getPlayerManager().getPlayer(client.player.getUuid());
                APOXP.get(playerEntity).addLevel(1);
                client.player.sendChatMessage("Level: "+ APOXP.get(playerEntity).getLevel());
                APOXP.sync(playerEntity);
            }
        });
    }
}
