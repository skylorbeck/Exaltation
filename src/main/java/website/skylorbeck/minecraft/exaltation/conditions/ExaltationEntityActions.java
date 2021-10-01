package website.skylorbeck.minecraft.exaltation.conditions;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.*;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import website.skylorbeck.minecraft.exaltation.AIGoals.ExaltationAttackWithOwnerGoal;
import website.skylorbeck.minecraft.exaltation.AIGoals.ExaltationFollowOwnerGoal;
import website.skylorbeck.minecraft.exaltation.AIGoals.ExaltationTrackOwnerAttackerGoal;
import website.skylorbeck.minecraft.exaltation.Declarar;
import website.skylorbeck.minecraft.exaltation.PlayerEntityInterface;
import website.skylorbeck.minecraft.exaltation.cardinal.PetComponent;
import website.skylorbeck.minecraft.exaltation.data.ExaltationDataTypes;
import website.skylorbeck.minecraft.exaltation.mixin.MobEntityAccessor;
import website.skylorbeck.minecraft.exaltation.powers.*;

import java.util.List;
import java.util.UUID;

import static website.skylorbeck.minecraft.exaltation.cardinal.ExaltationComponents.EXALXP;
import static website.skylorbeck.minecraft.exaltation.cardinal.ExaltationComponents.PETKEY;

