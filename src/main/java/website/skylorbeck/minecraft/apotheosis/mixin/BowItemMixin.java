package website.skylorbeck.minecraft.apotheosis.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.Power;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import website.skylorbeck.minecraft.apotheosis.powers.RangerDamagePower;
import website.skylorbeck.minecraft.apotheosis.powers.RangerRangedItemAccuracyPower;
import website.skylorbeck.minecraft.apotheosis.powers.SmithingArmorPower;

import static website.skylorbeck.minecraft.apotheosis.cardinal.ApotheosisComponents.APOXP;

@Mixin(BowItem.class)
public class BowItemMixin {
    @ModifyVariable(name = "persistentProjectileEntity",at = @At(value = "INVOKE",target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"),method = "onStoppedUsing")
    private PersistentProjectileEntity injectedOnStoppedUsing(PersistentProjectileEntity persistentProjectileEntity){
        LivingEntity user = MinecraftClient.getInstance().player;
        if( PowerHolderComponent.hasPower(user, RangerDamagePower.class)){
            RangerDamagePower power =PowerHolderComponent.getPowers(user, RangerDamagePower.class).get(0);
            persistentProjectileEntity.setDamage(persistentProjectileEntity.getDamage()+power.getDamage()+(power.getDamageScaled()*Math.floorDiv(  APOXP.get(user).getLevel(),power.getScale())));
        }
        return persistentProjectileEntity;
    }
    @ModifyConstant(method = "onStoppedUsing",constant = @Constant(floatValue = 1.0f,ordinal = 0))
    private float injectedOnStoppedUsing2(float accuracy){
        LivingEntity user = MinecraftClient.getInstance().player;
        if( PowerHolderComponent.hasPower(user, RangerRangedItemAccuracyPower.class)){
            RangerRangedItemAccuracyPower power =PowerHolderComponent.getPowers(user, RangerRangedItemAccuracyPower.class).get(0);
            accuracy= 1.0f-(Math.floorDiv(APOXP.get(user).getLevel(),power.getScale())*0.02f);
        }
        return accuracy;
    }
    @ModifyConstant(method = "onStoppedUsing",constant = @Constant(floatValue = 3.0f,ordinal = 0))
    private float injectedOnStoppedUsing3(float speed){
        LivingEntity user = MinecraftClient.getInstance().player;
        if( PowerHolderComponent.hasPower(user, RangerRangedItemAccuracyPower.class)){
            RangerRangedItemAccuracyPower power =PowerHolderComponent.getPowers(user, RangerRangedItemAccuracyPower.class).get(0);
            speed= 3.0f+(Math.floorDiv(APOXP.get(user).getLevel(),power.getScale())*0.03f);
        }
        return speed;
    }
}
