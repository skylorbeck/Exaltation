package website.skylorbeck.minecraft.apotheosis.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
    @Shadow
    @Final
    private Inventory inventory;

    @Shadow
    @Final
    private ScreenHandlerContext context;

    @Inject(method = "onButtonClick", at = @At(value = "RETURN"))
    private void injectedButtonClick(PlayerEntity player, int id, CallbackInfoReturnable<Boolean> cir) {
        ItemStack itemStack = (this).inventory.getStack(0);
        if (itemStack.getOrCreateNbt().getBoolean("ApoSmith")) {
            assert itemStack.getNbt() != null;
            itemStack.getNbt().putBoolean("ApoSmith", false);
        }
    }

    @Redirect(method = "method_17410", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;addEnchantment(Lnet/minecraft/enchantment/Enchantment;I)V"))
    private void doubleEnchant(ItemStack itemStack, Enchantment enchantment, int level, ItemStack itemStack2, int i, PlayerEntity player, int j, ItemStack itemStack3, World world, BlockPos pos) {
        if (PowerHolderComponent.hasPower(player, ArcanesmithDoubleEnchantPower.class) && world.random.nextInt(10) == 0) {
            level *= 2;
        }
        itemStack.addEnchantment(enchantment, level);
    }

    private PlayerAbilities fakeCreative(PlayerEntity player) {
        if (PowerHolderComponent.hasPower(player, ArcanesmithFreeLapisPower.class)) {
            PlayerAbilities playerAbilities = new PlayerAbilities();
            playerAbilities.creativeMode = true;
            return playerAbilities;
        } else return player.getAbilities();
    }

    @Redirect(method = "method_17410", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getAbilities()Lnet/minecraft/entity/player/PlayerAbilities;"))
    private PlayerAbilities fakeCreativeA(PlayerEntity player, ItemStack itemStack, int i, PlayerEntity player2, int j, ItemStack itemStack2, World world, BlockPos pos) {
        return fakeCreative(player);
    }

    @Redirect(method = "onButtonClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getAbilities()Lnet/minecraft/entity/player/PlayerAbilities;"))
    private PlayerAbilities fakeCreativeB(PlayerEntity player) {
        return fakeCreative(player);
    }


    @Inject(at = @At(value = "HEAD"), method = "getLapisCount", cancellable = true)
    private void takeFreeLapis(CallbackInfoReturnable<Integer> cir) {
        if (PowerHolderComponent.hasPower(((PlayerInventory) ((EnchantmentScreenHandler) (Object) this).slots.get(3).inventory).player, ArcanesmithFreeLapisPower.class)) {
            cir.setReturnValue(3);
        }
    }
}