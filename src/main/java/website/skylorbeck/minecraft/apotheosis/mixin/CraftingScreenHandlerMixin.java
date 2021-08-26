package website.skylorbeck.minecraft.apotheosis.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import website.skylorbeck.minecraft.apotheosis.Declarar;
import website.skylorbeck.minecraft.apotheosis.powers.*;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import static website.skylorbeck.minecraft.apotheosis.cardinal.ApotheosisComponents.APOXP;

@Mixin(CraftingScreenHandler.class)
public class CraftingScreenHandlerMixin {
    @Shadow @Final private ScreenHandlerContext context;

    @ModifyVariable(method = "updateResult",name = "itemStack", at = @At(value = "INVOKE",target = "Lnet/minecraft/inventory/CraftingResultInventory;setStack(ILnet/minecraft/item/ItemStack;)V"))
    private static ItemStack injectedUpdateResult(ItemStack stack) {
        if (smithArmor()) {
            Item item = stack.getItem();
            int scale= 0;
            if (item instanceof ArmorItem) {
                stack.getOrCreateNbt().putBoolean("ApoSmith", true);
                scale = smithArmorScale();
                if (scale != 0) {
                    stack.addEnchantment(Enchantments.UNBREAKING, scale);
                    stack.addEnchantment(Enchantments.PROTECTION, scale);
                }
            }
        }
        if (smithWeapon()) {
            Item item = stack.getItem();
            int scale= 0;
            if (item instanceof SwordItem || item instanceof AxeItem) {
                stack.getOrCreateNbt().putBoolean("ApoSmith", true);
                scale = smithWeaponScale();

                if (scale != 0) {
                    stack.addEnchantment(Enchantments.UNBREAKING, scale);
                    if (warsmithSword()){
                        scale+=5;
                    }
                    stack.addEnchantment(Enchantments.SHARPNESS, scale);
                }
            }
        }
        if (warsmithShield()) {
            Item item = stack.getItem();
            if (item instanceof ShieldItem) {
                stack.getOrCreateNbt().putBoolean("ApoSmith", true);
                stack.addEnchantment(Declarar.HEALTHBOOST, 1);
                stack.addEnchantment(Declarar.KNOCKBACKRESIST, 1);
            }
        }
        if (warsmithArmor()) {
            Item item = stack.getItem();
            if (item instanceof ArmorItem) {
                stack.getOrCreateNbt().putBoolean("ApoSmith", true);
                stack.addEnchantment(Declarar.ARMORSHARPNESS, 1);
            }
        }
        return stack;
    }

    private static boolean smithArmor(){
        assert MinecraftClient.getInstance().player != null;
        return PowerHolderComponent.hasPower(MinecraftClient.getInstance().player,SmithingArmorPower.class);
    }
    private static boolean smithWeapon(){
        assert MinecraftClient.getInstance().player != null;
        return PowerHolderComponent.hasPower(MinecraftClient.getInstance().player,SmithingWeaponPower.class);
    }
    private static boolean warsmithShield(){
        assert MinecraftClient.getInstance().player != null;
        return PowerHolderComponent.hasPower(MinecraftClient.getInstance().player, WarsmithShieldBuffPower.class);
    }
    private static boolean warsmithArmor(){
        assert MinecraftClient.getInstance().player != null;
        return PowerHolderComponent.hasPower(MinecraftClient.getInstance().player, WarsmithArmorBuffPower.class);
    }

    private static boolean warsmithSword(){
        assert MinecraftClient.getInstance().player != null;
        return PowerHolderComponent.hasPower(MinecraftClient.getInstance().player, WarsmithSwordBuffPower.class);
    }


    private static int smithArmorScale(){
        assert MinecraftClient.getInstance().player != null;
        MinecraftClient client = MinecraftClient.getInstance();
        PlayerEntity playerEntity = client.player;
        ServerPlayerEntity serverPlayerEntity = client.getServer().getPlayerManager().getPlayer(playerEntity.getUuid());
        assert serverPlayerEntity != null;
        int scale = (PowerHolderComponent.getPowers(serverPlayerEntity, SmithingArmorPower.class).get(0)).getScale();
//        Logger.getGlobal().log(Level.SEVERE,"SMITHSCALECHECK: "+scale + "::: power count: "+PowerHolderComponent.getPowers(serverPlayerEntity,SmithingArmorPower.class).size());

        if (scale!=0) {
            if (APOXP.get(serverPlayerEntity).getAscended()){
                return Math.floorDiv(50,scale);
            }
            return Math.floorDiv(APOXP.get(serverPlayerEntity).getLevel(), scale);
        }
            else
                return 0;
    }
    private static int smithWeaponScale(){
        assert MinecraftClient.getInstance().player != null;
        MinecraftClient client = MinecraftClient.getInstance();
        PlayerEntity playerEntity = client.player;
        ServerPlayerEntity serverPlayerEntity = client.getServer().getPlayerManager().getPlayer(playerEntity.getUuid());
        assert serverPlayerEntity != null;
        int scale = (PowerHolderComponent.getPowers(serverPlayerEntity, SmithingWeaponPower.class).get(0)).getScale();
//        Logger.getGlobal().log(Level.SEVERE,"SMITHSCALECHECK: "+scale + "::: power count: "+PowerHolderComponent.getPowers(serverPlayerEntity,SmithingArmorPower.class).size());

        if (scale!=0) {
            if (APOXP.get(serverPlayerEntity).getAscended()){
                return Math.floorDiv(50,scale);
            }
            return Math.floorDiv(APOXP.get(serverPlayerEntity).getLevel(), scale);
        }
            else
                return 0;
    }
}
