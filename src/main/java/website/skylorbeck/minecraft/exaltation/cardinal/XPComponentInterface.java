package website.skylorbeck.minecraft.exaltation.cardinal;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;

public interface XPComponentInterface extends ComponentV3 {

    int getLevel();

    void addLevel(int level);

    void setLevel(int level);


}
