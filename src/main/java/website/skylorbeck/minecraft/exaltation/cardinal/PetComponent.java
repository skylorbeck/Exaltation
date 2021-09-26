package website.skylorbeck.minecraft.exaltation.cardinal;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;

import java.util.UUID;

public class PetComponent implements AutoSyncedComponent {
    private final MobEntity pet;
    private int timeLeft = 0;
    private UUID ownerUUID;
    private boolean healOwner = false;

    public PetComponent(MobEntity provider) {
        this.pet = provider;

    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        if (tag.contains("owner"))
        this.ownerUUID = tag.getUuid("owner");
        if (tag.contains("timeleft"))
            this.timeLeft = tag.getInt("timeleft");
        if (tag.contains("heal"))
            this.healOwner = tag.getBoolean("heal");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        if (this.ownerUUID != null) {
            tag.putUuid("owner", this.ownerUUID);
        }
        tag.putInt("timeleft", this.timeLeft);
        tag.putBoolean("heal", this.healOwner);
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public void setOwnerUUID(UUID ownerUUID) {
        this.ownerUUID = ownerUUID;
    }

    public boolean shouldHealOwner() {
        return healOwner;
    }

    public void setHealOwner(boolean healOwner) {
        this.healOwner = healOwner;
    }
}
