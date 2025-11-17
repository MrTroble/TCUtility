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
import com.troblecodings.tcutility.fluids.TCFluids;
import com.troblecodings.tcutility.utils.FluidCreateInfo;
import com.troblecodings.tcutility.utils.FluidProperties;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class TCFluidsInit {

	private TCFluidsInit() {
	}

	public static ArrayList<Block> blocksToRegister = new ArrayList<>();

	@SubscribeEvent
	public static void registerBlock(final RegistryEvent.Register<Block> event) {
		final IForgeRegistry<Block> registry = event.getRegistry();
		blocksToRegister.forEach(registry::register);
	}

	@SubscribeEvent
	public static void registerItem(final RegistryEvent.Register<Item> event) {
		final IForgeRegistry<Item> registry = event.getRegistry();
		blocksToRegister.forEach(block -> {
			if (!block.toString().contains("door")) {
				registry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
			}
		});
	}

	public static void initJsonFiles() {
		final Map<String, FluidProperties> fluids = getFromJson("fluiddefinitions");

		for (final Entry<String, FluidProperties> fluidsEntry : fluids.entrySet()) {
			final String objectname = fluidsEntry.getKey();

			final FluidProperties property = fluidsEntry.getValue();

			final FluidCreateInfo fluidInfo = property.getFluidInfo();

			final TCFluids fluid = new TCFluids(objectname,
					new ResourceLocation(TCUtilityMain.MODID, ":blocks/" + objectname + "_still"),
					new ResourceLocation(TCUtilityMain.MODID, ":blocks/" + objectname + "_flow"), fluidInfo);

			FluidRegistry.registerFluid(fluid);
			FluidRegistry.addBucketForFluid(fluid);

		}
	}

	private static Map<String, FluidProperties> getFromJson(final String directory) {
		final Gson gson = new Gson();
		final List<Entry<String, String>> entrySet = TCUtilityMain.fileHandler.getFiles(directory);
		final Map<String, FluidProperties> properties = new HashMap<>();
		final Type typeOfHashMap = new TypeToken<Map<String, FluidProperties>>() {
		}.getType();
		if (entrySet != null) {
			entrySet.forEach(entry -> {
				final Map<String, FluidProperties> json = gson.fromJson(entry.getValue(), typeOfHashMap);
				properties.putAll(json);
			});
		}
		return properties;
	}

}
