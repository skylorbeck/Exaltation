package website.skylorbeck.minecraft.apotheosis.AIGoals;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.TameableEntity;

import java.util.EnumSet;

public class ApotheosisTrackOwnerAttackerGoal extends TrackTargetGoal {
    private final LivingEntity owner;
    private LivingEntity attacker;
    private int lastAttackedTime;

    public ApotheosisTrackOwnerAttackerGoal(LivingEntity owner, MobEntity pet) {
        super(pet, false);
        this.owner = owner;
        this.setControls(EnumSet.of(Goal.Control.TARGET));
    }

    public boolean canStart() {
            if (owner == null) {
                return false;
            } else {
                this.attacker = owner.getAttacker();
                int i = owner.getLastAttackedTime();
                return i != this.lastAttackedTime && this.canTrack(this.attacker, TargetPredicate.DEFAULT);
            }
    }

    public void start() {
        this.mob.setTarget(this.attacker);
        if (owner != null) {
            this.lastAttackedTime = owner.getLastAttackedTime();
        }
        super.start();
    }
}
