package website.skylorbeck.minecraft.apotheosis.cardinal;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import io.github.apace100.origins.origin.OriginLayer;
import io.github.apace100.origins.origin.OriginLayers;
import io.github.apace100.origins.registry.ModComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class XPComponent implements XPComponentInterface, AutoSyncedComponent {
    private int Level = 1;
    private boolean ascended = false;
    private final PlayerEntity playerEntity;

    public XPComponent(PlayerEntity provider) {
        this.playerEntity = provider;
    }
    public void setAscended(boolean ascended){
        this.ascended = ascended;
    }

    public int getLevelUpCost(){
        return (this.Level+1)%5==0?this.Level+6:this.Level+1;
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
        this.Level = tag.getInt("APOLV");
        this.ascended = tag.getBoolean("ASCEND");
    }

    @Override
    public void writeToNbt(@NotNull NbtCompound tag) {
        tag.putInt("APOLV",this.Level);
        tag.putBoolean("ASCEND",this.ascended);
    }

    public boolean getAscended() {
        return this.ascended;
    }
}
