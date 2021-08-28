package website.skylorbeck.minecraft.apotheosis.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import website.skylorbeck.minecraft.apotheosis.Declarar;
import website.skylorbeck.minecraft.apotheosis.powers.*;

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
        if (arcanesmithEnchant()) {
            Item item = stack.getItem();
            if ((item instanceof ToolItem || item instanceof ArmorItem) && item.isEnchantable(stack)) {
                stack = EnchantmentHelper.enchant(MinecraftClient.getInstance().world.random, stack,0,false);
                stack.getOrCreateNbt().putBoolean("ApoSmith", true);
            }
        }
        if (arcanesmithChest()) {
            Item item = stack.getItem();
            if (item instanceof ArmorItem) {
                if (((ArmorItem)item).getSlotType()== EquipmentSlot.CHEST){
                    stack.getOrCreateNbt().putBoolean("ApoSmith", true);
                    stack.addEnchantment(Declarar.HEALTHBOOST,2);
                }
            }
        }
        if (arcanesmithBoots()) {
            Item item = stack.getItem();
            if (item instanceof ArmorItem) {
                if (((ArmorItem)item).getSlotType()== EquipmentSlot.FEET){
                    stack.getOrCreateNbt().putBoolean("ApoSmith", true);
                    stack.addEnchantment(Declarar.SPEEDBOOSTER,1);
                }
            }
        }
        if (arcanesmithSword()) {
            Item item = stack.getItem();
            if (item instanceof ArmorItem) {
                if (((ArmorItem)item).getSlotType()== EquipmentSlot.FEET){
                    stack.getOrCreateNbt().putBoolean("ApoSmith", true);
                    Enchantment[] enchantment = new Enchantment[]{Declarar.WITHERASPECT,Declarar.POISONASPECT,Declarar.POISONASPECT};
                    stack.addEnchantment(enchantment[MinecraftClient.getInstance().world.random.nextInt(enchantment.length)],3);
                }
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
    private static boolean arcanesmithEnchant(){
        assert MinecraftClient.getInstance().player != null;
        return PowerHolderComponent.hasPower(MinecraftClient.getInstance().player, ArcanesmithAlwaysEnchanted.class);
    }
    private static boolean arcanesmithChest(){
        assert MinecraftClient.getInstance().player != null;
        return PowerHolderComponent.hasPower(MinecraftClient.getInstance().player, ArcanesmithChestBuffPower.class);
    }
    private static boolean arcanesmithBoots(){
        assert MinecraftClient.getInstance().player != null;
        return PowerHolderComponent.hasPower(MinecraftClient.getInstance().player, ArcanesmithBootBuffPower.class);
    }
    private static boolean arcanesmithSword(){
        assert MinecraftClient.getInstance().player != null;
        return PowerHolderComponent.hasPower(MinecraftClient.getInstance().player, ArcanemithSwordBuffPower.class);
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
