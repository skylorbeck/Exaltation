package website.skylorbeck.minecraft.apotheosis.blocks.entities;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import website.skylorbeck.minecraft.apotheosis.Declarar;
import website.skylorbeck.minecraft.apotheosis.blocks.screens.AltarScreenHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AltarEntity extends BlockEntity implements ExtendedScreenHandlerFactory, IAnimatable {
    int tier = 0;
    private final AnimationFactory factory = new AnimationFactory(this);

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


    @SuppressWarnings("unchecked")
    private <E extends BlockEntity & IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        event.getController().transitionLengthTicks = 0;
        return PlayState.CONTINUE;
    }
    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(
                new AnimationController<AltarEntity>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
