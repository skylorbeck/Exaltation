package website.skylorbeck.minecraft.apotheosis.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import website.skylorbeck.minecraft.apotheosis.powers.ArcanesmithDoubleEnchantPower;
import website.skylorbeck.minecraft.apotheosis.powers.ArcanesmithFreeLapisPower;

import java.util.List;

@Mixin(EnchantmentScreenHandler.class)
public class EnchantmentScreenHandlerMixin {
    @ModifyVariable(method = "onButtonClick",name = "itemStack", at = @At(value = "RETURN"))
    private ItemStack injectedButtonClick(ItemStack itemStack){
        if (itemStack.getOrCreateNbt().getBoolean("ApoSmith")) {
            assert itemStack.getNbt() != null;
            itemStack.getNbt().putBoolean("ApoSmith",false);
        }
        return itemStack;
    }

    @ModifyVariable(method = "generateEnchantments",name = "list", at = @At(value = "RETURN"))
    private List<EnchantmentLevelEntry> injectedGenEnchant(List<EnchantmentLevelEntry> list){
        if (PowerHolderComponent.hasPower(MinecraftClient.getInstance().player, ArcanesmithDoubleEnchantPower.class) && MinecraftClient.getInstance().world.random.nextInt(10)==0) {
            for (EnchantmentLevelEntry enchantmentLevelEntry : list) {
                ((EnchantmentLevelEntryAccessor) enchantmentLevelEntry).setLevel(enchantmentLevelEntry.level * 2);//todo fix visuals != actual
            }
        }
        return list;
    }

    @Inject(at = @At(value = "RETURN"),method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/screen/ScreenHandlerContext;)V")
    private void injectedButtonClick2(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, CallbackInfo ci){
        if (PowerHolderComponent.hasPower(MinecraftClient.getInstance().player, ArcanesmithFreeLapisPower.class)) {
            ((EnchantmentScreenHandlerAccessor)this).getInvetory().setStack(1,new ItemStack(Items.LAPIS_LAZULI,3));
        }
    }
    @Inject(at = @At(value = "HEAD"),method = "close")
    private void injectedButtonClick2(PlayerEntity player, CallbackInfo ci){
        if (PowerHolderComponent.hasPower(MinecraftClient.getInstance().player, ArcanesmithFreeLapisPower.class)) {
            ((EnchantmentScreenHandlerAccessor)this).getInvetory().getStack(1).decrement(3);
        }
    }

}
