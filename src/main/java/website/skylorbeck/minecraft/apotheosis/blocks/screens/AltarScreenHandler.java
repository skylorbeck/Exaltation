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
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerAdvancementLoader;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.apache.logging.log4j.core.jmx.Server;
import website.skylorbeck.minecraft.apotheosis.Declarar;
import website.skylorbeck.minecraft.apotheosis.cardinal.XPComponent;

import java.util.logging.Level;
import java.util.logging.Logger;

import static website.skylorbeck.minecraft.apotheosis.cardinal.ApotheosisComponents.APOXP;

public class AltarScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final ScreenHandlerContext context;

    public AltarScreenHandler(int syncId, PlayerInventory pInv) {
        super(Declarar.ALTARSCREENHANDLER, syncId);
        context = ScreenHandlerContext.EMPTY;
        this.inventory = new SimpleInventory(1);
        this.addSlot(new Slot(this.inventory, 0, 23, 47) {
            public boolean canInsert(ItemStack stack) {
                return true;
            }

            public int getMaxItemCount() {
                return 1;
            }
        });
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
        this.inventory = new SimpleInventory(1);
        this.addSlot(new Slot(this.inventory, 0, 15, 47) {
            public boolean canInsert(ItemStack stack) {
                return APOXP.get(pInv.player).getLevel()%5==0;
            }

            public int getMaxItemCount() {
                return 1;
            }
        });
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
        if (APOXP.get(player).getLevel()<50){
            return true;
        }
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
        ServerWorld serverWorld = server.getOverworld();
        switch (id){
            case 0,1 ->{
                Origin origin = ModComponents.ORIGIN.get(player).getOrigin(OriginLayers.getLayer(new Identifier("apotheosis", "class")));
                Identifier advancementID = id == 0 ? new Identifier(origin.getIdentifier() + "_upgrade_a") : new Identifier(origin.getIdentifier() + "_upgrade_b");

                server.getCommandManager().execute(new ServerCommandSource(server, new Vec3d(serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ()), Vec2f.ZERO, serverWorld, 4, "Apotheosis", new LiteralText("Apotheosis"), server, null),
                        String.format("advancement grant " + serverPlayer.getEntityName() + " only " + advancementID));
                APOXP.get(serverPlayer).setLevel(1);
                APOXP.get(serverPlayer).setAscended(true);
                APOXP.sync(serverPlayer);
                serverWorld.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.BLOCKS, 1.0F, serverWorld.random.nextFloat() * 0.1F + 0.9F);

//                MinecraftClient.getInstance().player.closeHandledScreen();
            }
            case 3 -> {
                XPComponent xpComponent= APOXP.get(serverPlayer);
                int cost = xpComponent.getLevelUpCost();
                serverPlayer.addExperienceLevels(-(cost));
                xpComponent.addLevel(1);
                APOXP.sync(serverPlayer);
                serverWorld.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 1.0F, serverWorld.random.nextFloat() * 0.1F + 0.9F);
//                MinecraftClient.getInstance().player.closeHandledScreen();
            }
        }
        assert MinecraftClient.getInstance().currentScreen != null;
        ((AltarHandledScreen)MinecraftClient.getInstance().currentScreen).levelUpClicked();
        return super.onButtonClick(player, id);
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);
        this.context.run((world, pos) -> {
            this.dropInventory(player, this.inventory);
        });
    }
}