public class ExaltationEntityActions {
    public static void register() {
        register(new ActionFactory<>(Declarar.getIdentifier("mending"), new SerializableData(),
                (data, entity) -> {
                    if (entity.isPlayer()) {
                        PlayerInventory inventory = ((PlayerEntity) entity).getInventory();
                        for (int i = 0; i < inventory.size(); i++) {
                            ItemStack stack = inventory.getStack(i);
                            if (stack.getDamage() > 0) {
                                stack.setDamage(stack.getDamage() - 1);
                            }
                        }
                    }
                }));
        register(new ActionFactory<>(Declarar.getIdentifier("ultimate_mending"), new SerializableData(),
                (data, entity) -> {
                    if (entity.isPlayer()) {
                        PlayerInventory inventory = ((PlayerEntity) entity).getInventory();
                        for (int i = 0; i < inventory.size(); i++) {
                            ItemStack stack = inventory.getStack(i);
                            if (stack.getDamage() > 0) {
                                stack.setDamage(stack.getDamage() - stack.getMaxDamage() / 10);
                            }
                            if (stack.getDamage() < 0) {
                                stack.setDamage(0);
                            }
                        }
                    }
                }));
        register(new ActionFactory<>(Declarar.getIdentifier("tome_of_knowledge"), new SerializableData(),
                (data, entity) -> {
                    if (entity.isPlayer()) {
                        ItemStack book = EnchantmentHelper.enchant(MinecraftClient.getInstance().world.random, new ItemStack(Items.BOOK), MinecraftClient.getInstance().world.random.nextInt(5), true);
                        book.setCustomName(Text.of("Tome Of Knowledge"));
                        ((PlayerEntity) entity).giveItemStack(book);
                    }
                }));
        register(new ActionFactory<>(Declarar.getIdentifier("summon_pet"), new SerializableData()
                .add("scale", SerializableDataTypes.INT, 10)
                .add("scaled_damage", SerializableDataTypes.DOUBLE, 0.5D)
                .add("base_damage", SerializableDataTypes.DOUBLE, 4.0D)
                .add("base_health", SerializableDataTypes.DOUBLE, 20.0D)
                .add("time", SerializableDataTypes.INT, 600)
                .add("entity_type", SerializableDataTypes.ENTITY_TYPE, EntityType.WOLF)
                .add("amount", SerializableDataTypes.INT, 1)
                ,
                (data, entity) -> {
                    boolean toggle = false;
                    if (EXALXP.get(entity).getPetUUID() != null) {
                        UUID[] pets = EXALXP.get(entity).getPetUUID();
                        TargetPredicate predicate = TargetPredicate.DEFAULT;
                        predicate.setPredicate((pet -> {
                            if (pet.getType().equals(data.get("entity_type"))) {
                                for (UUID uuid : pets) {
                                    if (pet.getUuid().equals(uuid)) {
                                        return true;
                                    }
                                }
                            }
                            return false;
                        }));
                        for (int i = 0; i < pets.length; i++) {
                            try {
                                LivingEntity oldPet = entity.world.getClosestEntity(MobEntity.class, predicate, (LivingEntity) entity, entity.getX(), entity.getY(), entity.getZ(), entity.getBoundingBox().expand(100D));
                                if (oldPet != null) {
                                    if (PETKEY.get(oldPet).getTimeLeft() == -1) {
                                        toggle = true;
                                    }
                                    oldPet.discard();
                                }
                            } catch (Exception ignored) {
                            }
                        }
                        EXALXP.get(entity).setPetUUID(null);
                        EXALXP.sync(entity);
                    }
                    if (!toggle) {
                        UUID[] UUIDArray = new UUID[data.getInt("amount")];
                        for (int i = 0; i < data.getInt("amount"); i++) {
                            MobEntity pet = (MobEntity) ((EntityType<?>) data.get("entity_type")).create(entity.world);
                            assert pet != null;
                            pet.setCustomName(Text.of(entity.getName().getString() + "'s Pet  Lv:" + EXALXP.get(entity).getLevel()));
                            BlockHitResult hitResult = (BlockHitResult) entity.raycast(1, 1f, true);
                            BlockPos blockPos = new BlockPos(hitResult.getPos());
                            switch (hitResult.getSide()) {
                                case NORTH, SOUTH, UP -> {
                                    pet.setPos(i % 2 == 0 ? blockPos.getX() + i : blockPos.getX() - i, blockPos.getY() + 1, blockPos.getZ());
                                }
                                case WEST, EAST, DOWN -> {
                                    pet.setPos(blockPos.getX(), blockPos.getY() + 1, i % 2 == 0 ? blockPos.getZ() + i : blockPos.getZ() - i);
                                }
                            }
                            boolean dire = (PowerHolderComponent.hasPower(entity, DruidDireWolfPower.class));
                            boolean pack = (PowerHolderComponent.hasPower(entity, DruidPackWolfPower.class));
                            boolean bond = (PowerHolderComponent.hasPower(entity, DruidWolfBondPower.class));
                            boolean blight = (PowerHolderComponent.hasPower(entity, WightBlightPower.class));
                            boolean bone = (PowerHolderComponent.hasPower(entity, WightBonePower.class));
                            boolean hell_a = (PowerHolderComponent.hasPower(entity, WightHellAPower.class));
                            boolean hell_b = (PowerHolderComponent.hasPower(entity, WightHellBPower.class));
                            boolean petCharge = (PowerHolderComponent.hasPower(entity, WightPetChargePower.class));
                            petCharge &= EXALXP.get(entity).getLevel() >= 50;
                            GoalSelector targetSelector = ((MobEntityAccessor) pet).getTargetSelector();
                            targetSelector.clear();
                            targetSelector.add(1, new ExaltationTrackOwnerAttackerGoal((LivingEntity) entity, pet));
                            targetSelector.add(2, new ExaltationAttackWithOwnerGoal((LivingEntity) entity, pet));
                            targetSelector.add(3, (new RevengeGoal((PathAwareEntity) pet, PlayerEntity.class)));
                            GoalSelector goalSelector = ((MobEntityAccessor) pet).getGoalSelector();
                            goalSelector.add(1, new ExaltationFollowOwnerGoal(pet, (LivingEntity) entity, 2.0D, 10.0F, 2.0F, false));
                            PetComponent petComponent = PETKEY.get(pet);
                            petComponent.setOwnerUUID(entity.getUuid());
                            petComponent.setHealOwner(blight);
                            petComponent.setTimeLeft(data.getInt("time") + (dire ? 100 : 0) + (pack ? 100 : 0) + (bond ? 100 : 0) + (blight ? 100 : 0));
                            if (EXALXP.get(entity).getLevel() >= 50) {
                                petComponent.setTimeLeft(3600);
                            }
                            PETKEY.sync(pet);
                            UUIDArray[i] = pet.getUuid();
                            if (pack) {
                                PowerHolderComponent.KEY.get(pet).addPower(PowerTypeRegistry.get(Declarar.getIdentifier("ranger/druid/wolf_mark")), Declarar.getIdentifier("wolfmark"));
                                pet.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(pet.getAttributeBaseValue(EntityAttributes.GENERIC_MOVEMENT_SPEED) + (pet.getAttributeBaseValue(EntityAttributes.GENERIC_MOVEMENT_SPEED) / 10));
                            }
                            if (bond) {
                                PowerHolderComponent.KEY.get(pet).addPower(PowerTypeRegistry.get(Declarar.getIdentifier("ranger/druid/wolf_hemorrhage")), Declarar.getIdentifier("wolf_hemorrhage"));
                            }
                            if (bone) {
                                PowerHolderComponent.KEY.get(pet).addPower(PowerTypeRegistry.get(Declarar.getIdentifier("knight/wight/wight_bone_pet")), Declarar.getIdentifier("wight_bone_pet"));
                            }
                            if (hell_a) {
                                pet.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(pet.getAttributeBaseValue(EntityAttributes.GENERIC_MOVEMENT_SPEED) + (pet.getAttributeBaseValue(EntityAttributes.GENERIC_MOVEMENT_SPEED) / 10));
                                ItemStack bow = new ItemStack(Items.BOW);
                                bow.addEnchantment(Enchantments.FLAME, 1);
                                pet.equipStack(EquipmentSlot.MAINHAND, bow);
                                pet.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.CHAINMAIL_HELMET));
                                pet.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
                                pet.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.CHAINMAIL_LEGGINGS));
                                pet.equipStack(EquipmentSlot.FEET, new ItemStack(Items.CHAINMAIL_BOOTS));
                            }
                            if (hell_b) {
                                pet.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(pet.getAttributeBaseValue(EntityAttributes.GENERIC_MOVEMENT_SPEED) - ((pet.getAttributeBaseValue(EntityAttributes.GENERIC_MOVEMENT_SPEED) / 10) * 3));
                                ItemStack sword = new ItemStack(Items.STONE_SWORD);
                                sword.addEnchantment(Enchantments.FIRE_ASPECT, 1);
                                pet.equipStack(EquipmentSlot.MAINHAND, sword);
                            }
                            if (petCharge) {
                                PowerHolderComponent.KEY.get(pet).addPower(PowerTypeRegistry.get(Declarar.getIdentifier("knight/wight/wight_mana_charge_pet_else")), Declarar.getIdentifier("wight_mana_charge_pet_else"));
                                PowerHolderComponent.KEY.get(pet).addPower(PowerTypeRegistry.get(Declarar.getIdentifier("knight/wight/wight_mana_charge_pet_player")), Declarar.getIdentifier("wight_mana_charge_pet_player"));
                            }
                            pet.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(data.getDouble("base_health") + (dire ? 5D : 0D) + (pack ? 5D : 0D) + (bond ? 10D : 0D) + (blight ? 12D : 0D));
                            pet.setHealth((float) data.getDouble("base_health"));
                            pet.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(data.getDouble("base_damage") + (dire ? 2D : 0D) + (pack ? 2D : 0D) + (blight ? 4D : 0D) + (Math.floorDiv(EXALXP.get(entity).getLevel(), data.getInt("scale")) * data.getDouble("scaled_damage")));
                            if (!entity.world.isClient) {
                                entity.world.spawnEntity(pet);
                                ((PlayerEntity) entity).sendMessage(Text.of("Pet Summoned"), true);
                                entity.world.playSound(null, pet.getBlockPos(), SoundEvents.BLOCK_NOTE_BLOCK_BELL, SoundCategory.PLAYERS, 1.0F, entity.world.random.nextFloat() * 0.1F + 0.9F);
                            }
                        }
                        EXALXP.get(entity).setPetUUID(UUIDArray);
                        EXALXP.sync(entity);
                    }
                }));

        register(new ActionFactory<>(Declarar.getIdentifier("toggle_power"), new SerializableData()
                .add("power", SerializableDataTypes.IDENTIFIER, null),
                (data, entity) -> {
                    if (entity.isPlayer()) {
                        Identifier identifier = data.getId("power");
                        if (PowerTypeRegistry.contains(identifier)) {
                            Power power = PowerTypeRegistry.get(identifier).get(entity);
                            if (PowerHolderComponent.hasPower(entity, power.getClass())) {
                                ((TogglePower) power).onUse();
                                PowerHolderComponent.sync(entity);
                            }
                        }
                    }
                }));

        register(new ActionFactory<>(Declarar.getIdentifier("cooldown"), new SerializableData()
                .add("ticks", SerializableDataTypes.INT, 20),
                (data, entity) -> {
                    PowerHolderComponent.KEY.get(entity).getPowers().forEach((power -> {
                        if (power instanceof CooldownPower) {
                            ((CooldownPower) power).modify(-data.getInt("ticks"));
                        }
                    }));
                    PowerHolderComponent.sync(entity);
                }));

        register(new ActionFactory<>(Declarar.getIdentifier("zoom_toggle"), new SerializableData()
                .add("item", SerializableDataTypes.STRING, null),
                (data, entity) -> {
                    if (((PlayerEntity) entity).getMainHandStack().isOf(Registry.ITEM.get(new Identifier(data.getString("item")))))
                        ((PlayerEntityInterface) entity).setSpyGlassOveride(!((PlayerEntityInterface) entity).getSpyGlassOverride());
                    else
                        ((PlayerEntityInterface) entity).setSpyGlassOveride(false);
                }));

        register(new ActionFactory<>(Declarar.getIdentifier("arrow_spawn"), new SerializableData()
                .add("potion", SerializableDataTypes.STRING, null)
                .add("potions", ExaltationDataTypes.STRINGS, null)
                .add("min", SerializableDataTypes.INT, 1)
                .add("max", SerializableDataTypes.INT, 5)
                ,
                (data, entity) -> {
                    ItemStack crossbow = ((PlayerEntity) entity).getMainHandStack();
                    if (!entity.world.isClient)
                        if (crossbow.isOf(Items.CROSSBOW)) {
                            if (data.isPresent("potion")) {
                                ItemStack arrow = new ItemStack(Items.TIPPED_ARROW);
                                PotionUtil.setPotion(arrow, Potion.byId(data.getString("potion")));
                                int amount = entity.world.random.nextInt(data.getInt("max") + 1 - data.getInt("min")) + (data.getInt("min"));
                                arrow.setCount(amount);
                                ((PlayerEntity) entity).giveItemStack(arrow);
                            } else if (data.isPresent("potions")) {
                                List<String> strings = (List<String>) data.get("potions");
                                ItemStack arrow = new ItemStack(Items.TIPPED_ARROW);
                                PotionUtil.setPotion(arrow, Potion.byId(strings.get(entity.world.random.nextInt(strings.size()))));
                                int amount = entity.world.random.nextInt(data.getInt("max") + 1 - data.getInt("min")) + (data.getInt("min"));
                                arrow.setCount(amount);
                                ((PlayerEntity) entity).giveItemStack(arrow);
                            }
                        }
                }));

        register(new ActionFactory<>(Declarar.getIdentifier("charge"), new SerializableData()
                ,
                (data, entity) -> {
                    int d = 64;
                    Vec3d vec3d = entity.getCameraPosVec(MinecraftClient.getInstance().getTickDelta());
                    Vec3d vec3d2 = entity.getRotationVec(1.0F);
                    EntityHitResult hit = ProjectileUtil.raycast(entity, vec3d, vec3d.add(vec3d2.x * d, vec3d2.y * d, vec3d2.z * d), entity.getBoundingBox().stretch(vec3d2.multiply(d)).expand(1.0D, 1.0D, 1.0D), (entityx) -> {
                        return !entityx.isSpectator() && entityx.collides();
                    }, d);
                    if (hit != null) {
                        Entity target = hit.getEntity();
                        double x = target.getX() - entity.getX();
                        double z = target.getZ() - entity.getZ();
                        x *= 0.25;
                        z *= 0.25;
                        ((LivingEntity) entity).addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 30), entity);
                        entity.setOnGround(false);
                        entity.addVelocity(x, 1.5, z);
                        entity.velocityModified = true;
                        ((PlayerEntityInterface) entity).setDracoFallImmune(true);
                        ((LivingEntity) target).addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 30), entity);
                        target.setOnGround(false);
                        target.addVelocity(0, 1.5, 0);
                        target.velocityModified = true;
                        ((PlayerEntity) entity).attack(target);
                    } else {
                        PowerHolderComponent.getPowers(entity, ResourcePower.class).forEach((resourcePower -> {
                            if (resourcePower.getMax() > 8) resourcePower.setValue(resourcePower.getValue() + 8);
                        }));
                    }
                }));
        register(new ActionFactory<>(Declarar.getIdentifier("slam"), new SerializableData()
                ,
                (data, entity) -> {
                    LivingEntity target = entity.world.getClosestEntity(LivingEntity.class, TargetPredicate.createAttackable(), (LivingEntity) entity, entity.getX(), entity.getY(), entity.getZ(), entity.getBoundingBox().expand(5));
                    if (target != null) {
                        target.addVelocity(0, -5, 0);
                        target.removeStatusEffect(StatusEffects.SLOW_FALLING);
                    }
                    entity.addVelocity(0, -5, 0);
                    ((LivingEntity) entity).removeStatusEffect(StatusEffects.SLOW_FALLING);
                    ((PlayerEntityInterface) entity).setDracoSlam(true);
                }));
        register(new ActionFactory<>(Declarar.getIdentifier("trigger_power"), new SerializableData()
                .add("power", ApoliDataTypes.POWER_TYPE)
                ,
                (data, entity) -> {
                    if (entity instanceof PlayerEntity) {
                        PowerHolderComponent component = PowerHolderComponent.KEY.get(entity);
                        Power p = component.getPower((PowerType<?>) data.get("power"));
                        if (p instanceof ActiveCooldownPower) {
                            ActiveCooldownPower acp = (ActiveCooldownPower) p;
                            if (acp.canUse()) {
                                acp.onUse();
                                PowerHolderComponent.sync(entity);
                            }
                        }
                    }
                }));
        register(new ActionFactory<>(Declarar.getIdentifier("draco_ultimate"), new SerializableData()
                ,
                (data, entity) -> {
                    PlayerEntity player = (PlayerEntity) entity;
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 600, 3), player);
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 600, 1), player);
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 600, 1), player);
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 600, 1), player);
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 600, 0), player);
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 600, 20), player);
                }));

        register(new ActionFactory<>(Declarar.getIdentifier("gift_owner"), new SerializableData()
                .add("stack", SerializableDataTypes.ITEM_STACK),
                (data, entity) -> {
                    if (!entity.world.isClient()) {
                        ItemStack stack = (ItemStack) data.get("stack");
                        stack = stack.copy();
                        if (entity instanceof PlayerEntity) {
                            ((PlayerEntity) entity).getInventory().offerOrDrop(stack);
                        } else {
                            if (PETKEY.maybeGet(entity).isPresent()) {
                                try {
                                    entity.world.getPlayerByUuid(PETKEY.get(entity).getOwnerUUID()).getInventory().offerOrDrop(stack);
                                } catch (Exception ignored) {
                                }
                            } else {
                                entity.world.spawnEntity(new ItemEntity(entity.world, entity.getX(), entity.getY(), entity.getZ(), stack));
                            }
                        }
                    }
                }));
        register(new ActionFactory<>(Declarar.getIdentifier("change_owner_resource"), new SerializableData()
                .add("resource", ApoliDataTypes.POWER_TYPE)
                .add("change", SerializableDataTypes.INT),
                (data, entity) -> {
                    PlayerEntity e = PETKEY.maybeGet(entity).isPresent() ? PETKEY.get(entity).getOwnerUUID() != null ? entity.world.getPlayerByUuid(PETKEY.get(entity).getOwnerUUID()) : null : null;
                    if (e != null) {
                        PowerHolderComponent component = PowerHolderComponent.KEY.get(e);
                        Power p = component.getPower((PowerType<?>) data.get("resource"));
                        if (p instanceof VariableIntPower) {
                            VariableIntPower vip = (VariableIntPower) p;
                            int newValue = vip.getValue() + data.getInt("change");
                            vip.setValue(newValue);
                            PowerHolderComponent.sync(e);
                        } else if (p instanceof CooldownPower) {
                            CooldownPower cp = (CooldownPower) p;
                            cp.modify(data.getInt("change"));
                            PowerHolderComponent.sync(e);
                        }
                    }
                }));
    }


    private static void register(ActionFactory<Entity> actionFactory) {
        Registry.register(ApoliRegistries.ENTITY_ACTION, actionFactory.getSerializerId(), actionFactory);
    }
}