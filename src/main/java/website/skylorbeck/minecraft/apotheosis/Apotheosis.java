package website.skylorbeck.minecraft.apotheosis;

import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Items;
import website.skylorbeck.minecraft.apotheosis.conditions.ApoLevelCondition;
import website.skylorbeck.minecraft.apotheosis.powers.PowerFactories;

public class Apotheosis implements ModInitializer {
    @Override
    public void onInitialize() {
        Registrar.Register();
        ApoLevelCondition.register();
        PowerFactories.register();

/*        MinecraftClient.getInstance().getServer().getCommandManager().execute(new ServerCommandSource(server, new Vec3d(user.getX(), user.getY(), user.getZ()), Vec2f.ZERO, (ServerWorld) world, 4, "Chunk Loader", new LiteralText("Chunk Loader"), server, null),
                String.format("forceload %s %s %s", "remove", blockPos.getX(), blockPos.getZ()));*/
    }
}
