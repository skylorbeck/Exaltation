package website.skylorbeck.minecraft.exaltation.statuseffects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class DamageSource extends net.minecraft.entity.damage.DamageSource {
    public DamageSource(String name) {
        super(name);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean isProjectile() {
        return super.isProjectile();
    }

    @Override
    public net.minecraft.entity.damage.DamageSource setProjectile() {
        return super.setProjectile();
    }

    @Override
    public boolean isExplosive() {
        return super.isExplosive();
    }

    @Override
    public net.minecraft.entity.damage.DamageSource setExplosive() {
        return super.setExplosive();
    }

    @Override
    public boolean bypassesArmor() {
        return super.bypassesArmor();
    }

    @Override
    public boolean isFallingBlock() {
        return super.isFallingBlock();
    }

    @Override
    public float getExhaustion() {
        return super.getExhaustion();
    }

    @Override
    public boolean isOutOfWorld() {
        return super.isOutOfWorld();
    }

    @Override
    public boolean isUnblockable() {
        return super.isUnblockable();
    }

    @Nullable
    @Override
    public Entity getSource() {
        return super.getSource();
    }

    @Nullable
    @Override
    public Entity getAttacker() {
        return super.getAttacker();
    }

    @Override
    public net.minecraft.entity.damage.DamageSource setBypassesArmor() {
        return super.setBypassesArmor();
    }

    @Override
    public net.minecraft.entity.damage.DamageSource setFallingBlock() {
        return super.setFallingBlock();
    }

    @Override
    public net.minecraft.entity.damage.DamageSource setOutOfWorld() {
        return super.setOutOfWorld();
    }

    @Override
    public net.minecraft.entity.damage.DamageSource setUnblockable() {
        return super.setUnblockable();
    }

    @Override
    public net.minecraft.entity.damage.DamageSource setFire() {
        return super.setFire();
    }

    @Override
    public net.minecraft.entity.damage.DamageSource setNeutral() {
        return super.setNeutral();
    }

    @Override
    public Text getDeathMessage(LivingEntity entity) {
        return super.getDeathMessage(entity);
    }

    @Override
    public boolean isFire() {
        return super.isFire();
    }

    @Override
    public boolean isNeutral() {
        return super.isNeutral();
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public net.minecraft.entity.damage.DamageSource setScaledWithDifficulty() {
        return super.setScaledWithDifficulty();
    }

    @Override
    public boolean isScaledWithDifficulty() {
        return super.isScaledWithDifficulty();
    }

    @Override
    public boolean isMagic() {
        return super.isMagic();
    }

    @Override
    public net.minecraft.entity.damage.DamageSource setUsesMagic() {
        return super.setUsesMagic();
    }

    @Override
    public boolean isFromFalling() {
        return super.isFromFalling();
    }

    @Override
    public net.minecraft.entity.damage.DamageSource setFromFalling() {
        return super.setFromFalling();
    }

    @Override
    public boolean isSourceCreativePlayer() {
        return super.isSourceCreativePlayer();
    }

    @Nullable
    @Override
    public Vec3d getPosition() {
        return super.getPosition();
    }
}
