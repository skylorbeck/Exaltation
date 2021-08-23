package website.skylorbeck.minecraft.apotheosis.blocks.screens;

import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.origin.OriginLayer;
import io.github.apace100.origins.origin.OriginLayers;
import io.github.apace100.origins.origin.OriginRegistry;
import io.github.apace100.origins.registry.ModComponents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerAdvancementLoader;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.apache.logging.log4j.core.jmx.Server;
import website.skylorbeck.minecraft.apotheosis.Declarar;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AltarScreenHandler extends ScreenHandler {
    private final Inventory pInv;
    private final ScreenHandlerContext context;

    public AltarScreenHandler(int syncId, PlayerInventory pInv) {
        super(Declarar.ALTARSCREENHANDLER, syncId);
        this.pInv = pInv;
        context = ScreenHandlerContext.EMPTY;
        int k;
        for (k = 0; k < 3; ++k) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(pInv, j + k * 9 + 9, 8 + j * 18, 84 + k * 18));
            }
        }

        for (k = 0; k < 9; ++k) {
            this.addSlot(new Slot(pInv, k, 8 + k * 18, 142));
        }
    }

    public AltarScreenHandler(int syncId, PlayerInventory pInv, ScreenHandlerContext context) {
        super(Declarar.ALTARSCREENHANDLER, syncId);
        this.context = context;
        this.pInv = pInv;
        int k;
        for (k = 0; k < 3; ++k) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(pInv, j + k * 9 + 9, 8 + j * 18, 84 + k * 18));
            }
        }

        for (k = 0; k < 9; ++k) {
            this.addSlot(new Slot(pInv, k, 8 + k * 18, 142));
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        boolean bool = ModComponents.ORIGIN.get(player).getOrigin(OriginLayers.getLayer(new Identifier("apotheosis", "class"))).hasUpgrade();
        if (!bool){
            player.sendMessage(Text.of("You have nothing to gain from using this"),false);
        }
        return bool;
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        MinecraftServer server = MinecraftClient.getInstance().getServer();
        assert server != null;
        ServerPlayerEntity serverPlayer = server.getPlayerManager().getPlayer(player.getUuid());
        Origin origin = ModComponents.ORIGIN.get(player).getOrigin(OriginLayers.getLayer(new Identifier("apotheosis", "class")));
        Identifier advancementID = id == 0 ? new Identifier(origin.getIdentifier() + "_upgrade_a") : new Identifier(origin.getIdentifier() + "_upgrade_b");
        ServerWorld serverWorld = server.getOverworld();

        server.getCommandManager().execute(new ServerCommandSource(server, new Vec3d(serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ()), Vec2f.ZERO, serverWorld, 4, "Apotheosis", new LiteralText("Apotheosis"), server, null),
                String.format("advancement grant " + serverPlayer.getEntityName() + " only " + advancementID));
        Logger.getGlobal().log(Level.SEVERE, "clicked: " + id);
        MinecraftClient.getInstance().player.closeHandledScreen();
        return super.onButtonClick(player, id);
    }
}
