package website.skylorbeck.minecraft.apotheosis.blocks.entities;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import website.skylorbeck.minecraft.apotheosis.Declarar;
import website.skylorbeck.minecraft.apotheosis.blocks.screens.AltarScreenHandler;

public class AltarEntity extends BlockEntity implements NamedScreenHandlerFactory {
    public AltarEntity(BlockPos pos, BlockState state) {
        super(Declarar.ALTARENTITY, pos, state);
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText("apotheosis.altar.name");
    }



    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory pInv, PlayerEntity player) {
        return new AltarScreenHandler(syncId,pInv, ScreenHandlerContext.create(world, pos));
    }
}
