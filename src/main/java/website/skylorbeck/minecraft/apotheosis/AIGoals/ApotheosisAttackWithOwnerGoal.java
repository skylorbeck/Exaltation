package website.skylorbeck.minecraft.apotheosis.AIGoals;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.TameableEntity;

import java.util.EnumSet;

public class ApotheosisAttackWithOwnerGoal extends TrackTargetGoal {
    private final LivingEntity owner;
    private LivingEntity attacking;
    private int lastAttackTime;

    public ApotheosisAttackWithOwnerGoal(LivingEntity owner, MobEntity pet) {
        super(pet, false);
        this.owner = owner;
        this.setControls(EnumSet.of(Goal.Control.TARGET));
    }

    public boolean canStart() {

            if (owner == null) {
                return false;
            } else {
                this.attacking = owner.getAttacking();
                int i = owner.getLastAttackTime();
                TargetPredicate targetPredicate =  TargetPredicate.DEFAULT;
                targetPredicate.setPredicate((LivingEntity::isAlive));
                return i != this.lastAttackTime && this.canTrack(this.attacking, targetPredicate);
            }

    }

    public void start() {
        this.mob.setTarget(this.attacking);
        if (owner != null) {
            this.lastAttackTime = owner.getLastAttackTime();
        }
        super.start();
    }
}
