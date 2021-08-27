package website.skylorbeck.minecraft.apotheosis.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import website.skylorbeck.minecraft.apotheosis.powers.ArcanesmithCheapEnchantingPower;

import java.util.Map;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilScreenHandlerMixin {
    @Shadow
    public abstract int getLevelCost();

    @Redirect(
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/inventory/CraftingResultInventory;setStack(ILnet/minecraft/item/ItemStack;)V"),
            method = "updateResult",
            slice = @Slice(from = @At(value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;setRepairCost(I)V"),
                    to = @At(value = "INVOKE",
                            target = "Lnet/minecraft/screen/AnvilScreenHandler;sendContentUpdates()V"))
    )
    private void injectedUpdateResult(CraftingResultInventory craftingResultInventory, int slot, ItemStack stack) {
        stack.getOrCreateNbt().putBoolean("ApoSmith", false);
        craftingResultInventory.setStack(slot, stack);
    }

    @Inject(
            at = @At(value = "INVOKE",
                    shift = At.Shift.AFTER,
                    target = "Lnet/minecraft/screen/Property;set(I)V"),
            method = "updateResult",
            slice = @Slice(from = @At(value = "INVOKE",
                    target = "Lorg/apache/commons/lang3/StringUtils;isBlank(Ljava/lang/CharSequence;)Z"),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/screen/Property;get()I"))
    )
    public void injectedHasEnchantments(CallbackInfo ci) {
        if (PowerHolderComponent.hasPower((MinecraftClient.getInstance().player), ArcanesmithCheapEnchantingPower.class)) {
            ((AnvilScreenHandlerAccessor) this).getlevelCost().set(getLevelCost() / 2);
        }
    }
}