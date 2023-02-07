package com.troblecodings.tcutility.init;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import com.troblecodings.tcutility.TCUtilityMain;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
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
}
