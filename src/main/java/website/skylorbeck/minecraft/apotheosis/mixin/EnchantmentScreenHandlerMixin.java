package website.skylorbeck.minecraft.apotheosis.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.EnchantmentScreenHandler;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import website.skylorbeck.minecraft.apotheosis.powers.ArcanesmithDoubleEnchantPower;

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

/*    @Inject(at = @At(value = "INVOKE",target = "Lnet/minecraft/screen/ScreenHandlerContext;run(Ljava/util/function/BiConsumer;)V"),method = "onButtonClick")
    private void injectedButtonClick2(PlayerEntity player, int id, CallbackInfoReturnable<Boolean> cir){
        for (int i = 0; i < ((EnchantmentScreenHandler) (Object) this).enchantmentPower.length; i++) {
            ((EnchantmentScreenHandler)(Object)this).enchantmentPower[i] = ((EnchantmentScreenHandler)(Object)this).enchantmentPower[i]*2;
        }

    }*/

}
