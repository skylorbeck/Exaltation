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
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import website.skylorbeck.minecraft.apotheosis.blocks.AltarAbstract;
import website.skylorbeck.minecraft.apotheosis.blocks.AltarBlockItem;
import website.skylorbeck.minecraft.apotheosis.blocks.entities.AltarEntity;
import website.skylorbeck.minecraft.apotheosis.blocks.screens.AltarScreenHandler;
import website.skylorbeck.minecraft.apotheosis.enchantment.*;
import website.skylorbeck.minecraft.apotheosis.statuseffects.WolfMarkEffect;

import java.util.UUID;

public class Declarar {
    public static final String MODID = "apotheosis";
    public static Identifier getIdentifier(String string){
        return new Identifier(MODID,string);
    }
//todo check breakbytool
    public static final Block stonealtar = new AltarAbstract(FabricBlockSettings.copyOf(Blocks.STONE).nonOpaque().luminance(5),0);
    public static final Block ironaltar = new AltarAbstract(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).nonOpaque().luminance(5),1);
    public static final Block goldaltar = new AltarAbstract(FabricBlockSettings.copyOf(Blocks.GOLD_BLOCK).nonOpaque().luminance(5),2);
    public static final Block diamondaltar = new AltarAbstract(FabricBlockSettings.copyOf(Blocks.DIAMOND_BLOCK).nonOpaque().luminance(5),3);
    public static final Block netheritealtar = new AltarAbstract(FabricBlockSettings.copyOf(Blocks.NETHERITE_BLOCK).nonOpaque().luminance(5),4);

    public static final BlockItem stonealtarItem = new AltarBlockItem(stonealtar,new FabricItemSettings().group(ItemGroup.MISC));
    public static final BlockItem ironaltarItem = new AltarBlockItem(ironaltar,new FabricItemSettings().group(ItemGroup.MISC));
    public static final BlockItem goldaltarItem = new AltarBlockItem(goldaltar,new FabricItemSettings().group(ItemGroup.MISC));
    public static final BlockItem diamondaltarItem = new AltarBlockItem(diamondaltar,new FabricItemSettings().group(ItemGroup.MISC));
    public static final BlockItem netheritealtarItem = new AltarBlockItem(netheritealtar,new FabricItemSettings().group(ItemGroup.MISC));

    public static final BlockEntityType<AltarEntity> ALTARENTITY = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            getIdentifier("altarentity"),
            FabricBlockEntityTypeBuilder.create(
                    AltarEntity::new,
                    stonealtar,
                    ironaltar,
                    goldaltar,
                    diamondaltar,
                    netheritealtar
            ).build(null));
    public static final ScreenHandlerType<AltarScreenHandler> ALTARSCREENHANDLER =
            ScreenHandlerRegistry.registerExtended(getIdentifier("altarscreen"),AltarScreenHandler::new);

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
    public static final Enchantment SPEEDBOOSTER = Registry.register(
            Registry.ENCHANTMENT,
            getIdentifier("speedbooster"),
            new SpeedBooster()
    );

    public static final Enchantment WITHERASPECT = Registry.register(
            Registry.ENCHANTMENT,
            getIdentifier("witheraspect"),
            new WitherAspect()
    );
    public static final Enchantment POISONASPECT = Registry.register(
            Registry.ENCHANTMENT,
            getIdentifier("poisonaspect"),
            new PoisonAspect()
    );
    public static final Enchantment FROSTASPECT = Registry.register(
            Registry.ENCHANTMENT,
            getIdentifier("frostaspect"),
            new FrostAspect()
    );

    public static UUID healthBoostUUID = UUID.randomUUID();
    public static UUID knockbackResistUUID = UUID.randomUUID();
    public static UUID armorsharpnessUUID = UUID.randomUUID();
    public static UUID speedboosterUUID = UUID.randomUUID();
    public static UUID witheraspectUUID = UUID.randomUUID();
    public static UUID poisonaspectUUID = UUID.randomUUID();
    public static UUID frostaspectUUID = UUID.randomUUID();


    public static StatusEffect WOLFMARK = new WolfMarkEffect(StatusEffectType.HARMFUL, DyeColor.RED.getSignColor());

    public static EntityAttributeModifier healthBoostEAM(LivingEntity entity){
        return new EntityAttributeModifier(healthBoostUUID,"apohpboost", EnchantmentHelper.getEquipmentLevel(Declarar.HEALTHBOOST,entity)*2, EntityAttributeModifier.Operation.ADDITION);
    }
    public static EntityAttributeModifier knockbackResistEAM(LivingEntity entity){
        return new EntityAttributeModifier(knockbackResistUUID,"apoknockbackresist", net.minecraft.enchantment.EnchantmentHelper.getEquipmentLevel(Declarar.KNOCKBACKRESIST,entity)*0.25, EntityAttributeModifier.Operation.ADDITION);
    }
    public static EntityAttributeModifier armorsharpnessEAM(LivingEntity entity){
        return new EntityAttributeModifier(armorsharpnessUUID,"apoarmorsharpness", EnchantmentHelper.getEquipmentLevel(Declarar.ARMORSHARPNESS,entity)*0.5, EntityAttributeModifier.Operation.ADDITION);
    }
    public static EntityAttributeModifier speedboosterEAM(LivingEntity entity){
        return new EntityAttributeModifier(speedboosterUUID,"speedbooster", EnchantmentHelper.getEquipmentLevel(Declarar.SPEEDBOOSTER,entity)*0.01, EntityAttributeModifier.Operation.ADDITION);
    }

}
