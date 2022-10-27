package com.troblecodings.tcutility.init;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import com.troblecodings.tcutility.TCUtilityMain;
import com.troblecodings.tcutility.items.ItemBigDoor;
import com.troblecodings.tcutility.items.ItemConductorTrowel;
import com.troblecodings.tcutility.items.ItemDoor;
import com.troblecodings.tcutility.items.ItemReflectiveArmor;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class TCItems {

    public static final ItemReflectiveArmor REFLECTIVE_CHESTPLATE = new ItemReflectiveArmor(
            ItemReflectiveArmor.reflectiveArmorMaterial, 1, EntityEquipmentSlot.CHEST);
    public static final ItemConductorTrowel CONDUCTOR_TROWEL_GREEN = new ItemConductorTrowel();
    public static final ItemConductorTrowel CONDUCTOR_TROWEL_RED = new ItemConductorTrowel();
    public static final ItemDoor DOOR_JAIL = new ItemDoor(TCBlocks.DOOR_JAIL_BLOCK);
    public static final ItemDoor DOOR_IRON_GLASS = new ItemDoor(TCBlocks.DOOR_IRON_GLASS_BLOCK);
    public static final ItemDoor DOOR_OAK_GLASS = new ItemDoor(TCBlocks.DOOR_OAK_GLASS_BLOCK);
    public static final ItemDoor DOOR_OAK_WINDOWED = new ItemDoor(
            TCBlocks.DOOR_OAK_WINDOWED_BLOCK);
    public static final ItemDoor DOOR_SPRUCE_WINDOWED = new ItemDoor(
            TCBlocks.DOOR_SPRUCE_WINDOWED_BLOCK);
    public static final ItemDoor DOOR_BIRCH_WINDOWED = new ItemDoor(
            TCBlocks.DOOR_BIRCH_WINDOWED_BLOCK);
    public static final ItemDoor DOOR_JUNGLE_WINDOWED = new ItemDoor(
            TCBlocks.DOOR_JUNGLE_WINDOWED_BLOCK);
    public static final ItemDoor DOOR_ACACIA_WINDOWED = new ItemDoor(
            TCBlocks.DOOR_ACACIA_WINDOWED_BLOCK);
    public static final ItemDoor DOOR_DARK_OAK_WINDOWED = new ItemDoor(
            TCBlocks.DOOR_DARK_OAK_WINDOWED_BLOCK);
    public static final ItemDoor DOOR_IRON_WINDOWED = new ItemDoor(
            TCBlocks.DOOR_IRON_WINDOWED_BLOCK);
    public static final ItemDoor DOOR_WHITE_WINDOWED = new ItemDoor(
            TCBlocks.DOOR_WHITE_WINDOWED_BLOCK);
    public static final ItemDoor DOOR_OAK = new ItemDoor(TCBlocks.DOOR_OAK_BLOCK);
    public static final ItemDoor DOOR_SPRUCE = new ItemDoor(TCBlocks.DOOR_SPRUCE_BLOCK);
    public static final ItemDoor DOOR_BIRCH = new ItemDoor(TCBlocks.DOOR_BIRCH_BLOCK);
    public static final ItemDoor DOOR_JUNGLE = new ItemDoor(TCBlocks.DOOR_JUNGLE_BLOCK);
    public static final ItemDoor DOOR_ACACIA = new ItemDoor(TCBlocks.DOOR_ACACIA_BLOCK);
    public static final ItemDoor DOOR_DARK_OAK = new ItemDoor(TCBlocks.DOOR_DARK_OAK_BLOCK);
    public static final ItemDoor DOOR_IRON = new ItemDoor(TCBlocks.DOOR_IRON_BLOCK);
    public static final ItemDoor DOOR_WHITE = new ItemDoor(TCBlocks.DOOR_WHITE_BLOCK);
    public static final ItemDoor DOOR_OLD = new ItemDoor(TCBlocks.DOOR_OLD_BLOCK);
    public static final ItemDoor DOOR_RUSTY = new ItemDoor(TCBlocks.DOOR_RUSTY_BLOCK);
    public static final ItemBigDoor BIGDOOR_OAK = new ItemBigDoor(TCBlocks.BIGDOOR_OAK_BLOCK);
    public static final ItemBigDoor BIGDOOR_SPRUCE = new ItemBigDoor(
            TCBlocks.BIGDOOR_SPRUCE_BLOCK);
    public static final ItemBigDoor BIGDOOR_BIRCH = new ItemBigDoor(TCBlocks.BIGDOOR_BIRCH_BLOCK);
    public static final ItemBigDoor BIGDOOR_JUNGLE = new ItemBigDoor(
            TCBlocks.BIGDOOR_JUNGLE_BLOCK);
    public static final ItemBigDoor BIGDOOR_ACACIA = new ItemBigDoor(
            TCBlocks.BIGDOOR_ACACIA_BLOCK);
    public static final ItemBigDoor BIGDOOR_DARK_OAK = new ItemBigDoor(
            TCBlocks.BIGDOOR_DARK_OAK_BLOCK);
    public static final ItemBigDoor BIGDOOR_IRON = new ItemBigDoor(TCBlocks.BIGDOOR_IRON_BLOCK);
    public static final ItemBigDoor BIGDOOR_WHITE = new ItemBigDoor(TCBlocks.BIGDOOR_WHITE_BLOCK);

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
    }
}
