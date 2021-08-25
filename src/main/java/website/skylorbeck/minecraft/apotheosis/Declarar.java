package website.skylorbeck.minecraft.apotheosis;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import website.skylorbeck.minecraft.apotheosis.blocks.AltarAbstract;
import website.skylorbeck.minecraft.apotheosis.blocks.entities.AltarEntity;
import website.skylorbeck.minecraft.apotheosis.blocks.screens.AltarScreenHandler;
import website.skylorbeck.minecraft.apotheosis.enchantment.ArmorSharpness;
import website.skylorbeck.minecraft.apotheosis.enchantment.EnchantmentHelper;
import website.skylorbeck.minecraft.apotheosis.enchantment.HealthBooster;
import website.skylorbeck.minecraft.apotheosis.enchantment.KnockbackResist;

import java.util.UUID;

public class Declarar {
    public static final String MODID = "apotheosis";
    public static Identifier getIdentifier(String string){
        return new Identifier(MODID,string);
    }

    public static final Block altar = new AltarAbstract(FabricBlockSettings.copyOf(Blocks.STONE),0);
    public static final BlockItem altarItem = new BlockItem(altar,new FabricItemSettings().group(ItemGroup.MISC));

    public static final BlockEntityType<AltarEntity> ALTARENTITY = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            getIdentifier("altarentity"),
            FabricBlockEntityTypeBuilder.create(
                    AltarEntity::new,
                    altar
            ).build(null));
    public static final ScreenHandlerType<AltarScreenHandler> ALTARSCREENHANDLER =
            ScreenHandlerRegistry.registerSimple(getIdentifier("altarscreen"),AltarScreenHandler::new);

    public static final Enchantment HEALTHBOOST = Registry.register(
            Registry.ENCHANTMENT,
            getIdentifier("healthbooster"),
            new HealthBooster()
    );
    public static final Enchantment KNOCKBACKRESIST = Registry.register(
            Registry.ENCHANTMENT,
            getIdentifier("knockbackresist"),
            new KnockbackResist()
    );

    public static final Enchantment ARMORSHARPNESS = Registry.register(
            Registry.ENCHANTMENT,
            getIdentifier("armorsharpness"),
            new ArmorSharpness()
    );

    public static UUID healthBoostUUID = UUID.randomUUID();
    public static UUID knockbackResistUUID = UUID.randomUUID();
    public static UUID armorsharpnessUUID = UUID.randomUUID();

    public static EntityAttributeModifier healthBoostEAM(LivingEntity entity){
        return new EntityAttributeModifier(healthBoostUUID,"apohpboost", EnchantmentHelper.getEquipmentLevel(Declarar.HEALTHBOOST,entity)*2, EntityAttributeModifier.Operation.ADDITION);
    }
    public static EntityAttributeModifier knockbackResistEAM(LivingEntity entity){
        return new EntityAttributeModifier(knockbackResistUUID,"apoknockbackresist", net.minecraft.enchantment.EnchantmentHelper.getEquipmentLevel(Declarar.KNOCKBACKRESIST,entity)*0.25, EntityAttributeModifier.Operation.ADDITION);
    }
    public static EntityAttributeModifier armorsharpnessEAM(LivingEntity entity){
        return new EntityAttributeModifier(armorsharpnessUUID,"apoarmorsharpness", EnchantmentHelper.getEquipmentLevel(Declarar.ARMORSHARPNESS,entity)*0.5, EntityAttributeModifier.Operation.ADDITION);
    }

}
