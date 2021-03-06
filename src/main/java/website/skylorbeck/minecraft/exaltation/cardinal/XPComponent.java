package website.skylorbeck.minecraft.exaltation.cardinal;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class XPComponent implements XPComponentInterface, AutoSyncedComponent {
    private int Level = 1;
    private boolean ascended = false;
    private UUID[] petUUID;
    private final PlayerEntity playerEntity;

    public XPComponent(PlayerEntity provider) {
        this.playerEntity = provider;
    }
    public void setAscended(boolean ascended){
        this.ascended = ascended;
    }

    public int getLevelUpCost(){
        int cost = Math.min(Level, 20);
        cost+=(Level+1)%5==0?5:0;
        return cost;
    }

    @Override
    public int getLevel() {
        return this.Level;
    }

    @Override
    public void addLevel(int level) {
        this.Level+=level;
    }

    @Override
    public void setLevel(int level) {
        this.Level = level;
    }

    @Override
    public void readFromNbt(@NotNull NbtCompound tag) {
        if (tag.contains("petUUID")) {
            this.petUUID = new UUID[tag.getInt("petUUID")];
            for (int i = 0; i < tag.getInt("petUUID"); i++) {
                this.petUUID[i] =  tag.getUuid("petUUID"+i);
            }
        }
        this.Level = tag.getInt("APOLV");
        this.ascended = tag.getBoolean("ASCEND");
    }

    @Override
    public void writeToNbt(@NotNull NbtCompound tag) {
        if (this.petUUID!=null) {
            for (int i = 0; i < this.petUUID.length; i++) {
                tag.putUuid("petUUID" + i, this.petUUID[i]);
            }
            tag.putInt("petUUID",petUUID.length);
        } else {
            tag.remove("petUUID");
        }
        tag.putInt("APOLV",this.Level);
        tag.putBoolean("ASCEND",this.ascended);
    }

    public UUID[] getPetUUID() {
        return petUUID;
    }

    public void setPetUUID(UUID[] petUUID) {
        this.petUUID = petUUID;

    }

    public boolean getAscended() {
        return this.ascended;
    }
}
