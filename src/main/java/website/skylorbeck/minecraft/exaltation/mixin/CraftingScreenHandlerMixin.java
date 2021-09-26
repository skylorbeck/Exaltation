package website.skylorbeck.minecraft.exaltation.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.item.*;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import website.skylorbeck.minecraft.exaltation.Declarar;
import website.skylorbeck.minecraft.exaltation.powers.*;

import static website.skylorbeck.minecraft.exaltation.cardinal.ExaltationComponents.APOXP;

@Mixin(CraftingScreenHandler.class)
public class CraftingScreenHandlerMixin {
    @Redirect(method = "updateResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/CraftingResultInventory;setStack(ILnet/minecraft/item/ItemStack;)V"))
    private static void injectedUpdateResult(CraftingResultInventory craftingResultInventory, int slot, ItemStack stack, ScreenHandler handler, World world, PlayerEntity player, CraftingInventory craftingInventory, CraftingResultInventory resultInventory) {
        if (PowerHolderComponent.hasPower(player, SmithingArmorPower.class)) {
            Item item = stack.getItem();
            int scale = 0;
            if (item instanceof ArmorItem) {
                stack.getOrCreateNbt().putBoolean("ApoSmith", true);
                scale = smithArmorScale(player);
                if (scale != 0) {
                    stack.addEnchantment(Enchantments.UNBREAKING, scale);
                    stack.addEnchantment(Enchantments.PROTECTION, scale);
                }
            }
        }
        if (PowerHolderComponent.hasPower(player, SmithingWeaponPower.class)) {
            Item item = stack.getItem();
            int scale = 0;
            if (item instanceof SwordItem || item instanceof AxeItem) {
                stack.getOrCreateNbt().putBoolean("ApoSmith", true);
                scale = smithWeaponScale(player);
                if (scale != 0) {
                    stack.addEnchantment(Enchantments.UNBREAKING, scale);
                    if (PowerHolderComponent.hasPower(player, WarsmithSwordBuffPower.class)) {
                        scale += 5;
                    }
                    stack.addEnchantment(Enchantments.SHARPNESS, scale);
                }
            }
        }
        if (PowerHolderComponent.hasPower(player, WarsmithShieldBuffPower.class)) {
            Item item = stack.getItem();
            if (item instanceof ShieldItem) {
                stack.getOrCreateNbt().putBoolean("ApoSmith", true);
                stack.addEnchantment(Declarar.HEALTHBOOST, 1);
                stack.addEnchantment(Declarar.KNOCKBACKRESIST, 1);
            }
        }
        if (PowerHolderComponent.hasPower(player, WarsmithArmorBuffPower.class)) {
            Item item = stack.getItem();
            if (item instanceof ArmorItem) {
                stack.getOrCreateNbt().putBoolean("ApoSmith", true);
                stack.addEnchantment(Declarar.ARMORSHARPNESS, 1);
            }
        }
        if (PowerHolderComponent.hasPower(player, ArcanesmithAlwaysEnchanted.class)) {
            Item item = stack.getItem();
            if ((item instanceof ToolItem || item instanceof ArmorItem) && item.isEnchantable(stack)) {
                stack = EnchantmentHelper.enchant(world.random, stack, 0, false);
                stack.getOrCreateNbt().putBoolean("ApoSmith", true);
            }
        }
        if (PowerHolderComponent.hasPower(player, ArcanesmithChestBuffPower.class)) {
            Item item = stack.getItem();
            if (item instanceof ArmorItem) {
                if (((ArmorItem) item).getSlotType() == EquipmentSlot.CHEST) {
                    stack.getOrCreateNbt().putBoolean("ApoSmith", true);
                    stack.addEnchantment(Declarar.HEALTHBOOST, 2);
                }
            }
        }
        if (PowerHolderComponent.hasPower(player, ArcanesmithBootBuffPower.class)) {
            Item item = stack.getItem();
            if (item instanceof ArmorItem) {
                if (((ArmorItem) item).getSlotType() == EquipmentSlot.FEET) {
                    stack.getOrCreateNbt().putBoolean("ApoSmith", true);
                    stack.addEnchantment(Declarar.SPEEDBOOSTER, 1);
                }
            }
        }
        if (PowerHolderComponent.hasPower(player, ArcanemithSwordBuffPower.class)) {
            Item item = stack.getItem();
            if (item instanceof ArmorItem) {
                if (((ArmorItem) item).getSlotType() == EquipmentSlot.FEET) {
                    stack.getOrCreateNbt().putBoolean("ApoSmith", true);
                    Enchantment[] enchantment = new Enchantment[]{Declarar.WITHERASPECT, Declarar.POISONASPECT, Declarar.POISONASPECT};
                    stack.addEnchantment(enchantment[world.random.nextInt(enchantment.length)], 3);
                }
            }
        }
        craftingResultInventory.setStack(slot, stack);
    }


    private static int smithArmorScale(PlayerEntity playerEntity) {
        int scale = (PowerHolderComponent.getPowers(playerEntity, SmithingArmorPower.class).get(0)).getScale();
        if (scale != 0) {
            if (APOXP.get(playerEntity).getAscended()) {
                return Math.floorDiv(50, scale);
            }
            return Math.floorDiv(APOXP.get(playerEntity).getLevel(), scale);
        } else
            return 0;
    }

    private static int smithWeaponScale(PlayerEntity playerEntity) {
        int scale = (PowerHolderComponent.getPowers(playerEntity, SmithingWeaponPower.class).get(0)).getScale();
        if (scale != 0) {
            if (APOXP.get(playerEntity).getAscended()) {
                return Math.floorDiv(50, scale);
            }
            return Math.floorDiv(APOXP.get(playerEntity).getLevel(), scale);
        } else
            return 0;
    }
}
