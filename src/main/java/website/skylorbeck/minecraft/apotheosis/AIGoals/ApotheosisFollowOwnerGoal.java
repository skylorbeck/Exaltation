package website.skylorbeck.minecraft.apotheosis.AIGoals;

import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.*;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

import java.util.EnumSet;

public class ApotheosisFollowOwnerGoal extends Goal {

	private LivingEntity owner;
	private final LivingEntity pet;
	private final WorldView world;
	private final double speed;
	private final EntityNavigation navigation;
	private int updateCountdownTicks;
	private final float maxDistance;
	private final float minDistance;
	private final boolean leavesAllowed;

	public ApotheosisFollowOwnerGoal(MobEntity pet, LivingEntity owner, double speed, float minDistance, float maxDistance, boolean leavesAllowed) {
		this.world = pet.world;
		this.pet = pet;
		this.owner = owner;
		this.speed = speed;
		this.navigation = pet.getNavigation();
		this.minDistance = minDistance;
		this.maxDistance = maxDistance;
		this.leavesAllowed = leavesAllowed;
		this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
		if (!(pet.getNavigation() instanceof MobNavigation) && !(pet.getNavigation() instanceof BirdNavigation)) {
			throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
		}
	}

	public boolean canStart() {
		if (owner == null) {
			return false;
		} else if (owner.isSpectator()) {
			return false;
		} else return !(this.pet.squaredDistanceTo(owner) < (double) (this.minDistance * this.minDistance));
	}

	public boolean shouldContinue() {
		if (this.navigation.isIdle()) {
			return false;
		} else {
			return !(this.pet.squaredDistanceTo(this.owner) <= (double)(this.maxDistance * this.maxDistance));
		}
	}

	public void start() {
		this.updateCountdownTicks = 0;
	}

	public void stop() {
//		this.owner = null;
		this.navigation.stop();
	}

	public void tick() {
		if (--this.updateCountdownTicks <= 0) {
			this.updateCountdownTicks = 10;
			if (!this.pet.hasVehicle()) {
				if (this.pet.squaredDistanceTo(this.owner) >= 144.0D) {
					this.tryTeleport();
				} else {
					this.navigation.startMovingTo(this.owner, this.speed);
				}

			}
		}
	}

	private void tryTeleport() {
		BlockPos blockPos = this.owner.getBlockPos();

		for(int i = 0; i < 10; ++i) {
			int j = this.getRandomInt(-3, 3);
			int k = this.getRandomInt(-1, 1);
			int l = this.getRandomInt(-3, 3);
			boolean bl = this.tryTeleportTo(blockPos.getX() + j, blockPos.getY() + k, blockPos.getZ() + l);
			if (bl) {
				return;
			}
		}

	}

	private boolean tryTeleportTo(int x, int y, int z) {
		if (Math.abs((double)x - this.owner.getX()) < 2.0D && Math.abs((double)z - this.owner.getZ()) < 2.0D) {
			return false;
		} else if (!this.canTeleportTo(new BlockPos(x, y, z))) {
			return false;
		} else {
			this.pet.refreshPositionAndAngles((double)x + 0.5D, (double)y, (double)z + 0.5D, this.pet.getYaw(), this.pet.getPitch());
			this.navigation.stop();
			return true;
		}
	}

	private boolean canTeleportTo(BlockPos pos) {
		PathNodeType pathNodeType = LandPathNodeMaker.getLandNodeType(this.world, pos.mutableCopy());
		if (pathNodeType != PathNodeType.WALKABLE) {
			return false;
		} else {
			BlockState blockState = this.world.getBlockState(pos.down());
			if (!this.leavesAllowed && blockState.getBlock() instanceof LeavesBlock) {
				return false;
			} else {
				BlockPos blockPos = pos.subtract(this.pet.getBlockPos());
				return this.world.isSpaceEmpty(this.pet, this.pet.getBoundingBox().offset(blockPos));
			}
		}
	}

	private int getRandomInt(int min, int max) {
		return this.pet.getRandom().nextInt(max - min + 1) + min;
	}
}
