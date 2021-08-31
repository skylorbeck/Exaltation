package website.skylorbeck.minecraft.apotheosis.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.shadowed.eliotlash.mclib.math.functions.limit.Min;
import website.skylorbeck.minecraft.apotheosis.powers.RangerDamagePower;
import website.skylorbeck.minecraft.apotheosis.powers.RangerRangedItemAccuracyPower;

import static net.minecraft.item.CrossbowItem.hasProjectile;
import static website.skylorbeck.minecraft.apotheosis.cardinal.ApotheosisComponents.APOXP;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {

    @ModifyVariable(name = "divergence", at = @At(value = "INVOKE"), method = "shootAll")
    private static float injectedShootAll(float divergence, World world, LivingEntity entity) {
        if (PowerHolderComponent.hasPower(entity, RangerRangedItemAccuracyPower.class)) {
            RangerRangedItemAccuracyPower power = PowerHolderComponent.getPowers(entity, RangerRangedItemAccuracyPower.class).get(0);
            divergence = 1.0f - (Math.floorDiv(APOXP.get(entity).getLevel(), power.getScale()) * 0.02f);
        }
        return divergence;
    }

    @ModifyVariable(name = "speed", at = @At(value = "INVOKE"), method = "shootAll")
    private static float injectedShootAll2(float speed, World world, LivingEntity entity, Hand hand, ItemStack stack) {
        if (PowerHolderComponent.hasPower(entity, RangerRangedItemAccuracyPower.class)) {
            RangerRangedItemAccuracyPower power = PowerHolderComponent.getPowers(entity, RangerRangedItemAccuracyPower.class).get(0);
            speed = (hasProjectile(stack, Items.FIREWORK_ROCKET) ? 1.6F : 3.15F) + (Math.floorDiv(APOXP.get(entity).getLevel(), power.getScale()) * ((hasProjectile(stack, Items.FIREWORK_ROCKET) ? 1.6F : 3.15F)/100f));
        }
        return speed;
    }

    @ModifyVariable(method = "createArrow", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;setCritical(Z)V"), name = "persistentProjectileEntity")
    private static PersistentProjectileEntity injectedShoot(PersistentProjectileEntity persistentProjectileEntity, World world, LivingEntity entity) {
        if (PowerHolderComponent.hasPower(entity, RangerDamagePower.class)) {
            RangerDamagePower power = PowerHolderComponent.getPowers(entity, RangerDamagePower.class).get(0);
            persistentProjectileEntity.setDamage(persistentProjectileEntity.getDamage() + power.getDamage() + (power.getDamageScaled() * Math.floorDiv(APOXP.get(entity).getLevel(), power.getScale())));
        }
        return persistentProjectileEntity;
    }
}
