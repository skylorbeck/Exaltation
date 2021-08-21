package website.skylorbeck.minecraft.apotheosis.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.impl.client.keybinding.KeyBindingRegistryImpl;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;

import static website.skylorbeck.minecraft.apotheosis.cardinal.ApotheosisComponents.APOXP;

@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
public class ApotheosisClient implements ClientModInitializer{
    public static KeyBinding testbind = KeyBindingHelper.registerKeyBinding(new KeyBinding("test",InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_Z,"test"));
    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (testbind.wasPressed()) {
                PlayerEntity playerEntity = MinecraftClient.getInstance().getServer().getPlayerManager().getPlayer(client.player.getUuid());
                client.player.sendChatMessage("XP BEFORE: "+ APOXP.get(playerEntity).getXP());
                APOXP.get(playerEntity).addXP(2.5f);
                client.player.sendChatMessage("XP AFTER: "+ APOXP.get(playerEntity).getXP());
                APOXP.sync(playerEntity);
                /*MinecraftServer server = client.getServer();
                assert server != null;
                ServerWorld world = server.getOverworld();
                PlayerEntity user = client.player;
                assert user != null;
                server.getCommandManager().execute(new ServerCommandSource(server, new Vec3d(user.getX(), user.getY(), user.getZ()), Vec2f.ZERO, world, 4, "Apotheosis", new LiteralText("Apotheosis"), server, null),
                String.format("advancement grant "+client.player.getEntityName()+" only apotheosis:warsmith"));*/
            }
        });
    }
}
