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

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public final class TCItems {

    private TCItems() {
    }

    public static ArrayList<Item> itemsToRegister = new ArrayList<>();

    public static void init() {
        final Field[] fields = TCItems.class.getFields();
        for (final Field field : fields) {
            final int modifiers = field.getModifiers();
            if (Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers)
                    && Modifier.isPublic(modifiers)) {
                final String name = field.getName().toLowerCase();
                try {
                    final Item item = (Item) field.get(null);
                    item.setRegistryName(new ResourceLocation(TCUtilityMain.MODID, name));
                    item.setUnlocalizedName(name);
                    itemsToRegister.add(item);
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

    public static void setName(final Item item, final String name) {
        item.setRegistryName(new ResourceLocation(TCUtilityMain.MODID, name));
        item.setUnlocalizedName(name);
        itemsToRegister.add(item);
        System.out.println(item.getRegistryName());
    }

    private static Map<String, ArmorProperties> getFromJson(final String directory) {
        final Gson gson = new Gson();
        final List<Entry<String, String>> entrySet = TCUtilityMain.fileHandler.getFiles(directory);
        final Map<String, ArmorProperties> properties = new HashMap<>();
        final Type typeOfHashMap = new TypeToken<Map<String, ArmorProperties>>() {
        }.getType();
        if (entrySet != null) {
            entrySet.forEach(entry -> {
                final Map<String, ArmorProperties> json = gson.fromJson(entry.getValue(),
                        typeOfHashMap);
                properties.putAll(json);
            });
        }
        return properties;
    }

    public static void initJsonFiles() {
        final Map<String, ArmorProperties> armor = getFromJson("armordefinitions");

        for (final Entry<String, ArmorProperties> armorEntry : armor.entrySet()) {
            final String armorName = armorEntry.getKey();
            final ArmorProperties property = armorEntry.getValue();
            final ArmorCreateInfo armorInfo = property.getArmorInfo();
            final List<String> slots = property.getSlots();

            for (final String slot : slots) {
                final ArmorTypes type = Enum.valueOf(ArmorTypes.class, slot.toUpperCase());
                final String registryName = type.getRegistryName(armorName);

                switch (type) {
                    case HEAD:
                        final ArmorMaterial materialHead = EnumHelper.addArmorMaterial(armorName,
                                TCUtilityMain.MODID + ":" + armorName, armorInfo.durability,
                                new int[] {
                                        1, 1, 1, 1
                                }, armorInfo.enchantability, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC,
                                armorInfo.toughness);
                        final ItemArmor armorHead = new ItemArmor(materialHead, 1,
                                EntityEquipmentSlot.HEAD);
                        armorHead.setRegistryName(
                                new ResourceLocation(TCUtilityMain.MODID, registryName));
                        armorHead.setUnlocalizedName(registryName);
                        itemsToRegister.add(armorHead);
                        armorHead.setCreativeTab(CreativeTabs.COMBAT);
                        break;
                    case CHEST:
                        final ArmorMaterial materialChest = EnumHelper.addArmorMaterial(armorName,
                                TCUtilityMain.MODID + ":" + armorName, armorInfo.durability,
                                new int[] {
                                        1, 1, 1, 1
                                }, armorInfo.enchantability, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC,
                                armorInfo.toughness);
                        final ItemArmor armorChest = new ItemArmor(materialChest, 1,
                                EntityEquipmentSlot.CHEST);
                        armorChest.setRegistryName(
                                new ResourceLocation(TCUtilityMain.MODID, registryName));
                        armorChest.setUnlocalizedName(registryName);
                        itemsToRegister.add(armorChest);
                        armorChest.setCreativeTab(CreativeTabs.COMBAT);
                        break;
                    case LEGS:
                        final ArmorMaterial materialLegs = EnumHelper.addArmorMaterial(armorName,
                                TCUtilityMain.MODID + ":" + armorName, armorInfo.durability,
                                new int[] {
                                        1, 1, 1, 1
                                }, armorInfo.enchantability, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC,
                                armorInfo.toughness);
                        final ItemArmor armorLegs = new ItemArmor(materialLegs, 1,
                                EntityEquipmentSlot.LEGS);
                        armorLegs.setRegistryName(
                                new ResourceLocation(TCUtilityMain.MODID, registryName));
                        armorLegs.setUnlocalizedName(registryName);
                        itemsToRegister.add(armorLegs);
                        armorLegs.setCreativeTab(CreativeTabs.COMBAT);
                        break;
                    case FEET:
                        final ArmorMaterial materialFeet = EnumHelper.addArmorMaterial(armorName,
                                TCUtilityMain.MODID + ":" + armorName, armorInfo.durability,
                                new int[] {
                                        1, 1, 1, 1
                                }, armorInfo.enchantability, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC,
                                armorInfo.toughness);
                        final ItemArmor armorFeet = new ItemArmor(materialFeet, 1,
                                EntityEquipmentSlot.FEET);
                        armorFeet.setRegistryName(
                                new ResourceLocation(TCUtilityMain.MODID, registryName));
                        armorFeet.setUnlocalizedName(registryName);
                        itemsToRegister.add(armorFeet);
                        armorFeet.setCreativeTab(CreativeTabs.COMBAT);
                        break;
                    default:
                        throw new IllegalStateException(
                                "The given state " + slot + " is not valid.");
                }
            }
        }
    }
}
