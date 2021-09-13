package website.skylorbeck.minecraft.apotheosis.conditions;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.CooldownPower;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerTypeRegistry;
import io.github.apace100.apoli.power.TogglePower;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import website.skylorbeck.minecraft.apotheosis.data.ApoDataTypes;
import website.skylorbeck.minecraft.apotheosis.Declarar;
import website.skylorbeck.minecraft.apotheosis.PlayerEntityInterface;
import website.skylorbeck.minecraft.apotheosis.powers.DruidDireWolfPower;
import website.skylorbeck.minecraft.apotheosis.powers.DruidPackWolfPower;
import website.skylorbeck.minecraft.apotheosis.powers.DruidWolfBondPower;

import java.lang.reflect.Field;
import java.util.List;

import static website.skylorbeck.minecraft.apotheosis.cardinal.ApotheosisComponents.APOXP;
import static website.skylorbeck.minecraft.apotheosis.cardinal.ApotheosisComponents.PETKEY;

public class ApoEntityActions {
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
//                .add("living_entity", SerializableDataTypes.ENTITY_TYPE, EntityType.WOLF)
                ,
                (data, entity) -> {
                    boolean toggle = false;
                    if (APOXP.get(entity).getPetUUID() != null) {
                        try {
                            TargetPredicate predicate = TargetPredicate.DEFAULT;
                            predicate.setPredicate((pet -> PETKEY.maybeGet(pet).isPresent() && PETKEY.get(pet).getOwnerUUID() == entity.getUuid()));
                            WolfEntity oldPet = entity.world.getClosestEntity(WolfEntity.class, predicate, (LivingEntity) entity, entity.getX(), entity.getY(), entity.getZ(), entity.getBoundingBox().expand(100D));
                            if (oldPet != null) {
                                if (PETKEY.get(oldPet).getTimeLeft() == -1) {
                                    toggle = true;
                                }
                                oldPet.discard();
                            }
                            APOXP.get(entity).setPetUUID(null);
                            APOXP.sync(entity);
                        } catch (Exception ignored){}
                    }
                    if (!toggle) {
//                   LivingEntity pet = (LivingEntity) ((EntityType<?>)data.get("living_entity")).create(entity.world);
                        WolfEntity pet = EntityType.WOLF.create(entity.world);
                        pet.setCustomName(Text.of(entity.getName().getString() + "'s Pet  Lv:" + APOXP.get(entity).getLevel()));
                        BlockPos blockPos = new BlockPos(entity.raycast(1, 1f, true).getPos());
                        pet.setPos(blockPos.getX(), blockPos.getY() + 1, blockPos.getZ());
                        boolean dire = (PowerHolderComponent.hasPower(entity, DruidDireWolfPower.class));
                        boolean pack = (PowerHolderComponent.hasPower(entity, DruidPackWolfPower.class));
                        boolean bond = (PowerHolderComponent.hasPower(entity, DruidWolfBondPower.class));
//                    ((LivingEntityInterface) pet).setTimeRemaining(data.getInt("time") + (dire ? 100 : 0) + (pack ? 100 : 0));
                        pet.setTamed(true);
                        pet.setOwner((PlayerEntity) entity);
                        PETKEY.get(pet).setOwnerUUID(entity.getUuid());
                        PETKEY.get(pet).setTimeLeft(data.getInt("time") + (dire ? 100 : 0) + (pack ? 100 : 0) + (bond ? 100 : 0));
                        if (APOXP.get(entity).getLevel() >= 50) {
                            PETKEY.get(pet).setTimeLeft(3600);
                        }
                        PETKEY.sync(pet);
                        APOXP.get(entity).setPetUUID(pet.getUuid());
                        APOXP.sync(entity);
                        if (pack) {
                            PowerHolderComponent.KEY.get(pet).addPower(PowerTypeRegistry.get(Declarar.getIdentifier("ranger/druid/wolf_mark")), Declarar.getIdentifier("wolfmark"));
                            pet.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(pet.getAttributeBaseValue(EntityAttributes.GENERIC_MOVEMENT_SPEED) + (pet.getAttributeBaseValue(EntityAttributes.GENERIC_MOVEMENT_SPEED) / 10));
                        }
                        if (bond) {
                            PowerHolderComponent.KEY.get(pet).addPower(PowerTypeRegistry.get(Declarar.getIdentifier("ranger/druid/wolf_hemorrhage")), Declarar.getIdentifier("wolf_hemorrhage"));
                        }
                        pet.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(data.getDouble("base_health") + (dire ? 5D : 0D) + (pack ? 5D : 0D) + (bond ? 10D : 0D));
                        pet.setHealth((float) data.getDouble("base_health"));
                        pet.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(data.getDouble("base_damage") + (dire ? 2D : 0D) + (pack ? 2D : 0D) + (Math.floorDiv(APOXP.get(entity).getLevel(), data.getInt("scale")) * data.getDouble("scaled_damage")));
                        if (!entity.world.isClient) {
                            entity.world.spawnEntity(pet);
                            ((PlayerEntity) entity).sendMessage(Text.of("Pet Summoned"), true);
                            entity.world.playSound(null, pet.getBlockPos(), SoundEvents.BLOCK_NOTE_BLOCK_BELL, SoundCategory.PLAYERS, 1.0F, entity.world.random.nextFloat() * 0.1F + 0.9F);
                        }
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
                        ((PlayerEntityInterface) entity).setSpyGlassOveride(false);//todo fix this staying zoomed in
                }));

        register(new ActionFactory<>(Declarar.getIdentifier("arrow_spawn"), new SerializableData()
                .add("potion", SerializableDataTypes.STRING, null)
                .add("potions", ApoDataTypes.STRINGS, null)
                .add("min",SerializableDataTypes.INT,1)
                .add("max",SerializableDataTypes.INT,5)
                ,
                (data, entity) -> {
                    ItemStack crossbow = ((PlayerEntity) entity).getMainHandStack();
                    if (!entity.world.isClient)
                    if (crossbow.isOf(Items.CROSSBOW)) {
                        if (data.isPresent("potion")) {
                            ItemStack arrow = new ItemStack(Items.TIPPED_ARROW);
                            PotionUtil.setPotion(arrow, Potion.byId(data.getString("potion")));
                            int amount = entity.world.random.nextInt(data.getInt("max")+1-data.getInt("min"))+(data.getInt("min"));
                            arrow.setCount(amount);
                            ((PlayerEntity) entity).giveItemStack(arrow);
                        } else
                        if (data.isPresent("potions")) {
                            List<String> strings = (List<String>) data.get("potions");
                            ItemStack arrow = new ItemStack(Items.TIPPED_ARROW);
                            PotionUtil.setPotion(arrow, Potion.byId(strings.get(entity.world.random.nextInt(strings.size()))));
                            int amount = entity.world.random.nextInt(data.getInt("max")+1-data.getInt("min"))+(data.getInt("min"));
                            arrow.setCount(amount);
                            ((PlayerEntity) entity).giveItemStack(arrow);
                        }
                    }
                }));
    }


    private static void register(ActionFactory<Entity> actionFactory) {
        Registry.register(ApoliRegistries.ENTITY_ACTION, actionFactory.getSerializerId(), actionFactory);
    }
}