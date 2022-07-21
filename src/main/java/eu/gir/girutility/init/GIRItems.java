package eu.gir.girutility.init;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import eu.gir.girutility.GirutilityMain;
import eu.gir.girutility.items.ItemReflectiveArmor;
import eu.gir.girutility.items.SlabItem;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class GIRItems {

    public static final ItemReflectiveArmor reflective_Chestplate = new ItemReflectiveArmor(
            ItemReflectiveArmor.reflectiveArmorMaterial, 1, EntityEquipmentSlot.CHEST);
    public static final SlabItem slabItem = new SlabItem(GIRBlocks.SLAB1_HALF, GIRBlocks.SLAB1_HALF,
            GIRBlocks.SLAB1_DOUBLE);

    public static ArrayList<Item> itemsToRegister = new ArrayList<>();

    public static void init() {
        Field[] fields = GIRItems.class.getFields();
        for (Field field : fields) {
            int modifiers = field.getModifiers();
            if (Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers)
                    && Modifier.isPublic(modifiers)) {
                String name = field.getName().toLowerCase();
                try {
                    Item item = (Item) field.get(null);
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
    public static void registerItem(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        itemsToRegister.forEach(registry::register);
    }

    public static void setName(Item item, String name) {
        item.setRegistryName(new ResourceLocation(GirutilityMain.MODID, name));
        item.setUnlocalizedName(name);
        itemsToRegister.add(item);
    }
}
