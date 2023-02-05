package com.troblecodings.tcutility.init;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import com.troblecodings.tcutility.items.TCBigDoorItem;
import com.troblecodings.tcutility.items.TCDoorItem;
import com.troblecodings.tcutility.utils.BlockCreateInfo;
import com.troblecodings.tcutility.utils.BlockProperties;
import com.troblecodings.tcutility.utils.FileReader;

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
            if (Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers)
                    && Modifier.isPublic(modifiers)) {
                final String name = field.getName().toLowerCase();
                try {
                    final Block block = (Block) field.get(null);
                    block.setRegistryName(new ResourceLocation(TCUtilityMain.MODID, name));
                    block.setUnlocalizedName(name);
                    blocksToRegister.add(block);
                    if (block instanceof ITileEntityProvider) {
                        final ITileEntityProvider provider = (ITileEntityProvider) block;
                        try {
                            final Class<? extends TileEntity> tileclass = provider
                                    .createNewTileEntity(null, 0).getClass();
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
        System.out.println(blocksToRegister);
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
        final Map<String, BlockProperties> blocks = FileReader
                .getFromJson("/assets/tcutility/blockdefinitions");

        for (final Entry<String, BlockProperties> blocksEntry : blocks.entrySet()) {
            final String objectname = blocksEntry.getKey();

            final BlockProperties property = blocksEntry.getValue();

            final BlockCreateInfo blockInfo = property.getBlockInfo();

            final List<String> states = property.getStates();

            for (final String state : states) {
                switch (state) {
                    case "cube":
                        final TCCube cube = new TCCube(blockInfo);
                        cube.setRegistryName(
                                new ResourceLocation(TCUtilityMain.MODID, "block_" + objectname));
                        cube.setUnlocalizedName("block_" + objectname);
                        blocksToRegister.add(cube);
                        break;
                    case "cube_rot":
                        final TCCubeRotation cuberot = new TCCubeRotation(blockInfo);
                        cuberot.setRegistryName(new ResourceLocation(
                                 TCUtilityMain.MODID, "block_rot_" + objectname));
                        cuberot.setUnlocalizedName("block_rot_" + objectname);
                        blocksToRegister.add(cuberot);
                        break;
                    case "stair":
                        final TCStairs stair = new TCStairs(blockInfo);
                        stair.setRegistryName(
                                new ResourceLocation(TCUtilityMain.MODID, "stair_" + objectname));
                        stair.setUnlocalizedName("stair_" + objectname);
                        blocksToRegister.add(stair);
                        break;
                    case "slab":
                        final TCSlab slab = new TCSlab(blockInfo);
                        slab.setRegistryName(
                                new ResourceLocation(TCUtilityMain.MODID, "slab_" + objectname));
                        slab.setUnlocalizedName("slab_" + objectname);
                        blocksToRegister.add(slab);
                        break;
                    case "fence":
                        final TCFence fence = new TCFence(blockInfo);
                        fence.setRegistryName(
                                new ResourceLocation(TCUtilityMain.MODID, "fence_" + objectname));
                        fence.setUnlocalizedName("fence_" + objectname);
                        blocksToRegister.add(fence);
                        break;
                    case "fence_gate":
                        final TCFenceGate fencegate = new TCFenceGate(blockInfo);
                        fencegate.setRegistryName(new ResourceLocation(TCUtilityMain.MODID,
                                "fence_gate_" + objectname));
                        fencegate.setUnlocalizedName("fence_gate_" + objectname);
                        blocksToRegister.add(fencegate);
                        break;
                    case "wall":
                        final TCWall wall = new TCWall(blockInfo);
                        wall.setRegistryName(
                                new ResourceLocation(TCUtilityMain.MODID, "wall_" + objectname));
                        wall.setUnlocalizedName("wall_" + objectname);
                        blocksToRegister.add(wall);
                        break;
                    case "trapdoor":
                        final TCTrapDoor trapdoor = new TCTrapDoor(blockInfo);
                        trapdoor.setRegistryName(new ResourceLocation(TCUtilityMain.MODID,
                                "trapdoor_" + objectname));
                        trapdoor.setUnlocalizedName("trapdoor_" + objectname);
                        blocksToRegister.add(trapdoor);
                        break;
                    case "window":
                        final TCWindow window = new TCWindow(blockInfo);
                        window.setRegistryName(
                                new ResourceLocation(TCUtilityMain.MODID, "window_" + objectname));
                        window.setUnlocalizedName("window_" + objectname);
                        blocksToRegister.add(window);
                        break;
                    case "ladder":
                        final TCLadder ladder = new TCLadder(blockInfo);
                        ladder.setRegistryName(
                                new ResourceLocation(TCUtilityMain.MODID, "ladder_" + objectname));
                        ladder.setUnlocalizedName("ladder_" + objectname);
                        blocksToRegister.add(ladder);
                        break;
                    case "door":
                        final TCDoor door = new TCDoor(blockInfo);
                        door.setRegistryName(new ResourceLocation(TCUtilityMain.MODID,
                                "door_" + objectname + "_block"));
                        door.setUnlocalizedName("door_" + objectname + "_block");
                        blocksToRegister.add(door);
                        final TCDoorItem dooritem = new TCDoorItem(door);
                        dooritem.setRegistryName(
                                new ResourceLocation(TCUtilityMain.MODID, "door_" + objectname));
                        dooritem.setUnlocalizedName("door_" + objectname);
                        TCItems.itemsToRegister.add(dooritem);
                        break;
                    case "bigdoor":
                        final TCBigDoor bigdoor = new TCBigDoor(blockInfo);
                        bigdoor.setRegistryName(new ResourceLocation(TCUtilityMain.MODID,
                                "bigdoor_" + objectname + "_block"));
                        bigdoor.setUnlocalizedName("bigdoor_" + objectname + "_block");
                        blocksToRegister.add(bigdoor);
                        final TCBigDoorItem bigdooritem = new TCBigDoorItem(bigdoor);
                        bigdooritem.setRegistryName(
                                new ResourceLocation(TCUtilityMain.MODID, "bigdoor_" + objectname));
                        bigdooritem.setUnlocalizedName("bigdoor_" + objectname);
                        TCItems.itemsToRegister.add(bigdooritem);
                        break;
                    default:
                        TCUtilityMain.LOG.error("The given state " + state + " is not valid.");
                        break;
                }
            }
        }
    }
}