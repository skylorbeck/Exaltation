package website.skylorbeck.minecraft.apotheosis.cardinal;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;

public interface XPComponentInterface extends ComponentV3 {
    float getXP();

    void addXP(float xp);

    void setXP(float xp);

    int getLevel();

    void addLevel(int level);

    void setLevel(int level);
}
