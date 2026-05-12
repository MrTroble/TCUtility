package com.troblecodings.tcutility.init;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.EnumMap;
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

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

/**
 * Item-Registrierung. 1.21.4 hat das Equipment-System ueberarbeitet:
 * {@link ArmorMaterial} wandert von {@code net.minecraft.world.item} nach
 * {@code net.minecraft.world.item.equipment}, ist weiterhin ein Record, aber mit anderem
 * Konstruktor: {@code (durability, defenseMap, enchantmentValue, equipSound, toughness,
 * knockbackResistance, repairTag, assetId)}. Repair-Ingredient ist ein {@link
 * net.minecraft.tags.TagKey TagKey&lt;Item&gt;}, Renderdaten kommen ueber einen
 * {@link EquipmentAsset}-{@link ResourceKey}. Item.Properties uebernimmt die per-Type-
 * Durability + Attribute via {@link ArmorMaterial#humanoidProperties}.
 *
 * <p>{@code ArmorItem.Type} ist umbenannt in {@link ArmorType} (auch im equipment-Package).
 */
public final class TCItems {

    private TCItems() {
    }

    private static final class ArmorSpec {
        final String registryName;
        final ArmorMaterial material;
        final ArmorType type;

        ArmorSpec(final String registryName, final ArmorMaterial material, final ArmorType type) {
            this.registryName = registryName;
            this.material = material;
            this.type = type;
        }
    }

    private static final List<ArmorSpec> armorSpecs = new ArrayList<>();
    private static final List<String> itemNames = new ArrayList<>();

    public static void init() {
        // No-op; Items werden erst im RegisterEvent konstruiert.
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
                armorSpecs.add(new ArmorSpec(registryName, material, mapType(type)));
            }
        }

        final Map<String, ItemProperties> items = getItemFromJson("itemdefinitions");
        for (final Entry<String, ItemProperties> itemEntry : items.entrySet()) {
            itemNames.add(itemEntry.getKey());
        }
    }

    @SubscribeEvent
    public static void onRegister(final RegisterEvent event) {
        if (!event.getRegistryKey().equals(Registries.ITEM)) {
            return;
        }
        event.register(Registries.ITEM, helper -> {
            for (final ArmorSpec spec : armorSpecs) {
                // 1.21.2+: Item.Properties#setId muss vor dem Item-Ctor gesetzt sein,
                // sonst NPEt der DataComponent-Setup mit "Item id not set".
                final ResourceLocation rl = ResourceLocation.fromNamespaceAndPath(
                        TCUtilityMain.MODID, spec.registryName);
                final Item.Properties props = spec.material.humanoidProperties(
                        new Item.Properties().setId(ResourceKey.create(Registries.ITEM, rl)),
                        spec.type);
                final ArmorItem armorItem = new ArmorItem(spec.material, spec.type, props);
                helper.register(rl, armorItem);
            }
            for (final String itemName : itemNames) {
                final ResourceLocation rl = ResourceLocation.fromNamespaceAndPath(
                        TCUtilityMain.MODID, itemName);
                final Item item = new Item(new Item.Properties()
                        .setId(ResourceKey.create(Registries.ITEM, rl)));
                helper.register(rl, item);
            }
        });
    }

    private static ArmorType mapType(final ArmorTypes type) {
        switch (type) {
            case HEAD:
                return ArmorType.HELMET;
            case CHEST:
                return ArmorType.CHESTPLATE;
            case LEGS:
                return ArmorType.LEGGINGS;
            case FEET:
                return ArmorType.BOOTS;
            default:
                throw new IllegalStateException("Unknown armor slot " + type);
        }
    }

    /**
     * 1.21.4 ArmorMaterial-Record: Defense pro Slot, Durability + Repair-Tag (statt
     * Ingredient.Supplier), Equipment-Asset-Key zeigt auf das Render-Layer-Asset.
     * Der Asset-Key wird einfach pro Material-Name gebaut; das tatsaechliche Asset-File
     * unter {@code assets/<modid>/equipment/<name>.json} muss separat existieren bzw. wird
     * mangels Asset auf Default gerendert.
     */
    private static ArmorMaterial makeArmorMaterial(final String name,
            final ArmorCreateInfo info) {
        final Map<ArmorType, Integer> defense = new EnumMap<>(ArmorType.class);
        for (final ArmorType type : ArmorType.values()) {
            defense.put(type, 1);
        }
        final ResourceKey<EquipmentAsset> assetId = ResourceKey.create(EquipmentAssets.ROOT_ID,
                ResourceLocation.fromNamespaceAndPath(TCUtilityMain.MODID, name));
        return new ArmorMaterial(
                info.durability,
                defense,
                info.enchantability,
                SoundEvents.ARMOR_EQUIP_GENERIC,
                info.toughness,
                0.0F,
                ItemTags.REPAIRS_LEATHER_ARMOR,
                assetId);
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
