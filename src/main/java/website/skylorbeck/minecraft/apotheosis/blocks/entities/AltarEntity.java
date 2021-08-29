package website.skylorbeck.minecraft.apotheosis.blocks.entities;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import website.skylorbeck.minecraft.apotheosis.Declarar;
import website.skylorbeck.minecraft.apotheosis.blocks.screens.AltarScreenHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AltarEntity extends BlockEntity implements ExtendedScreenHandlerFactory {
    int tier = 0;
    public AltarEntity(BlockPos pos, BlockState state) {
        super(Declarar.ALTARENTITY, pos, state);
    }
    public AltarEntity(BlockPos pos, BlockState state, int tier) {
        super(Declarar.ALTARENTITY, pos, state);
        this.tier = tier;
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText("apotheosis.altar.name");
    }



    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory pInv, PlayerEntity player) {
        return new AltarScreenHandler(syncId,pInv, ScreenHandlerContext.create(world, pos),pos);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(pos);
    }

}
