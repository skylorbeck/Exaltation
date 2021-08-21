package website.skylorbeck.minecraft.apotheosis.cardinal;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class XPComponent implements XPComponentInterface, AutoSyncedComponent {
    private float XP = 0;
    private int Level = 0;
    private final PlayerEntity playerEntity;

    public XPComponent(PlayerEntity provider) {
        this.playerEntity = provider;
    }

    @Override
    public float getXP() {
        return this.XP;
    }

    @Override
    public void addXP(float xp) {
        this.XP+=xp;
    }

    @Override
    public void setXP(float xp) {
        this.XP = xp;
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
    public void readFromNbt(NbtCompound tag) {
        this.XP = tag.getFloat("APOXP");
        this.Level = tag.getInt("APOLV");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putFloat("APOXP",this.XP);
        tag.putInt("APOLV",this.Level);
    }
}
