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

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

/**
 * Item-Registrierung. In 1.21 ist {@link ArmorMaterial} ein {@code record} (final, kein
 * anonym implementierbares Interface mehr) und {@link ArmorItem} erwartet einen
 * {@code Holder<ArmorMaterial>} statt der Material-Instanz selbst. Wir bauen das Material
 * mit dem Record-Konstruktor und wickeln es per {@link Holder#direct(Object)} ein -- damit
 * landen die Mod-eigenen Materials nicht in der Daten-Registry, was fuer reine Render-/
 * Defense-Werte ausreicht.
 */
public final class TCItems {

    private TCItems() {
    }

    private static final class ArmorSpec {
        final String registryName;
        final Holder<ArmorMaterial> material;
        final ArmorItem.Type type;
        final int durabilityFactor;

        ArmorSpec(final String registryName, final Holder<ArmorMaterial> material,
                final ArmorItem.Type type, final int durabilityFactor) {
            this.registryName = registryName;
            this.material = material;
            this.type = type;
            this.durabilityFactor = durabilityFactor;
        }
    }

    private static final List<ArmorSpec> armorSpecs = new ArrayList<>();
    private static final List<String> itemNames = new ArrayList<>();

    public static void init() {
        // Reflection-Pfad fuer manuell deklarierte Items ist mit dem Defer-Modell nicht
        // vereinbar -- Items duerfen erst im RegisterEvent konstruiert werden. No-op.
    }

    public static void initJsonFiles() {
        final Map<String, ArmorProperties> armor = getArmorFromJson("armordefinitions");
        for (final Entry<String, ArmorProperties> armorEntry : armor.entrySet()) {
            final String armorName = armorEntry.getKey();
            final ArmorProperties property = armorEntry.getValue();
            final ArmorCreateInfo armorInfo = property.getArmorInfo();
            final Holder<ArmorMaterial> material = makeArmorMaterial(armorName, armorInfo);
            final List<String> slots = property.getSlots();
            for (final String slot : slots) {
                final ArmorTypes type = Enum.valueOf(ArmorTypes.class, slot.toUpperCase());
                final String registryName = type.getRegistryName(armorName);
                armorSpecs.add(new ArmorSpec(registryName, material, mapType(type),
                        armorInfo.durability));
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
                // 1.21: ArmorItem-Ctor uebernimmt die Durability nicht mehr aus dem Material;
                // wir leiten sie ueber Item.Properties#durability aus dem JSON-Wert ab und
                // nutzen den vanilla Type-Multiplikator (Helmet*11, Chestplate*16, ...).
                final Item.Properties props = new Item.Properties()
                        .durability(spec.type.getDurability(spec.durabilityFactor));
                final ArmorItem armorItem = new ArmorItem(spec.material, spec.type, props);
                helper.register(ResourceLocation.fromNamespaceAndPath(TCUtilityMain.MODID,
                        spec.registryName), armorItem);
            }
            for (final String itemName : itemNames) {
                final Item item = new Item(new Item.Properties());
                helper.register(ResourceLocation.fromNamespaceAndPath(TCUtilityMain.MODID,
                        itemName), item);
            }
        });
    }

    private static ArmorItem.Type mapType(final ArmorTypes type) {
        switch (type) {
            case HEAD:
                return ArmorItem.Type.HELMET;
            case CHEST:
                return ArmorItem.Type.CHESTPLATE;
            case LEGS:
                return ArmorItem.Type.LEGGINGS;
            case FEET:
                return ArmorItem.Type.BOOTS;
            default:
                throw new IllegalStateException("Unknown armor slot " + type);
        }
    }

    /**
     * 1.21: ArmorMaterial ist ein finaler Record. Wir konstruieren ihn direkt und schliessen
     * ihn in einen Direct-Holder ein -- ArmorItem nimmt {@code Holder<ArmorMaterial>}, eine
     * Eintragung in der Vanilla-{@code Registries.ARMOR_MATERIAL}-Registry ist fuer rein
     * funktionale Werte (Defense, Sound, Toughness) nicht noetig.
     *
     * <p>Texture-Layer wird auf {@code <modid>:<armorName>} gesetzt; das Asset-Lookup-Schema
     * sucht spaeter {@code assets/<modid>/textures/entity/equipment/humanoid/<armorName>.png}
     * (1.21-Pfad) bzw. {@code .../humanoid_leggings/<armorName>.png}.
     */
    private static Holder<ArmorMaterial> makeArmorMaterial(final String name,
            final ArmorCreateInfo info) {
        final ResourceLocation assetId = ResourceLocation.fromNamespaceAndPath(
                TCUtilityMain.MODID, name);
        final Map<ArmorItem.Type, Integer> defense = new EnumMap<>(ArmorItem.Type.class);
        for (final ArmorItem.Type type : ArmorItem.Type.values()) {
            defense.put(type, 1);
        }
        final ArmorMaterial material = new ArmorMaterial(
                defense,
                info.enchantability,
                SoundEvents.ARMOR_EQUIP_GENERIC,
                () -> Ingredient.EMPTY,
                List.of(new ArmorMaterial.Layer(assetId)),
                info.toughness,
                0.0F);
        return Holder.direct(material);
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
