package website.skylorbeck.minecraft.apotheosis;

import net.fabricmc.api.ModInitializer;
import website.skylorbeck.minecraft.apotheosis.conditions.ApoEntityCondition;
import website.skylorbeck.minecraft.apotheosis.conditions.ApoItemCondition;
import website.skylorbeck.minecraft.apotheosis.powers.PowerFactories;

public class Apotheosis implements ModInitializer {
    @Override
    public void onInitialize() {
        Registrar.Register();
        ApoEntityCondition.register();
        ApoItemCondition.register();
        PowerFactories.register();

/*        MinecraftClient.getInstance().getServer().getCommandManager().execute(new ServerCommandSource(server, new Vec3d(user.getX(), user.getY(), user.getZ()), Vec2f.ZERO, (ServerWorld) world, 4, "Chunk Loader", new LiteralText("Chunk Loader"), server, null),
                String.format("forceload %s %s %s", "remove", blockPos.getX(), blockPos.getZ()));*/
    }
}
