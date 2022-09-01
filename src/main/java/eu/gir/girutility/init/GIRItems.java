package eu.gir.girutility.init;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import eu.gir.girutility.GirutilityMain;
import eu.gir.girutility.items.ItemBigDoor;
import eu.gir.girutility.items.ItemDoor;
import eu.gir.girutility.items.ItemReflectiveArmor;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class GIRItems {

    public static final ItemReflectiveArmor REFLECTIVE_CHESTPLATE = new ItemReflectiveArmor(
            ItemReflectiveArmor.reflectiveArmorMaterial, 1, EntityEquipmentSlot.CHEST);
    public static final ItemDoor DOOR_JAIL = new ItemDoor(GIRBlocks.DOOR_JAIL_BLOCK);
    public static final ItemDoor DOOR_IRON_GLASS = new ItemDoor(GIRBlocks.DOOR_IRON_GLASS_BLOCK);
    public static final ItemDoor DOOR_OAK_GLASS = new ItemDoor(GIRBlocks.DOOR_OAK_GLASS_BLOCK);
    public static final ItemDoor DOOR_OAK_WINDOWED = new ItemDoor(
            GIRBlocks.DOOR_OAK_WINDOWED_BLOCK);
    public static final ItemDoor DOOR_SPRUCE_WINDOWED = new ItemDoor(
            GIRBlocks.DOOR_SPRUCE_WINDOWED_BLOCK);
    public static final ItemDoor DOOR_BIRCH_WINDOWED = new ItemDoor(
            GIRBlocks.DOOR_BIRCH_WINDOWED_BLOCK);
    public static final ItemDoor DOOR_JUNGLE_WINDOWED = new ItemDoor(
            GIRBlocks.DOOR_JUNGLE_WINDOWED_BLOCK);
    public static final ItemDoor DOOR_ACACIA_WINDOWED = new ItemDoor(
            GIRBlocks.DOOR_ACACIA_WINDOWED_BLOCK);
    public static final ItemDoor DOOR_DARK_OAK_WINDOWED = new ItemDoor(
            GIRBlocks.DOOR_DARK_OAK_WINDOWED_BLOCK);
    public static final ItemDoor DOOR_IRON_WINDOWED = new ItemDoor(
            GIRBlocks.DOOR_IRON_WINDOWED_BLOCK);
    public static final ItemDoor DOOR_WHITE_WINDOWED = new ItemDoor(
            GIRBlocks.DOOR_WHITE_WINDOWED_BLOCK);
    public static final ItemDoor DOOR_OAK = new ItemDoor(GIRBlocks.DOOR_OAK_BLOCK);
    public static final ItemDoor DOOR_SPRUCE = new ItemDoor(GIRBlocks.DOOR_SPRUCE_BLOCK);
    public static final ItemDoor DOOR_BIRCH = new ItemDoor(GIRBlocks.DOOR_BIRCH_BLOCK);
    public static final ItemDoor DOOR_JUNGLE = new ItemDoor(GIRBlocks.DOOR_JUNGLE_BLOCK);
    public static final ItemDoor DOOR_ACACIA = new ItemDoor(GIRBlocks.DOOR_ACACIA_BLOCK);
    public static final ItemDoor DOOR_DARK_OAK = new ItemDoor(GIRBlocks.DOOR_DARK_OAK_BLOCK);
    public static final ItemDoor DOOR_IRON = new ItemDoor(GIRBlocks.DOOR_IRON_BLOCK);
    public static final ItemDoor DOOR_WHITE = new ItemDoor(GIRBlocks.DOOR_WHITE_BLOCK);
    public static final ItemDoor DOOR_OLD = new ItemDoor(GIRBlocks.DOOR_OLD_BLOCK);
    public static final ItemDoor DOOR_RUSTY = new ItemDoor(GIRBlocks.DOOR_RUSTY_BLOCK);
    public static final ItemBigDoor BIGDOOR_OAK = new ItemBigDoor(GIRBlocks.BIGDOOR_OAK_BLOCK);
    public static final ItemBigDoor BIGDOOR_SPRUCE = new ItemBigDoor(
            GIRBlocks.BIGDOOR_SPRUCE_BLOCK);
    public static final ItemBigDoor BIGDOOR_BIRCH = new ItemBigDoor(GIRBlocks.BIGDOOR_BIRCH_BLOCK);
    public static final ItemBigDoor BIGDOOR_JUNGLE = new ItemBigDoor(
            GIRBlocks.BIGDOOR_JUNGLE_BLOCK);
    public static final ItemBigDoor BIGDOOR_ACACIA = new ItemBigDoor(
            GIRBlocks.BIGDOOR_ACACIA_BLOCK);
    public static final ItemBigDoor BIGDOOR_DARK_OAK = new ItemBigDoor(
            GIRBlocks.BIGDOOR_DARK_OAK_BLOCK);
    public static final ItemBigDoor BIGDOOR_IRON = new ItemBigDoor(GIRBlocks.BIGDOOR_IRON_BLOCK);
    public static final ItemBigDoor BIGDOOR_WHITE = new ItemBigDoor(GIRBlocks.BIGDOOR_WHITE_BLOCK);

    public static ArrayList<Item> itemsToRegister = new ArrayList<>();

    public static void init() {
        final Field[] fields = GIRItems.class.getFields();
        for (final Field field : fields) {
            final int modifiers = field.getModifiers();
            if (Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers)
                    && Modifier.isPublic(modifiers)) {
                final String name = field.getName().toLowerCase();
                try {
                    final Item item = (Item) field.get(null);
                    item.setRegistryName(new ResourceLocation(GirutilityMain.MODID, name));
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
        item.setRegistryName(new ResourceLocation(GirutilityMain.MODID, name));
        item.setUnlocalizedName(name);
        itemsToRegister.add(item);
    }
}
