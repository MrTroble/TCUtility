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
import com.troblecodings.tcutility.blocks.TCBigDoor;
import com.troblecodings.tcutility.blocks.TCCube;
import com.troblecodings.tcutility.blocks.TCCubeRotation;
import com.troblecodings.tcutility.blocks.TCDoor;
import com.troblecodings.tcutility.blocks.TCFence;
import com.troblecodings.tcutility.blocks.TCFenceGate;
import com.troblecodings.tcutility.blocks.TCLadder;
import com.troblecodings.tcutility.blocks.TCSlab;
import com.troblecodings.tcutility.blocks.TCStairs;
import com.troblecodings.tcutility.blocks.TCTrapDoor;
import com.troblecodings.tcutility.blocks.TCWall;
import com.troblecodings.tcutility.blocks.TCWindow;
import com.troblecodings.tcutility.enums.BlockTypes;
import com.troblecodings.tcutility.items.TCBigDoorItem;
import com.troblecodings.tcutility.items.TCDoorItem;
import com.troblecodings.tcutility.utils.BlockCreateInfo;
import com.troblecodings.tcutility.utils.BlockProperties;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public final class TCBlocks {

	private TCBlocks() {
	}

	public static ArrayList<Block> blocksToRegister = new ArrayList<>();

	public static void init() {
		final Field[] fields = TCBlocks.class.getFields();
		for (final Field field : fields) {
			final int modifiers = field.getModifiers();
			if (Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers) && Modifier.isPublic(modifiers)) {
				final String name = field.getName().toLowerCase();
				try {
					final Block block = (Block) field.get(null);
					block.setRegistryName(new ResourceLocation(TCUtilityMain.MODID, name));
					block.setUnlocalizedName(name);
					blocksToRegister.add(block);
					if (block instanceof ITileEntityProvider) {
						final ITileEntityProvider provider = (ITileEntityProvider) block;
						try {
							final Class<? extends TileEntity> tileclass = provider.createNewTileEntity(null, 0)
									.getClass();
							TileEntity.register(tileclass.getSimpleName().toLowerCase(), tileclass);
						} catch (final NullPointerException ex) {
							TCUtilityMain.LOG.trace(
									"All tileentity provide need to call back a default entity if the world is null!",
									ex);
						}
					}
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

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
		final Map<String, BlockProperties> blocks = getFromJson("blockdefinitions");

		for (final Entry<String, BlockProperties> blocksEntry : blocks.entrySet()) {
			final String objectname = blocksEntry.getKey();

			final BlockProperties property = blocksEntry.getValue();

			final BlockCreateInfo blockInfo = property.getBlockInfo();

			final List<String> states = property.getStates();

			for (final String state : states) {
				final BlockTypes type = Enum.valueOf(BlockTypes.class, state.toUpperCase());
				final String registryName = type.getRegistryName(objectname);
				switch (type) {
				case CUBE:
					final TCCube cube = new TCCube(blockInfo);
					cube.setRegistryName(new ResourceLocation(TCUtilityMain.MODID, registryName));
					cube.setUnlocalizedName(registryName);
					blocksToRegister.add(cube);
					break;
				case CUBE_ROT:
					final TCCubeRotation cuberot = new TCCubeRotation(blockInfo);
					cuberot.setRegistryName(new ResourceLocation(TCUtilityMain.MODID, registryName));
					cuberot.setUnlocalizedName(registryName);
					blocksToRegister.add(cuberot);
					break;
				case STAIR:
					final TCStairs stair = new TCStairs(blockInfo);
					stair.setRegistryName(new ResourceLocation(TCUtilityMain.MODID, registryName));
					stair.setUnlocalizedName(registryName);
					blocksToRegister.add(stair);
					break;
				case SLAB:
					final TCSlab slab = new TCSlab(blockInfo);
					slab.setRegistryName(new ResourceLocation(TCUtilityMain.MODID, registryName));
					slab.setUnlocalizedName(registryName);
					blocksToRegister.add(slab);
					break;
				case FENCE:
					final TCFence fence = new TCFence(blockInfo);
					fence.setRegistryName(new ResourceLocation(TCUtilityMain.MODID, registryName));
					fence.setUnlocalizedName("fence_" + objectname);
					blocksToRegister.add(fence);
					break;
				case FENCE_GATE:
					final TCFenceGate fencegate = new TCFenceGate(blockInfo);
					fencegate.setRegistryName(new ResourceLocation(TCUtilityMain.MODID, registryName));
					fencegate.setUnlocalizedName(registryName);
					blocksToRegister.add(fencegate);
					break;
				case WALL:
					final TCWall wall = new TCWall(blockInfo);
					wall.setRegistryName(new ResourceLocation(TCUtilityMain.MODID, registryName));
					wall.setUnlocalizedName(registryName);
					blocksToRegister.add(wall);
					break;
				case TRAPDOOR:
					final TCTrapDoor trapdoor = new TCTrapDoor(blockInfo);
					trapdoor.setRegistryName(new ResourceLocation(TCUtilityMain.MODID, registryName));
					trapdoor.setUnlocalizedName("latch_" + objectname);
					blocksToRegister.add(trapdoor);
					break;
				case WINDOW:
					final TCWindow window = new TCWindow(blockInfo);
					window.setRegistryName(new ResourceLocation(TCUtilityMain.MODID, registryName));
					window.setUnlocalizedName(registryName);
					blocksToRegister.add(window);
					break;
				case LADDER:
					final TCLadder ladder = new TCLadder(blockInfo);
					ladder.setRegistryName(new ResourceLocation(TCUtilityMain.MODID, registryName));
					ladder.setUnlocalizedName(registryName);
					blocksToRegister.add(ladder);
					break;
				case DOOR:
					final TCDoor door = new TCDoor(blockInfo);
					door.setRegistryName(new ResourceLocation(TCUtilityMain.MODID, registryName));
					door.setUnlocalizedName(registryName);
					blocksToRegister.add(door);
					final TCDoorItem dooritem = new TCDoorItem(door);
					dooritem.setRegistryName(new ResourceLocation(TCUtilityMain.MODID, "door_" + objectname));
					dooritem.setUnlocalizedName("door_" + objectname);
					TCItems.itemsToRegister.add(dooritem);
					break;
				case BIGDOOR:
					final TCBigDoor bigdoor = new TCBigDoor(blockInfo);
					bigdoor.setRegistryName(new ResourceLocation(TCUtilityMain.MODID, registryName));
					bigdoor.setUnlocalizedName(registryName);
					blocksToRegister.add(bigdoor);
					final TCBigDoorItem bigdooritem = new TCBigDoorItem(bigdoor);
					bigdooritem.setRegistryName(new ResourceLocation(TCUtilityMain.MODID, "bigdoor_" + objectname));
					bigdooritem.setUnlocalizedName("bigdoor_" + objectname);
					TCItems.itemsToRegister.add(bigdooritem);
					break;
				default:
					throw new IllegalStateException("The given state " + state + " is not valid.");
				}
			}
		}
	}

	private static Map<String, BlockProperties> getFromJson(final String directory) {
		final Gson gson = new Gson();
		final List<Entry<String, String>> entrySet = TCUtilityMain.fileHandler.getFiles(directory);
		final Map<String, BlockProperties> properties = new HashMap<>();
		final Type typeOfHashMap = new TypeToken<Map<String, BlockProperties>>() {
		}.getType();
		if (entrySet != null) {
			entrySet.forEach(entry -> {
				final Map<String, BlockProperties> json = gson.fromJson(entry.getValue(), typeOfHashMap);
				properties.putAll(json);
			});
		}
		return properties;
	}
}