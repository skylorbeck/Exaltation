package website.skylorbeck.minecraft.apotheosis.blocks.screens;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import website.skylorbeck.minecraft.apotheosis.Declarar;

public class AltarScreenHandler extends ScreenHandler {
    private final Inventory pInv;
    public AltarScreenHandler(int syncId, PlayerInventory pInv) {
        super(Declarar.ALTARSCREENHANDLER, syncId);
        this.pInv = pInv;
        int k;
        for(k = 0; k < 3; ++k) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(pInv, j + k * 9 + 9, 8 + j * 18, 84 + k * 18));
            }
        }

        for(k = 0; k < 9; ++k) {
            this.addSlot(new Slot(pInv, k, 8 + k * 18, 142));
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}
