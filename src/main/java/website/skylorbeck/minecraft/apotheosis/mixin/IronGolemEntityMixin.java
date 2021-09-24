package website.skylorbeck.minecraft.apotheosis.mixin;

import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IronGolemEntity.class)
public class IronGolemEntityMixin {
    @Inject(at = @At(value = "RETURN"),method = "initGoals", cancellable = true)
    public void attackUndeadPlayer(CallbackInfo ci){
        ((MobEntityAccessor)this).getTargetSelector().add(2, new FollowTargetGoal(((MobEntity)(Object)this), PlayerEntity.class,10,true,false,(entity -> ((LivingEntity)entity).getGroup()== EntityGroup.UNDEAD)));
    }
}
