package website.skylorbeck.minecraft.apotheosis.powers;

import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.*;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.apoli.util.AttributedEntityAttributeModifier;
import io.github.apace100.apoli.util.HudRender;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;
import website.skylorbeck.minecraft.apotheosis.data.ApoDataTypes;
import website.skylorbeck.minecraft.apotheosis.Declarar;
import website.skylorbeck.minecraft.apotheosis.data.QuiverData;

import java.util.List;
import java.util.function.BiFunction;

public class PowerFactories {
    public static void register(){
        register(new PowerFactory<>(Declarar.getIdentifier("branching"),
                new SerializableData().add("level",SerializableDataTypes.INT,0),
                data -> (type,player) -> new BranchingClassPower(type,player,data.getInt("level"))));
        register(new PowerFactory<>(Declarar.getIdentifier("consuming"),
                new SerializableData()
                        .add("item",SerializableDataTypes.STRING,"minecraft:air"),
                data -> (type,player) -> new ConsumingItemPower(type,player,data.getString("item"))));
        register(new PowerFactory<>(Declarar.getIdentifier("reset_level"),
                new SerializableData(),
                data -> (BiFunction<PowerType<Power>, LivingEntity, Power>) ResetLevelPower::new));

        register(new PowerFactory<>(Declarar.getIdentifier( "conditioned_attribute"),
                new SerializableData()
                        .add("modifier", ApoliDataTypes.ATTRIBUTED_ATTRIBUTE_MODIFIER, null)
                        .add("modifiers", ApoliDataTypes.ATTRIBUTED_ATTRIBUTE_MODIFIERS, null)
                        .add("tick_rate", SerializableDataTypes.INT, 20),
                data ->
                        (type, player) -> {
                            ApoConditionedAttributePower ap = new ApoConditionedAttributePower(type, player, data.getInt("tick_rate"));
                            if(data.isPresent("modifier")) {
                                ap.addModifier((AttributedEntityAttributeModifier)data.get("modifier"));
                            }
                            if(data.isPresent("modifiers")) {
                                List<AttributedEntityAttributeModifier> modifierList = (List<AttributedEntityAttributeModifier>)data.get("modifiers");
                                modifierList.forEach(ap::addModifier);
                            }
                            return ap;
                        }).allowCondition());
        register(new PowerFactory<>(Declarar.getIdentifier("leveled_attribute"),
                new SerializableData()
                        .add("modifier", ApoliDataTypes.ATTRIBUTED_ATTRIBUTE_MODIFIER, null)
                        .add("tick_rate", SerializableDataTypes.INT, 20)
                        .add("scale", SerializableDataTypes.INT, 1),
                data ->
                        (type, player) -> {
                            LeveledAttributePower ap = new LeveledAttributePower(type, player, data.getInt("tick_rate"),data.getInt("scale"));
                            if(data.isPresent("modifier")) {
                                ap.addModifier((AttributedEntityAttributeModifier)data.get("modifier"));
                            }
                            return ap;
                        }).allowCondition());
        register(new PowerFactory<>(Declarar.getIdentifier("scaling_modify_damage_taken"),
                new SerializableData()
                        .add("damage_condition", ApoliDataTypes.DAMAGE_CONDITION, null)
                        .add("modifier", SerializableDataTypes.ATTRIBUTE_MODIFIER, null)
                        .add("self_action", ApoliDataTypes.ENTITY_ACTION, null)
                        .add("attacker_action", ApoliDataTypes.ENTITY_ACTION, null)
                        .add("scale",SerializableDataTypes.INT,0),
                data ->
                        (type, player) -> {
                            ScalingModifyDamageTakenPower power = new ScalingModifyDamageTakenPower(
                                    data.getInt("scale"),
                                    data.getModifier("modifier"),
                                    type,
                                    player,
                                    data.isPresent("damage_condition") ? (ConditionFactory<Pair<DamageSource, Float>>.Instance)data.get("damage_condition") : dmg -> true);
                            if(data.isPresent("self_action")) {
                                power.setSelfAction((ActionFactory<LivingEntity>.Instance)data.get("self_action"));
                            }
                            if(data.isPresent("attacker_action")) {
                                power.setAttackerAction((ActionFactory<LivingEntity>.Instance)data.get("attacker_action"));
                            }

                            return power;
                        })
                .allowCondition());
        register(new PowerFactory<>(Declarar.getIdentifier("smithing_armor_power"),
                new SerializableData().add("scaling",SerializableDataTypes.INT,0),
                data -> (type,player) -> new SmithingArmorPower(type,player,data.getInt("scaling"))).allowCondition());
        register(new PowerFactory<>(Declarar.getIdentifier("smithing_weapon_power"),
                new SerializableData().add("scaling",SerializableDataTypes.INT,0),
                data -> (type,player) -> new SmithingWeaponPower(type,player,data.getInt("scaling"))).allowCondition());
        register(new PowerFactory<>(Declarar.getIdentifier("warsmith_shield_buff"),
                new SerializableData(),
                data -> (BiFunction<PowerType<Power>, LivingEntity, Power>) WarsmithShieldBuffPower::new));
        register(new PowerFactory<>(Declarar.getIdentifier("warsmith_armor_buff"),
                new SerializableData(),
                data -> (BiFunction<PowerType<Power>, LivingEntity, Power>) WarsmithShieldBuffPower::new));
        register(new PowerFactory<>(Declarar.getIdentifier("warsmith_sword_buff"),
                new SerializableData(),
                data -> (BiFunction<PowerType<Power>, LivingEntity, Power>) WarsmithSwordBuffPower::new));
        register(new PowerFactory<>(Declarar.getIdentifier("arcanesmith_always_enchant"),
                new SerializableData(),
                data -> (BiFunction<PowerType<Power>, LivingEntity, Power>) ArcanesmithAlwaysEnchanted::new));
        register(new PowerFactory<>(Declarar.getIdentifier("arcanesmith_chest_buff"),
                new SerializableData(),
                data -> (BiFunction<PowerType<Power>, LivingEntity, Power>) ArcanesmithChestBuffPower::new));
        register(new PowerFactory<>(Declarar.getIdentifier("arcanesmith_boot_buff"),
                new SerializableData(),
                data -> (BiFunction<PowerType<Power>, LivingEntity, Power>) ArcanesmithBootBuffPower::new));
        register(new PowerFactory<>(Declarar.getIdentifier("arcanesmith_cheap_enchanting"),
                new SerializableData(),
                data -> (BiFunction<PowerType<Power>, LivingEntity, Power>) ArcanesmithCheapEnchantingPower::new));
        register(new PowerFactory<>(Declarar.getIdentifier("arcanesmith_sword_buff"),
                new SerializableData(),
                data -> (BiFunction<PowerType<Power>, LivingEntity, Power>) ArcanemithSwordBuffPower::new));
        register(new PowerFactory<>(Declarar.getIdentifier("arcanesmith_double_enchant"),
                new SerializableData(),
                data -> (BiFunction<PowerType<Power>, LivingEntity, Power>) ArcanesmithDoubleEnchantPower::new));
        register(new PowerFactory<>(Declarar.getIdentifier("arcanesmith_free_lapis"),
                new SerializableData(),
                data -> (BiFunction<PowerType<Power>, LivingEntity, Power>) ArcanesmithFreeLapisPower::new));

        register(new PowerFactory<>(Declarar.getIdentifier("ranger_accuracy"),
                new SerializableData()
                        .add("accuracy",SerializableDataTypes.FLOAT,0F)
                        .add("bow",SerializableDataTypes.BOOLEAN,false)
                        .add("crossbow",SerializableDataTypes.BOOLEAN,false)
                        .add("scale",SerializableDataTypes.INT,0)
                ,
                data -> (type,player) -> new RangerRangedItemAccuracyPower(type,player,
                        data.getFloat("accuracy"),
                        data.getBoolean("bow"),
                        data.getBoolean("crossbow"),
                        data.getInt("scale")
                )));
        register(new PowerFactory<>(Declarar.getIdentifier("ranger_damage_increase"),
                new SerializableData()
                        .add("damage",SerializableDataTypes.FLOAT,0F)
                        .add("damage_scaled",SerializableDataTypes.FLOAT,0F)
                        .add("scale",SerializableDataTypes.INT,0)
                ,
                data -> (type,player) -> new RangerDamagePower(type,player,
                        data.getFloat("damage"),
                        data.getFloat("damage_scaled"),
                        data.getInt("scale")
                )));

        register(new PowerFactory<>(Declarar.getIdentifier("druid_dire_wolf"),
                new SerializableData(),
                data -> (BiFunction<PowerType<Power>, LivingEntity, Power>) DruidDireWolfPower::new));
        register(new PowerFactory<>(Declarar.getIdentifier("druid_pack_wolf"),
                new SerializableData(),
                data -> (BiFunction<PowerType<Power>, LivingEntity, Power>) DruidPackWolfPower::new));
        register(new PowerFactory<>(Declarar.getIdentifier("druid_bond_wolf"),
                new SerializableData(),
                data -> (BiFunction<PowerType<Power>, LivingEntity, Power>) DruidWolfBondPower::new));

        register(new PowerFactory<>(Declarar.getIdentifier("marksman_quiver_manager"),
                new SerializableData()
                        .add("quivers", ApoDataTypes.QUIVERS)
                        .add("key", ApoliDataTypes.BACKWARDS_COMPATIBLE_KEY, new Active.Key())
                ,
                data -> ((type, player) ->{
                    List<QuiverData> quiverData = (List<QuiverData>) data.get("quivers");
                    StatusEffectInstance[][] instances = new StatusEffectInstance[quiverData.size()][];
                    boolean[] doDamage = new boolean[quiverData.size()];

                    for (int i = 0; i < quiverData.size(); i++) {
                        doDamage[i] = quiverData.get(i).isDoDamage();
                        List<StatusEffectInstance> effects = quiverData.get(i).getEffects();
                        instances[i] = new StatusEffectInstance[effects.size()];
                        for (int j = 0; j < effects.size(); j++) {
                            instances[i][j] = effects.get(j);
                        }
                    }
                    MarksmanArrowCyclingPower power = new MarksmanArrowCyclingPower(type, player,instances,doDamage);
                    power.setKey((Active.Key)data.get("key"));
                    return power;
                }
                )));
        register(new PowerFactory<>(Declarar.getIdentifier("marksman_reloading"),
                new SerializableData(),
                data -> (BiFunction<PowerType<Power>, LivingEntity, Power>) MarksmanReloadingPower::new));
        register(new PowerFactory<>(Declarar.getIdentifier("marksman_big_game"),
                new SerializableData(),
                data -> (BiFunction<PowerType<Power>, LivingEntity, Power>) MarksmanBigGamePower::new));
        register(new PowerFactory<>(Declarar.getIdentifier("marksman_ultimate"),
                new SerializableData(),
                data -> (BiFunction<PowerType<Power>, LivingEntity, Power>) MarksmanUltimatePower::new));

        register(new PowerFactory<>(Declarar.getIdentifier("draco_shield"),
                new SerializableData()
                        .add("entity_action", ApoliDataTypes.ENTITY_ACTION)
                        .add("cooldown", SerializableDataTypes.INT)
                        .add("hud_render", ApoliDataTypes.HUD_RENDER)
                        .add("key", ApoliDataTypes.BACKWARDS_COMPATIBLE_KEY, new Active.Key()),
                data ->
                        (type, player) -> {
                            ActiveCooldownPower power = new DracoKnightShieldPower(type, player, data.getInt("cooldown"), (HudRender)data.get("hud_render"),
                                    (ActionFactory<Entity>.Instance)data.get("entity_action"));
                            power.setKey((Active.Key)data.get("key"));
                            return power;
                        })
                .allowCondition());

        register(new PowerFactory<>(Declarar.getIdentifier("aoe_power"),
                new SerializableData()
                        .add("effects", SerializableDataTypes.STATUS_EFFECT_INSTANCES, null)
                        .add("distance", SerializableDataTypes.INT,5)
                        .add("tickrate", SerializableDataTypes.INT,20)
                ,
                data ->
                        (type, player) -> {
                            return new AoEPower(type,player, data.getInt("tickrate"), data.getInt("distance"),(List<StatusEffectInstance>)data.get("effects"));
                        })
                .allowCondition());
        register(new PowerFactory<>(Declarar.getIdentifier("wight_blight"),
                new SerializableData(),
                data -> (BiFunction<PowerType<Power>, LivingEntity, Power>) WightBlightPower::new));
        register(new PowerFactory<>(Declarar.getIdentifier("wight_bone"),
                new SerializableData(),
                data -> (BiFunction<PowerType<Power>, LivingEntity, Power>) WightBonePower::new));

    }

    private static void register(PowerFactory serializer) {
        Registry.register(ApoliRegistries.POWER_FACTORY, serializer.getSerializerId(), serializer);
    }
}
