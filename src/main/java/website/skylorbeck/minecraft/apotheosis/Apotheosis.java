package website.skylorbeck.minecraft.apotheosis;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class Apotheosis implements ModInitializer {
    @Override
    public void onInitialize() {
/*        MinecraftClient.getInstance().getServer().getCommandManager().execute(new ServerCommandSource(server, new Vec3d(user.getX(), user.getY(), user.getZ()), Vec2f.ZERO, (ServerWorld) world, 4, "Chunk Loader", new LiteralText("Chunk Loader"), server, null),
                String.format("forceload %s %s %s", "remove", blockPos.getX(), blockPos.getZ()));*/
    }
}
