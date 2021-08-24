package website.skylorbeck.minecraft.apotheosis.powers;

import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.ModifyDamageTakenPower;
import io.github.apace100.apoli.power.SwimmingPower;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.apoli.util.AttributedEntityAttributeModifier;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;
import website.skylorbeck.minecraft.apotheosis.Declarar;

import java.util.List;

public class PowerFactories {
    public static void register(){
        register(new PowerFactory<>(Declarar.getIdentifier("branching"),
                new SerializableData().add("level",SerializableDataTypes.INT,0),
                data -> (type,player) -> new BranchingClassPower(type,player,data.getInt("level"))));

        register(new PowerFactory<>(Declarar.getIdentifier("leveled_attribute"),
                new SerializableData()
                        .add("modifier", ApoliDataTypes.ATTRIBUTED_ATTRIBUTE_MODIFIER, null)
                        .add("modifiers", ApoliDataTypes.ATTRIBUTED_ATTRIBUTE_MODIFIERS, null)
                        .add("tick_rate", SerializableDataTypes.INT, 20),
                data ->
                        (type, player) -> {
                            LeveledAttributePower ap = new LeveledAttributePower(type, player, data.getInt("tick_rate"));
                            if(data.isPresent("modifier")) {
                                ap.addModifier((AttributedEntityAttributeModifier)data.get("modifier"));
                            }
                            if(data.isPresent("modifiers")) {
                                List<AttributedEntityAttributeModifier> modifierList = (List<AttributedEntityAttributeModifier>)data.get("modifiers");
                                modifierList.forEach(ap::addModifier);
                            }
                            return ap;
                        }).allowCondition());
        register(new PowerFactory<>(Declarar.getIdentifier("scaling_modify_damage_taken"),
                new SerializableData()
                        .add("damage_condition", ApoliDataTypes.DAMAGE_CONDITION, null)
                        .add("modifier", SerializableDataTypes.ATTRIBUTE_MODIFIER, null)
                        .add("self_action", ApoliDataTypes.ENTITY_ACTION, null)
                        .add("attacker_action", ApoliDataTypes.ENTITY_ACTION, null)
                        .add("scaling",SerializableDataTypes.INT,0),
                data ->
                        (type, player) -> {
                            ScalingModifyDamageTakenPower power = new ScalingModifyDamageTakenPower(
                                    data.getInt("scaling"),
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
    }

    private static void register(PowerFactory serializer) {
        Registry.register(ApoliRegistries.POWER_FACTORY, serializer.getSerializerId(), serializer);
    }
}
