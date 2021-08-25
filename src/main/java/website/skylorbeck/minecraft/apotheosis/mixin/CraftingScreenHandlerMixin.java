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
import website.skylorbeck.minecraft.apotheosis.powers.SmithingArmorPower;
import website.skylorbeck.minecraft.apotheosis.powers.SmithingWeaponPower;
import website.skylorbeck.minecraft.apotheosis.powers.WarsmithShieldBuffPower;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import static website.skylorbeck.minecraft.apotheosis.cardinal.ApotheosisComponents.APOXP;

@Mixin(CraftingScreenHandler.class)
public class CraftingScreenHandlerMixin {
    @Shadow @Final private ScreenHandlerContext context;

    @ModifyVariable(method = "updateResult",name = "itemStack", at = @At(value = "INVOKE",target = "Lnet/minecraft/inventory/CraftingResultInventory;setStack(ILnet/minecraft/item/ItemStack;)V"))
    private static ItemStack injectedUpdateResult(ItemStack stack) {
        if (isSmith()) {
            Item item = stack.getItem();
            int scale= 0;
            if (item instanceof ArmorItem) {
                stack.getOrCreateNbt().putBoolean("ApoSmith", true);
                scale = smithArmorScale();
                /*NbtList lore = new NbtList();
                lore.add(NbtString.of("Crafted by: "+ MinecraftClient.getInstance().getServer().getPlayerManager().getPlayer(MinecraftClient.getInstance().player.getUuid()).getEntityName()));
                stack.getOrCreateSubNbt("display").put("Lore",lore);*/
                if (scale != 0) {
                    stack.addEnchantment(Enchantments.UNBREAKING, scale);
                }
            } else if (item instanceof SwordItem || item instanceof AxeItem) {
                stack.getOrCreateNbt().putBoolean("ApoSmith", true);
                scale = smithWeaponScale();
                if (scale != 0) {
                    stack.addEnchantment(Enchantments.SHARPNESS, scale);
                }
            }
        }
        if (warsmithShield()){
            Item item = stack.getItem();
            if (item instanceof ShieldItem) {
                stack.getOrCreateNbt().putBoolean("ApoSmith", true);
                    stack.addEnchantment(Declarar.HEALTHBOOST,1);
                    stack.addEnchantment(Declarar.KNOCKBACKRESIST,1);
                }
            }
        return stack;
    }

    private static boolean isSmith(){
        assert MinecraftClient.getInstance().player != null;
        return PowerHolderComponent.hasPower(MinecraftClient.getInstance().player,SmithingArmorPower.class)||PowerHolderComponent.hasPower(MinecraftClient.getInstance().player,SmithingWeaponPower.class);
    }
    private static boolean warsmithShield(){
        assert MinecraftClient.getInstance().player != null;
        return PowerHolderComponent.hasPower(MinecraftClient.getInstance().player, WarsmithShieldBuffPower.class);
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
