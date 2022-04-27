package eu.gir.girutility.init;

import java.util.ArrayList;

import eu.gir.girutility.GirutilityMain;
import eu.gir.girutility.items.ItemReflectiveArmor;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class GIRItems {

    public static final ArmorMaterial reflectiveArmorMaterial = EnumHelper
            .addArmorMaterial("reflective", GirutilityMain.MODID + ":reflective", 1000, new int[] {
                    1, 1, 1, 1
            }, 30, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0F);
    public static final ItemReflectiveArmor reflectiveChestplate = new ItemReflectiveArmor(
            reflectiveArmorMaterial, 1, EntityEquipmentSlot.CHEST);

    public static ArrayList<Item> itemsToRegister = new ArrayList<>();

    public static void init() {
        setName(reflectiveChestplate, "reflective_chestplate");

    }

    @SubscribeEvent
    public static void registerItem(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        registry.register(reflectiveChestplate);
    }

    public static void setName(Item item, String name) {
        item.setRegistryName(new ResourceLocation(GirutilityMain.MODID, name));
        item.setUnlocalizedName(name);
        itemsToRegister.add(item);
    }
}
