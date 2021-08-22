package website.skylorbeck.minecraft.apotheosis.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
import website.skylorbeck.minecraft.apotheosis.powers.SmithingArmorPower;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import static website.skylorbeck.minecraft.apotheosis.cardinal.ApotheosisComponents.APOXP;

@Mixin(CraftingScreenHandler.class)
public class CraftingScreenHandlerMixin {
    @Shadow @Final private ScreenHandlerContext context;

    @ModifyVariable(method = "updateResult",name = "itemStack", at = @At(value = "INVOKE",target = "Lnet/minecraft/inventory/CraftingResultInventory;setStack(ILnet/minecraft/item/ItemStack;)V"))
    private static ItemStack injectedUpdateResult(ItemStack stack) {
        if (stack.getItem() instanceof ArmorItem) {
            if (isSmith()) {
                stack.getOrCreateNbt().putBoolean("ApoSmith", true);
                /*NbtList lore = new NbtList();
                lore.add(NbtString.of("Crafted by: "+ MinecraftClient.getInstance().getServer().getPlayerManager().getPlayer(MinecraftClient.getInstance().player.getUuid()).getEntityName()));
                stack.getOrCreateSubNbt("display").put("Lore",lore);*/
                int scale = smithScale();
                if (scale != 0) {
                    stack.addEnchantment(Enchantments.UNBREAKING, scale);
                }
            }
        }
        return stack;
    }
    private static boolean isSmith(){
        assert MinecraftClient.getInstance().player != null;
        return PowerHolderComponent.hasPower(MinecraftClient.getInstance().player,SmithingArmorPower.class);
    }
    private static int smithScale(){
        assert MinecraftClient.getInstance().player != null;
        MinecraftClient client = MinecraftClient.getInstance();
        PlayerEntity playerEntity = client.player;
        ServerPlayerEntity serverPlayerEntity = client.getServer().getPlayerManager().getPlayer(playerEntity.getUuid());
        assert serverPlayerEntity != null;
        int scale = (PowerHolderComponent.getPowers(serverPlayerEntity, SmithingArmorPower.class).get(0)).getScale();
//        Logger.getGlobal().log(Level.SEVERE,"SMITHSCALECHECK: "+scale + "::: power count: "+PowerHolderComponent.getPowers(serverPlayerEntity,SmithingArmorPower.class).size());

        if (scale!=0) {
            return Math.floorDiv(APOXP.get(serverPlayerEntity).getLevel(), scale);
        }
            else
                return 0;
    }
}
