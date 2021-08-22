package website.skylorbeck.minecraft.apotheosis.blocks.screens;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;
import website.skylorbeck.minecraft.apotheosis.Declarar;

public class AltarScreenHandler extends ScreenHandler {
    public AltarScreenHandler(int syncId) {
        super(Declarar.ALTARSCREENHANDLER, syncId);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}
