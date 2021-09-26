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
    private boolean addDamage(World world, Entity entity,ItemStack stack, World worldx, LivingEntity user, int remainingUseTicks){
//        LivingEntity user = MinecraftClient.getInstance().player;
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


    @ModifyConstant(method = "onStoppedUsing",constant = @Constant(floatValue = 1.0f,ordinal = 0))
    private float modifyAccuracy(float accuracy,ItemStack stack, World world, LivingEntity user, int remainingUseTicks){
//        LivingEntity user = MinecraftClient.getInstance().player;
        if( PowerHolderComponent.hasPower(user, RangerRangedItemAccuracyPower.class)){
            RangerRangedItemAccuracyPower power =PowerHolderComponent.getPowers(user, RangerRangedItemAccuracyPower.class).get(0);
            accuracy= 1.0f-(Math.floorDiv(APOXP.get(user).getLevel(),power.getScale())*0.02f);
        }
        return accuracy;
    }
    @ModifyConstant(method = "onStoppedUsing",constant = @Constant(floatValue = 3.0f,ordinal = 0))
    private float modifySpeed(float speed,ItemStack stack, World world, LivingEntity user, int remainingUseTicks){
//        LivingEntity user = MinecraftClient.getInstance().player;
        if( PowerHolderComponent.hasPower(user, RangerRangedItemAccuracyPower.class)){
            RangerRangedItemAccuracyPower power =PowerHolderComponent.getPowers(user, RangerRangedItemAccuracyPower.class).get(0);
            speed= 3.0f+(Math.floorDiv(APOXP.get(user).getLevel(),power.getScale())*0.03f);
        }
        return speed;
    }
}
