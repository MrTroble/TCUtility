package com.troblecodings.tcutility.init;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.troblecodings.tcutility.BlockProperties;
import com.troblecodings.tcutility.TCUtilityMain;
import com.troblecodings.tcutility.blocks.BigDoor;
import com.troblecodings.tcutility.blocks.Bin;
import com.troblecodings.tcutility.blocks.BlockCreateInfo;
import com.troblecodings.tcutility.blocks.Clock;
import com.troblecodings.tcutility.blocks.Door;
import com.troblecodings.tcutility.blocks.Lantern;
import com.troblecodings.tcutility.blocks.Slab;
import com.troblecodings.tcutility.blocks.Stairs;
import com.troblecodings.tcutility.blocks.TCCube;
import com.troblecodings.tcutility.blocks.TCCubeRotation;
import com.troblecodings.tcutility.blocks.TCFence;
import com.troblecodings.tcutility.blocks.TCFenceGate;
import com.troblecodings.tcutility.blocks.TCLadder;
import com.troblecodings.tcutility.blocks.TCTrapDoor;
import com.troblecodings.tcutility.blocks.TCWall;
import com.troblecodings.tcutility.blocks.TCWindow;
import com.troblecodings.tcutility.blocks.TrafficCone;
import com.troblecodings.tcutility.utils.FileReader;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
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

    public static final TrafficCone TRAFFIC_CONE = new TrafficCone();
    public static final Bin BIN = new Bin();
    public static final Lantern LANTERN = new Lantern();
    public static final Door DOOR_JAIL_BLOCK = new Door(Material.IRON);
    public static final Door DOOR_IRON_GLASS_BLOCK = new Door(Material.IRON);
    public static final Door DOOR_OAK_GLASS_BLOCK = new Door(Material.WOOD);
    public static final Door DOOR_OAK_WINDOWED_BLOCK = new Door(Material.WOOD);
    public static final Door DOOR_SPRUCE_WINDOWED_BLOCK = new Door(Material.WOOD);
    public static final Door DOOR_BIRCH_WINDOWED_BLOCK = new Door(Material.WOOD);
    public static final Door DOOR_JUNGLE_WINDOWED_BLOCK = new Door(Material.WOOD);
    public static final Door DOOR_ACACIA_WINDOWED_BLOCK = new Door(Material.WOOD);
    public static final Door DOOR_DARK_OAK_WINDOWED_BLOCK = new Door(Material.WOOD);
    public static final Door DOOR_IRON_WINDOWED_BLOCK = new Door(Material.IRON);
    public static final Door DOOR_WHITE_WINDOWED_BLOCK = new Door(Material.WOOD);
    public static final Door DOOR_OAK_BLOCK = new Door(Material.WOOD);
    public static final Door DOOR_SPRUCE_BLOCK = new Door(Material.WOOD);
    public static final Door DOOR_BIRCH_BLOCK = new Door(Material.WOOD);
    public static final Door DOOR_JUNGLE_BLOCK = new Door(Material.WOOD);
    public static final Door DOOR_ACACIA_BLOCK = new Door(Material.WOOD);
    public static final Door DOOR_DARK_OAK_BLOCK = new Door(Material.WOOD);
    public static final Door DOOR_IRON_BLOCK = new Door(Material.IRON);
    public static final Door DOOR_WHITE_BLOCK = new Door(Material.WOOD);
    public static final Door DOOR_OLD_BLOCK = new Door(Material.WOOD);
    public static final Door DOOR_RUSTY_BLOCK = new Door(Material.IRON);
    public static final BigDoor BIGDOOR_OAK_BLOCK = new BigDoor(Material.WOOD);
    public static final BigDoor BIGDOOR_SPRUCE_BLOCK = new BigDoor(Material.WOOD);
    public static final BigDoor BIGDOOR_BIRCH_BLOCK = new BigDoor(Material.WOOD);
    public static final BigDoor BIGDOOR_JUNGLE_BLOCK = new BigDoor(Material.WOOD);
    public static final BigDoor BIGDOOR_ACACIA_BLOCK = new BigDoor(Material.WOOD);
    public static final BigDoor BIGDOOR_DARK_OAK_BLOCK = new BigDoor(Material.WOOD);
    public static final BigDoor BIGDOOR_IRON_BLOCK = new BigDoor(Material.IRON);
    public static final BigDoor BIGDOOR_WHITE_BLOCK = new BigDoor(Material.WOOD);
    public static final Clock CLOCK = new Clock();

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
    }

    @SubscribeEvent
    public static void registerItem(final RegistryEvent.Register<Item> event) {
        final IForgeRegistry<Item> registry = event.getRegistry();
        blocksToRegister.forEach(block -> registry
                .register(new ItemBlock(block).setRegistryName(block.getRegistryName())));
    }

    public static void initJsonFiles() {
        final Map<String, BlockProperties> blocks = FileReader
                .getFromJson("/assets/tcutility/blockdefinitions");

        System.out.println(blocks);
        for (final Entry<String, BlockProperties> blocksEntry : blocks.entrySet()) {
            final String objectname = blocksEntry.getKey();

            System.out.println(objectname);

            System.out.println(blocksEntry.getValue());

            final BlockProperties property = blocksEntry.getValue();

            final BlockCreateInfo blockInfo = property.getBlockInfo();

            final List<String> states = property.getStates();

            for (final String state : states) {
                switch (state) {
                    case "stair":
                        final TCCube tCCube = new TCCube(blockInfo);
                        final Block stair = new Stairs(tCCube.getDefaultState());
                        stair.setRegistryName(
                                new ResourceLocation(TCUtilityMain.MODID, objectname + "_stair"));
                        stair.setUnlocalizedName(objectname + "_stair");
                        blocksToRegister.add(stair);
                    case "slab":
                        final Slab slab = new Slab(blockInfo);
                        slab.setRegistryName(
                                new ResourceLocation(TCUtilityMain.MODID, objectname + "_slab"));
                        slab.setUnlocalizedName(objectname + "_slab");
                        blocksToRegister.add(slab);
                    case "cube":
                        final TCCube cube = new TCCube(blockInfo);
                        cube.setRegistryName(
                                new ResourceLocation(TCUtilityMain.MODID, objectname));
                        cube.setUnlocalizedName(objectname);
                        blocksToRegister.add(cube);
                        break;
                    case "cube_rot":
                        final TCCubeRotation cuberot = new TCCubeRotation(blockInfo);
                        cuberot.setRegistryName(
                                new ResourceLocation(TCUtilityMain.MODID, objectname));
                        cuberot.setUnlocalizedName(objectname);
                        blocksToRegister.add(cuberot);
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
                        fencegate.setRegistryName(
                                new ResourceLocation(TCUtilityMain.MODID, "fence_gate_" + objectname));
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
                        trapdoor.setRegistryName(
                                new ResourceLocation(TCUtilityMain.MODID, "trapdoor_" + objectname));
                        trapdoor.setUnlocalizedName("trapdoor_" + objectname);
                        blocksToRegister.add(trapdoor);
                        break;
                    case "window":
                        final TCWindow window = new TCWindow(blockInfo);
                        window.setRegistryName(
                                new ResourceLocation(TCUtilityMain.MODID, objectname + "_window"));
                        window.setUnlocalizedName(objectname + "_window");
                        blocksToRegister.add(window);
                        break;
                    case "ladder":
                        final TCLadder ladder = new TCLadder(blockInfo);
                        ladder.setRegistryName(
                                new ResourceLocation(TCUtilityMain.MODID, "ladder_" + objectname));
                        ladder.setUnlocalizedName("ladder_" + objectname);
                        blocksToRegister.add(ladder);
                        break;
                    default:
                        TCUtilityMain.LOG.error("The given state " + state + " is not valid.");
                        break;
                }
            }
        }
    }
}