package website.skylorbeck.minecraft.apotheosis.conditions;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerTypeRegistry;
import io.github.apace100.apoli.power.TogglePower;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import website.skylorbeck.minecraft.apotheosis.Declarar;
import website.skylorbeck.minecraft.apotheosis.LivingEntityInterface;

import static website.skylorbeck.minecraft.apotheosis.cardinal.ApotheosisComponents.APOXP;

public class ApoEntityActions {
    public static void register() {
        register(new ActionFactory<>(Declarar.getIdentifier("mending"), new SerializableData(),
                (data, entity) -> {
                    if (entity instanceof PlayerEntity) {
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
                    if (entity instanceof PlayerEntity) {
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
                    if (entity instanceof PlayerEntity) {
                        ItemStack book = EnchantmentHelper.enchant(MinecraftClient.getInstance().world.random, new ItemStack(Items.BOOK), MinecraftClient.getInstance().world.random.nextInt(5), true);
                        book.setCustomName(Text.of("Tome Of Knowledge"));
                        ((PlayerEntity) entity).giveItemStack(book);
                    }
                }));
        register(new ActionFactory<>(Declarar.getIdentifier("summon_pet"), new SerializableData()
//                .add("living_entity", SerializableDataTypes.ENTITY_TYPE, EntityType.WOLF)
                ,
                (data, entity) -> {
//                   LivingEntity pet = (LivingEntity) ((EntityType<?>)data.get("living_entity")).create(entity.world);
                    WolfEntity pet = EntityType.WOLF.create(entity.world);
                    pet.setCustomName(Text.of(entity.getName().getString() + "'s Pet  Lv:" + APOXP.get(entity).getLevel()));
                    BlockPos blockPos = new BlockPos(entity.raycast(1, 1f, true).getPos());
                    pet.setPos(blockPos.getX(), blockPos.getY() + 1, blockPos.getZ());
                    ((LivingEntityInterface) pet).setTimeRemaining(200);
                    pet.setTamed(true);
                    pet.setOwner((PlayerEntity) entity);
                    pet.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(4.0D + (Math.floorDiv(APOXP.get(entity).getLevel(), 10) * 0.5D));
                    if (!entity.world.isClient) {
                        entity.world.spawnEntity(pet);
                        ((PlayerEntity) entity).sendMessage(Text.of("Pet Summoned"), true);
                        entity.world.playSound(null, pet.getBlockPos(), SoundEvents.BLOCK_NOTE_BLOCK_BELL, SoundCategory.PLAYERS, 1.0F, entity.world.random.nextFloat() * 0.1F + 0.9F);
                    }
                }));

        register(new ActionFactory<>(Declarar.getIdentifier("turn_power_off"), new SerializableData()
                .add("power", SerializableDataTypes.IDENTIFIER, null),
                (data, entity) -> {
                    if (entity instanceof PlayerEntity) {
                        Identifier identifier = data.getId("power");
                        if (PowerTypeRegistry.contains(identifier)) {
                            Power power = PowerTypeRegistry.get(identifier).get(entity);
                            if (PowerHolderComponent.hasPower(entity, power.getClass())) {
                                if (power.isActive()) {
                                    ((TogglePower) power).onUse();
                                    PowerHolderComponent.sync(entity);
                                }
                            }
                        }
                    }
                }));
    }


    private static void register(ActionFactory<Entity> actionFactory) {
        Registry.register(ApoliRegistries.ENTITY_ACTION, actionFactory.getSerializerId(), actionFactory);
    }
}