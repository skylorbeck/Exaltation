package website.skylorbeck.minecraft.apotheosis.cardinal;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class XPComponent implements XPComponentInterface, AutoSyncedComponent {
    private int Level = 1;
    private boolean ascended = false;
    private UUID petUUID;
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
//        if (this.Level ==50 && ModComponents.ORIGIN.get(playerEntity).getOrigin(OriginLayers.getLayer(new Identifier("apotheosis","class"))).hasUpgrade()){
//            playerEntity.sendMessage(new TranslatableText("apotheosis.levelup1"),false);
//            playerEntity.sendMessage(new TranslatableText("apotheosis.levelup2"),false);
//        }
    }

    @Override
    public void setLevel(int level) {
        this.Level = level;
    }

    @Override
    public void readFromNbt(@NotNull NbtCompound tag) {
        if (tag.contains("petUUID"))
        this.petUUID = tag.getUuid("petUUID");
        this.Level = tag.getInt("APOLV");
        this.ascended = tag.getBoolean("ASCEND");
    }

    @Override
    public void writeToNbt(@NotNull NbtCompound tag) {
        if (this.petUUID!=null)
            tag.putUuid("petUUID",this.petUUID);
        else
            tag.remove("petUUID");
        tag.putInt("APOLV",this.Level);
        tag.putBoolean("ASCEND",this.ascended);
    }

    public UUID getPetUUID() {
        return petUUID;
    }

    public void setPetUUID(UUID petUUID) {
        this.petUUID = petUUID;

    }

    public boolean getAscended() {
        return this.ascended;
    }
}
