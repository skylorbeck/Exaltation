package website.skylorbeck.minecraft.apotheosis.blocks.screens;

import io.github.apace100.apoli.component.PowerHolderComponent;
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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.screen.EnchantmentScreenHandler;
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
import website.skylorbeck.minecraft.apotheosis.powers.ConsumingItemPower;

import java.util.logging.Level;
import java.util.logging.Logger;

import static website.skylorbeck.minecraft.apotheosis.cardinal.ApotheosisComponents.APOXP;

public class AltarScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final ScreenHandlerContext context;

    public AltarScreenHandler(int syncId, PlayerInventory pInv) {
        this(syncId,pInv,ScreenHandlerContext.EMPTY);
    }

    public AltarScreenHandler(int syncId, PlayerInventory pInv, ScreenHandlerContext context) {
        super(Declarar.ALTARSCREENHANDLER, syncId);
        this.context = context;
        this.inventory = new SimpleInventory(1);
        this.addSlot(new Slot(this.inventory, 0, 23, 47) {
            public boolean canInsert(ItemStack stack) {
                return true;
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
    public ItemStack transferSlot(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
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
        switch (id){
            case 0,1 ->{
                Origin origin = ModComponents.ORIGIN.get(player).getOrigin(OriginLayers.getLayer(new Identifier("apotheosis", "class")));
                Identifier advancementID = id == 0 ? new Identifier(origin.getIdentifier() + "_upgrade_a") : new Identifier(origin.getIdentifier() + "_upgrade_b");
                context.run(((world, blockPos) -> {
                world.getServer().getCommandManager().execute(new ServerCommandSource(world.getServer(), new Vec3d(player.getX(), player.getY(), player.getZ()), Vec2f.ZERO, (ServerWorld) world, 4, "Apotheosis", new LiteralText("Apotheosis"), world.getServer(), null),
                        String.format("advancement grant " + player.getEntityName() + " only " + advancementID));
                }));
                if (!APOXP.get(player).getAscended()) {
                    APOXP.get(player).setLevel(1);
                    APOXP.get(player).setAscended(true);
                }
                APOXP.sync(player);
                this.context.run(((world, blockPos) -> {
                    world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.BLOCKS, 1.0F, world.random.nextFloat() * 0.1F + 0.9F);
                }));
            }
            case 3 -> {
                XPComponent xpComponent= APOXP.get(player);

                int APOXPLVL = xpComponent.getLevel();
                if ((APOXPLVL+1)%5==0) {
                    if (PowerHolderComponent.hasPower(player, ConsumingItemPower.class)){
                        ItemStack item = inventory.getStack(0);
                        Item itemcost = APOXPLVL>=44?Items.DIAMOND:PowerHolderComponent.getPowers(player,ConsumingItemPower.class).get(0).getItem();
                        int cost = APOXPLVL>=44?APOXPLVL>=49?2:1:Math.min(Math.floorDiv(APOXPLVL+1,5),4);

                        if ((!item.getItem().equals(itemcost)||item.getCount()<5*cost) && !player.isCreative()) {
                            this.context.run(((world, blockPos) -> {
                                player.sendMessage(Text.of("You are lacking "+5*cost+" "+itemcost.getName().getString()),false);
                                world.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_DISPENSER_FAIL, SoundCategory.BLOCKS, 1.0F, world.random.nextFloat() * 0.1F + 0.9F);
                            }));
                            return true;
                        } else {
                            if (item.getCount()>=5*cost) {
                                ItemStack itemStack = item;
                                this.context.run(((world, blockPos) -> {
                                itemStack.decrement(5 * cost);
                                this.inventory.setStack(0, itemStack);
                                if (itemStack.isEmpty()) {
                                    this.inventory.setStack(0, ItemStack.EMPTY);
                                }

                                this.inventory.markDirty();
                                this.onContentChanged(this.inventory);
                                }));
                            }
                        }
                    }
                }

                int cost = xpComponent.getLevelUpCost();
                player.addExperienceLevels(-(cost));
                xpComponent.addLevel(1);
                APOXP.sync(player);
                this.context.run(((world, blockPos) -> {
                        world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 1.0F, world.random.nextFloat() * 0.1F + 0.9F);
                }));
            }
        }
        assert MinecraftClient.getInstance().currentScreen != null;
        ((AltarHandledScreen)MinecraftClient.getInstance().currentScreen).levelUpClicked();
        return true;
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        super.onContentChanged(inventory);
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);
        this.context.run((world, pos) -> {
            this.dropInventory(player, this.inventory);
        });
    }
}
