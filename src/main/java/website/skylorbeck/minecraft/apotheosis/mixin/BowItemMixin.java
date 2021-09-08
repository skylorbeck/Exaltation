package website.skylorbeck.minecraft.apotheosis.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import website.skylorbeck.minecraft.apotheosis.powers.MarksmanArrowCyclingPower;
import website.skylorbeck.minecraft.apotheosis.powers.RangerDamagePower;
import website.skylorbeck.minecraft.apotheosis.powers.RangerRangedItemAccuracyPower;

import static website.skylorbeck.minecraft.apotheosis.cardinal.ApotheosisComponents.APOXP;

@Mixin(BowItem.class)
public class BowItemMixin {
    @Redirect(at = @At(value = "INVOKE",target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"),method = "onStoppedUsing")
    private boolean addDamage(World world, Entity entity){
        LivingEntity user = MinecraftClient.getInstance().player;
        PersistentProjectileEntity persistentProjectileEntity = (PersistentProjectileEntity) entity;
        if( PowerHolderComponent.hasPower(user, RangerDamagePower.class)){
            RangerDamagePower power =PowerHolderComponent.getPowers(user, RangerDamagePower.class).get(0);
            persistentProjectileEntity.setDamage(persistentProjectileEntity.getDamage()+power.getDamage()+(power.getDamageScaled()*Math.floorDiv(  APOXP.get(user).getLevel(),power.getScale())));
        }
        if (PowerHolderComponent.hasPower(user, MarksmanArrowCyclingPower.class)) {
            MarksmanArrowCyclingPower marksmanArrowCyclingPower = PowerHolderComponent.KEY.get(user).getPowers(MarksmanArrowCyclingPower.class).get(0);
            if (marksmanArrowCyclingPower.isActive() && !marksmanArrowCyclingPower.doDamage()){
                persistentProjectileEntity.setDamage(1);
            }
        }
        return world.spawnEntity(entity);
    }
    @Redirect(at = @At(value = "INVOKE",target = "Lnet/minecraft/entity/player/PlayerEntity;getArrowType(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;"),method = "onStoppedUsing")
    private ItemStack redirectGetArrowType(PlayerEntity playerEntity, ItemStack stack){
        ItemStack arrowStack = playerEntity.getArrowType(stack);
        ItemStack newArrow = arrowStack;
        if (PowerHolderComponent.hasPower(playerEntity, MarksmanArrowCyclingPower.class)) {
            MarksmanArrowCyclingPower marksmanArrowCyclingPower = PowerHolderComponent.KEY.get(playerEntity).getPowers(MarksmanArrowCyclingPower.class).get(0);
            if (marksmanArrowCyclingPower.isActive() && arrowStack != ItemStack.EMPTY) {
                newArrow = arrowStack.split(1);
                if (newArrow.isOf(Items.ARROW)){
                    newArrow = Items.TIPPED_ARROW.getDefaultStack();
                    newArrow.setCount(1);
                }
                PotionUtil.setPotion(newArrow, marksmanArrowCyclingPower.getPotion());
            }
        }//todo move this to arrow.onhit instead of enchanting the arrow
        return newArrow;
    }

    @ModifyConstant(method = "onStoppedUsing",constant = @Constant(floatValue = 1.0f,ordinal = 0))
    private float modifyAccuracy(float accuracy){
        LivingEntity user = MinecraftClient.getInstance().player;
        if( PowerHolderComponent.hasPower(user, RangerRangedItemAccuracyPower.class)){
            RangerRangedItemAccuracyPower power =PowerHolderComponent.getPowers(user, RangerRangedItemAccuracyPower.class).get(0);
            accuracy= 1.0f-(Math.floorDiv(APOXP.get(user).getLevel(),power.getScale())*0.02f);
        }
        return accuracy;
    }
    @ModifyConstant(method = "onStoppedUsing",constant = @Constant(floatValue = 3.0f,ordinal = 0))
    private float modifySpeed(float speed){
        LivingEntity user = MinecraftClient.getInstance().player;
        if( PowerHolderComponent.hasPower(user, RangerRangedItemAccuracyPower.class)){
            RangerRangedItemAccuracyPower power =PowerHolderComponent.getPowers(user, RangerRangedItemAccuracyPower.class).get(0);
            speed= 3.0f+(Math.floorDiv(APOXP.get(user).getLevel(),power.getScale())*0.03f);
        }
        return speed;
    }
}
