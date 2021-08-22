package website.skylorbeck.minecraft.apotheosis;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;

public class Declarar {
    public static String MODID = "apotheosis";
    public static Identifier getMODID(String string){
        return new Identifier(MODID,string);
    }
    public static Block altar = new Block(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK));
    public static BlockItem altarItem = new BlockItem(altar,new FabricItemSettings().group(ItemGroup.MISC));

}
