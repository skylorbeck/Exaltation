package website.skylorbeck.minecraft.apotheosis.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import website.skylorbeck.minecraft.apotheosis.powers.MarksmanArrowCyclingPower;
import website.skylorbeck.minecraft.apotheosis.powers.RangerDamagePower;
import website.skylorbeck.minecraft.apotheosis.powers.RangerRangedItemAccuracyPower;

import static net.minecraft.item.CrossbowItem.hasProjectile;
import static website.skylorbeck.minecraft.apotheosis.cardinal.ApotheosisComponents.APOXP;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin {

    @Shadow
    private static void shoot(World world, LivingEntity shooter, Hand hand, ItemStack crossbow, ItemStack projectile, float soundPitch, boolean creative, float speed, float divergence, float simulated) {
    }

    @Redirect( at = @At(value = "INVOKE",target = "Lnet/minecraft/item/CrossbowItem;shoot(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;FZFFF)V"), method = "shootAll")
    private static void injectedShootAll(World world, LivingEntity shooter, Hand hand, ItemStack crossbow, ItemStack projectile, float soundPitch, boolean creative, float speed, float divergence, float simulated) {
        if (PowerHolderComponent.hasPower(shooter, RangerRangedItemAccuracyPower.class)) {
            RangerRangedItemAccuracyPower power = PowerHolderComponent.getPowers(shooter, RangerRangedItemAccuracyPower.class).get(0);
            divergence = 1.0f - (Math.floorDiv(APOXP.get(shooter).getLevel(), power.getScale()) * 0.02f);
        }
        if (PowerHolderComponent.hasPower(shooter, RangerRangedItemAccuracyPower.class)) {
            RangerRangedItemAccuracyPower power = PowerHolderComponent.getPowers(shooter, RangerRangedItemAccuracyPower.class).get(0);
            speed = (hasProjectile(crossbow, Items.FIREWORK_ROCKET) ? 1.6F : 3.15F) + (Math.floorDiv(APOXP.get(shooter).getLevel(), power.getScale()) * ((hasProjectile(crossbow, Items.FIREWORK_ROCKET) ? 1.6F : 3.15F)/100f));
        }
        shoot(world, shooter, hand, crossbow, projectile, soundPitch, creative, speed, divergence, simulated);
    }

    @Inject(method = "createArrow", at = @At(value = "RETURN", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;setCritical(Z)V"), cancellable = true)
    private static void injectedShoot(World world, LivingEntity entity, ItemStack crossbow, ItemStack arrow, CallbackInfoReturnable<PersistentProjectileEntity> cir) {
        if (PowerHolderComponent.hasPower(entity, RangerDamagePower.class)) {
            RangerDamagePower power = PowerHolderComponent.getPowers(entity, RangerDamagePower.class).get(0);
            PersistentProjectileEntity persistentProjectileEntity = cir.getReturnValue();
            persistentProjectileEntity.setDamage(persistentProjectileEntity.getDamage() + power.getDamage() + (power.getDamageScaled() * Math.floorDiv(APOXP.get(entity).getLevel(), power.getScale())));
            cir.setReturnValue(persistentProjectileEntity);
        }
    }

    @Redirect(at = @At(value = "INVOKE",target = "Lnet/minecraft/entity/LivingEntity;getArrowType(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;"),method = "loadProjectiles")
    private static ItemStack redirectGetArrowType(LivingEntity entity, ItemStack stack){
        ItemStack arrowStack = entity.getArrowType(stack);
        ItemStack newArrow = arrowStack;
        if (PowerHolderComponent.hasPower(entity, MarksmanArrowCyclingPower.class)) {
            MarksmanArrowCyclingPower marksmanArrowCyclingPower = PowerHolderComponent.KEY.get(entity).getPowers(MarksmanArrowCyclingPower.class).get(0);
            if (marksmanArrowCyclingPower.isActive() && arrowStack != ItemStack.EMPTY) {
                newArrow = arrowStack.split(1);
                if (newArrow.isOf(Items.ARROW)){
                    newArrow = Items.TIPPED_ARROW.getDefaultStack();
                    newArrow.setCount(1);
                }
                PotionUtil.setPotion(newArrow, marksmanArrowCyclingPower.getPotion());
            }
        }
        return newArrow;
    }
}
