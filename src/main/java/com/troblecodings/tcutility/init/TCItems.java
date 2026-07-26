package com.troblecodings.tcutility.init;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.troblecodings.tcutility.TCUtilityMain;
import com.troblecodings.tcutility.enums.ArmorTypes;
import com.troblecodings.tcutility.utils.ArmorCreateInfo;
import com.troblecodings.tcutility.utils.ArmorProperties;
import com.troblecodings.tcutility.utils.ItemProperties;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = TCUtilityMain.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class TCItems {

    private TCItems() {
    }

    public static final ArrayList<Item> itemsToRegister = new ArrayList<>();

    public static void init() {
        for (final Field field : TCItems.class.getFields()) {
            final int modifiers = field.getModifiers();
            if (Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers)
                    && Modifier.isPublic(modifiers)) {
                final String name = field.getName().toLowerCase();
                try {
                    final Object value = field.get(null);
                    if (value instanceof Item) {
                        final Item item = (Item) value;
                        item.setRegistryName(new ResourceLocation(TCUtilityMain.MODID, name));
                        itemsToRegister.add(item);
                    }
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @SubscribeEvent
    public static void registerItem(final RegistryEvent.Register<Item> event) {
        final IForgeRegistry<Item> registry = event.getRegistry();
        itemsToRegister.forEach(registry::register);
    }

    public static void initJsonFiles() {
        final Map<String, ArmorProperties> armor = getArmorFromJson("armordefinitions");

        for (final Entry<String, ArmorProperties> armorEntry : armor.entrySet()) {
            final String armorName = armorEntry.getKey();
            final ArmorProperties property = armorEntry.getValue();
            final ArmorCreateInfo armorInfo = property.getArmorInfo();
            final IArmorMaterial material = makeArmorMaterial(armorName, armorInfo);
            final List<String> slots = property.getSlots();

            for (final String slot : slots) {
                final ArmorTypes type = Enum.valueOf(ArmorTypes.class, slot.toUpperCase());
                final String registryName = type.getRegistryName(armorName);
                final EquipmentSlotType equipSlot = mapSlot(type);
                final ArmorItem armorItem = new ArmorItem(material, equipSlot,
                        new Item.Properties().group(ItemGroup.COMBAT));
                armorItem.setRegistryName(
                        new ResourceLocation(TCUtilityMain.MODID, registryName));
                itemsToRegister.add(armorItem);
            }
        }

        final Map<String, ItemProperties> items = getItemFromJson("itemdefinitions");

        for (final Entry<String, ItemProperties> itemEntry : items.entrySet()) {
            final String itemName = itemEntry.getKey();
            final Item item = new Item(new Item.Properties().group(TCTabs.ITEMS));
            item.setRegistryName(new ResourceLocation(TCUtilityMain.MODID, itemName));
            itemsToRegister.add(item);
        }
    }

    public static void setName(final Item item, final String name) {
        item.setRegistryName(new ResourceLocation(TCUtilityMain.MODID, name));
        itemsToRegister.add(item);
    }

    private static EquipmentSlotType mapSlot(final ArmorTypes type) {
        switch (type) {
            case HEAD:
                return EquipmentSlotType.HEAD;
            case CHEST:
                return EquipmentSlotType.CHEST;
            case LEGS:
                return EquipmentSlotType.LEGS;
            case FEET:
                return EquipmentSlotType.FEET;
            default:
                throw new IllegalStateException("Unknown armor slot " + type);
        }
    }

    /**
     * 1.14.4 hat keinen EnumHelper.addArmorMaterial; stattdessen wird ein
     * {@link IArmorMaterial} pro Material instanziert.
     */
    private static IArmorMaterial makeArmorMaterial(final String name,
            final ArmorCreateInfo info) {
        final String texturePrefix = TCUtilityMain.MODID + ":" + name;
        return new IArmorMaterial() {
            @Override
            public int getDurability(final EquipmentSlotType slot) {
                return info.durability;
            }

            @Override
            public int getDamageReductionAmount(final EquipmentSlotType slot) {
                return 1;
            }

            @Override
            public int getEnchantability() {
                return info.enchantability;
            }

            @Override
            public SoundEvent getSoundEvent() {
                return SoundEvents.ITEM_ARMOR_EQUIP_GENERIC;
            }

            @Override
            public Ingredient getRepairMaterial() {
                return Ingredient.EMPTY;
            }

            @Override
            public String getName() {
                return texturePrefix;
            }

            @Override
            public float getToughness() {
                return info.toughness;
            }
        };
    }

    private static Map<String, ArmorProperties> getArmorFromJson(final String directory) {
        return parseJson(directory, new TypeToken<Map<String, ArmorProperties>>() {
        }.getType());
    }

    private static Map<String, ItemProperties> getItemFromJson(final String directory) {
        return parseJson(directory, new TypeToken<Map<String, ItemProperties>>() {
        }.getType());
    }

    private static <V> Map<String, V> parseJson(final String directory, final Type typeOfHashMap) {
        final Gson gson = new Gson();
        final List<Entry<String, String>> entrySet = TCUtilityMain.fileHandler.getFiles(directory);
        final Map<String, V> properties = new HashMap<>();
        if (entrySet != null) {
            entrySet.forEach(entry -> {
                final Map<String, V> json = gson.fromJson(entry.getValue(), typeOfHashMap);
                if (json != null) {
                    properties.putAll(json);
                }
            });
        }
        return properties;
    }
}
