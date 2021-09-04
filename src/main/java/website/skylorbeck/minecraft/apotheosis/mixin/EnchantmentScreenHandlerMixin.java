package website.skylorbeck.minecraft.apotheosis.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import website.skylorbeck.minecraft.apotheosis.powers.ArcanesmithDoubleEnchantPower;
import website.skylorbeck.minecraft.apotheosis.powers.ArcanesmithFreeLapisPower;

@Mixin(EnchantmentScreenHandler.class)
public class EnchantmentScreenHandlerMixin {
    @Shadow @Final private Inventory inventory;

    @Inject(method = "onButtonClick", at = @At(value = "RETURN"))
    private void injectedButtonClick(PlayerEntity player, int id, CallbackInfoReturnable<Boolean> cir){
        ItemStack itemStack = (this).inventory.getStack(0);
        if (itemStack.getOrCreateNbt().getBoolean("ApoSmith")) {
            assert itemStack.getNbt() != null;
            itemStack.getNbt().putBoolean("ApoSmith",false);
        }
    }
   @Redirect(method = "method_17410" , at = @At(value = "INVOKE",target = "Lnet/minecraft/item/ItemStack;addEnchantment(Lnet/minecraft/enchantment/Enchantment;I)V"))
   private void injectedGenEnchant(ItemStack itemStack, Enchantment enchantment, int level) {
        if (PowerHolderComponent.hasPower(MinecraftClient.getInstance().player, ArcanesmithDoubleEnchantPower.class) && MinecraftClient.getInstance().world.random.nextInt(10) == 0) {
            level *= 2;
        }
        itemStack.addEnchantment(enchantment,level);
    }

    @Inject(at = @At(value = "RETURN"),method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/screen/ScreenHandlerContext;)V")
    private void injectedButtonClick2(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, CallbackInfo ci){
        if (PowerHolderComponent.hasPower(MinecraftClient.getInstance().player, ArcanesmithFreeLapisPower.class)) {//todo fix bug with duplicating lapis
            ((EnchantmentScreenHandlerAccessor)this).getInvetory().setStack(1,new ItemStack(Items.LAPIS_LAZULI,3));
        }
    }
    @Inject(at = @At(value = "HEAD"),method = "close")
    private void injectedClose(PlayerEntity player, CallbackInfo ci){
        if (PowerHolderComponent.hasPower(MinecraftClient.getInstance().player, ArcanesmithFreeLapisPower.class)) {
            ((EnchantmentScreenHandlerAccessor)this).getInvetory().getStack(1).decrement(3);
        }
    }

}
