package com.troblecodings.tcutility.init;

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

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

/**
 * 1.19.2: {@code Item.<init>} ruft (wie Block.<init>) intern
 * {@code createIntrusiveHolder} und scheitert ausserhalb des ITEMS-
 * {@link RegisterEvent}-Frames an einer eingefrorenen Registry. Wir
 * sammeln daher in {@link #initJsonFiles} nur Spezifikationen
 * (Armor-Material/Slot bzw. plain-Item-Name) und konstruieren die
 * Items erst im {@link #onRegister}-Handler.
 */
public final class TCItems {

    private TCItems() {
    }

    private static final class ArmorSpec {
        final String registryName;
        final ArmorMaterial material;
        final EquipmentSlot slot;

        ArmorSpec(final String registryName, final ArmorMaterial material,
                final EquipmentSlot slot) {
            this.registryName = registryName;
            this.material = material;
            this.slot = slot;
        }
    }

    private static final List<ArmorSpec> armorSpecs = new ArrayList<>();
    private static final List<String> itemNames = new ArrayList<>();

    public static void init() {
        // Reflection-Pfad fuer manuell deklarierte Items ist mit dem Defer-
        // Modell nicht vereinbar -- Items duerfen erst im RegisterEvent
        // konstruiert werden. No-op, bleibt fuer kuenftige Erweiterungen.
    }

    public static void initJsonFiles() {
        final Map<String, ArmorProperties> armor = getArmorFromJson("armordefinitions");
        for (final Entry<String, ArmorProperties> armorEntry : armor.entrySet()) {
            final String armorName = armorEntry.getKey();
            final ArmorProperties property = armorEntry.getValue();
            final ArmorCreateInfo armorInfo = property.getArmorInfo();
            final ArmorMaterial material = makeArmorMaterial(armorName, armorInfo);
            final List<String> slots = property.getSlots();
            for (final String slot : slots) {
                final ArmorTypes type = Enum.valueOf(ArmorTypes.class, slot.toUpperCase());
                final String registryName = type.getRegistryName(armorName);
                armorSpecs.add(new ArmorSpec(registryName, material, mapSlot(type)));
            }
        }

        final Map<String, ItemProperties> items = getItemFromJson("itemdefinitions");
        for (final Entry<String, ItemProperties> itemEntry : items.entrySet()) {
            itemNames.add(itemEntry.getKey());
        }
    }

    @SubscribeEvent
    public static void onRegister(final RegisterEvent event) {
        if (!event.getRegistryKey().equals(ForgeRegistries.Keys.ITEMS)) {
            return;
        }
        event.register(ForgeRegistries.Keys.ITEMS, helper -> {
            for (final ArmorSpec spec : armorSpecs) {
                final ArmorItem armorItem = new ArmorItem(spec.material, spec.slot,
                        new Item.Properties().tab(CreativeModeTab.TAB_COMBAT));
                helper.register(new ResourceLocation(TCUtilityMain.MODID, spec.registryName),
                        armorItem);
            }
            for (final String itemName : itemNames) {
                final Item item = new Item(new Item.Properties().tab(TCTabs.ITEMS));
                helper.register(new ResourceLocation(TCUtilityMain.MODID, itemName), item);
            }
        });
    }

    private static EquipmentSlot mapSlot(final ArmorTypes type) {
        switch (type) {
            case HEAD:
                return EquipmentSlot.HEAD;
            case CHEST:
                return EquipmentSlot.CHEST;
            case LEGS:
                return EquipmentSlot.LEGS;
            case FEET:
                return EquipmentSlot.FEET;
            default:
                throw new IllegalStateException("Unknown armor slot " + type);
        }
    }

    /**
     * 1.19.2 hat keinen EnumHelper.addArmorMaterial; stattdessen wird ein
     * {@link ArmorMaterial} pro Material instanziert. ArmorMaterial ist ein
     * pures Interface und nutzt selbst keine Registry-Holder, daher ist die
     * Konstruktion bereits im Mod-Konstruktor unbedenklich.
     */
    private static ArmorMaterial makeArmorMaterial(final String name,
            final ArmorCreateInfo info) {
        final String texturePrefix = TCUtilityMain.MODID + ":" + name;
        return new ArmorMaterial() {
            @Override
            public int getDurabilityForSlot(final EquipmentSlot slot) {
                return info.durability;
            }

            @Override
            public int getDefenseForSlot(final EquipmentSlot slot) {
                return 1;
            }

            @Override
            public int getEnchantmentValue() {
                return info.enchantability;
            }

            @Override
            public SoundEvent getEquipSound() {
                return SoundEvents.ARMOR_EQUIP_GENERIC;
            }

            @Override
            public Ingredient getRepairIngredient() {
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

            @Override
            public float getKnockbackResistance() {
                return 0.0F;
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
