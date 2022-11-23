package com.troblecodings.tcutility.init;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import com.troblecodings.tcutility.TCUtilityMain;
import com.troblecodings.tcutility.blocks.BigDoor;
import com.troblecodings.tcutility.blocks.Bin;
import com.troblecodings.tcutility.blocks.Clock;
import com.troblecodings.tcutility.blocks.Concrete;
import com.troblecodings.tcutility.blocks.Crate;
import com.troblecodings.tcutility.blocks.Door;
import com.troblecodings.tcutility.blocks.Fence;
import com.troblecodings.tcutility.blocks.FenceGate;
import com.troblecodings.tcutility.blocks.Ladder;
import com.troblecodings.tcutility.blocks.Lantern;
import com.troblecodings.tcutility.blocks.PlatformEdge;
import com.troblecodings.tcutility.blocks.TrafficCone;
import com.troblecodings.tcutility.blocks.TrapDoor;
import com.troblecodings.tcutility.blocks.WoodenWindow;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
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
    public static final Crate CRATE = new Crate();
    public static final Crate CRATE_SPRUCE = new Crate();
    public static final Crate CRATE_BIRCH = new Crate();
    public static final Crate CRATE_JUNGLE = new Crate();
    public static final Crate CRATE_ACACIA = new Crate();
    public static final Crate CRATE_DARK_OAK = new Crate();
    public static final Bin BIN = new Bin();
    public static final WoodenWindow WOODEN_WINDOW = new WoodenWindow();
    public static final TrapDoor TRAPDOOR_OAK = new TrapDoor();
    public static final TrapDoor TRAPDOOR_SPRUCE = new TrapDoor();
    public static final TrapDoor TRAPDOOR_BIRCH = new TrapDoor();
    public static final TrapDoor TRAPDOOR_JUNGLE = new TrapDoor();
    public static final TrapDoor TRAPDOOR_ACACIA = new TrapDoor();
    public static final TrapDoor TRAPDOOR_DARK_OAK = new TrapDoor();
    public static final TrapDoor TRAPDOOR_GLASS = new TrapDoor();
    public static final PlatformEdge PLATFORM_EDGE_1_ASPHALT_1 = new PlatformEdge();
    public static final PlatformEdge PLATFORM_EDGE_1_CONCRETE_1 = new PlatformEdge();
    public static final PlatformEdge PLATFORM_EDGE_1_STONE_1 = new PlatformEdge();
    public static final PlatformEdge PLATFORM_EDGE_1_BRICK_1 = new PlatformEdge();
    public static final PlatformEdge PLATFORM_EDGE_1_ASPHALT_2 = new PlatformEdge();
    public static final PlatformEdge PLATFORM_EDGE_1_CONCRETE_2 = new PlatformEdge();
    public static final PlatformEdge PLATFORM_EDGE_1_STONE_2 = new PlatformEdge();
    public static final PlatformEdge PLATFORM_EDGE_1_BRICK_2 = new PlatformEdge();
    public static final PlatformEdge PLATFORM_EDGE_1_ASPHALT_3 = new PlatformEdge();
    public static final PlatformEdge PLATFORM_EDGE_1_CONCRETE_3 = new PlatformEdge();
    public static final PlatformEdge PLATFORM_EDGE_1_STONE_3 = new PlatformEdge();
    public static final PlatformEdge PLATFORM_EDGE_1_BRICK_3 = new PlatformEdge();
    public static final PlatformEdge PLATFORM_EDGE_2_ASPHALT_1 = new PlatformEdge();
    public static final PlatformEdge PLATFORM_EDGE_2_CONCRETE_1 = new PlatformEdge();
    public static final PlatformEdge PLATFORM_EDGE_2_STONE_1 = new PlatformEdge();
    public static final PlatformEdge PLATFORM_EDGE_2_BRICK_1 = new PlatformEdge();
    public static final PlatformEdge PLATFORM_EDGE_2_ASPHALT_2 = new PlatformEdge();
    public static final PlatformEdge PLATFORM_EDGE_2_CONCRETE_2 = new PlatformEdge();
    public static final PlatformEdge PLATFORM_EDGE_2_STONE_2 = new PlatformEdge();
    public static final PlatformEdge PLATFORM_EDGE_2_BRICK_2 = new PlatformEdge();
    public static final PlatformEdge PLATFORM_EDGE_2_ASPHALT_3 = new PlatformEdge();
    public static final PlatformEdge PLATFORM_EDGE_2_CONCRETE_3 = new PlatformEdge();
    public static final PlatformEdge PLATFORM_EDGE_2_STONE_3 = new PlatformEdge();
    public static final PlatformEdge PLATFORM_EDGE_2_BRICK_3 = new PlatformEdge();
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
    public static final Ladder LADDER_IRON = new Ladder(Material.IRON);
    public static final Ladder LADDER_RUSTY = new Ladder(Material.IRON);
    public static final Ladder LADDER_ROOF = new Ladder(Material.IRON);
    public static final Fence FENCE_PICKET_OAK = new Fence(Material.WOOD, MapColor.WOOD);
    public static final Fence FENCE_PICKET_BIRCH = new Fence(Material.WOOD, MapColor.WOOD);
    public static final Fence FENCE_PICKET_ACACIA = new Fence(Material.WOOD, MapColor.WOOD);
    public static final Fence FENCE_PICKET_WHITE = new Fence(Material.WOOD, MapColor.WOOD);
    public static final Fence FENCE_CROSS_OAK = new Fence(Material.WOOD, MapColor.WOOD);
    public static final Fence FENCE_CROSS_BIRCH = new Fence(Material.WOOD, MapColor.WOOD);
    public static final Fence FENCE_CROSS_ACACIA = new Fence(Material.WOOD, MapColor.WOOD);
    public static final Fence FENCE_CROSS_WHITE = new Fence(Material.WOOD, MapColor.WOOD);
    public static final Fence FENCE_BOLLARD_OAK = new Fence(Material.WOOD, MapColor.WOOD);
    public static final Fence FENCE_BOLLARD_BIRCH = new Fence(Material.WOOD, MapColor.WOOD);
    public static final Fence FENCE_BOLLARD_ACACIA = new Fence(Material.WOOD, MapColor.WOOD);
    public static final Fence FENCE_BOLLARD_WHITE = new Fence(Material.WOOD, MapColor.WOOD);
    public static final Fence FENCE_STICK_OAK = new Fence(Material.WOOD, MapColor.WOOD);
    public static final Fence FENCE_STICK_BIRCH = new Fence(Material.WOOD, MapColor.WOOD);
    public static final Fence FENCE_STICK_ACACIA = new Fence(Material.WOOD, MapColor.WOOD);
    public static final Fence FENCE_STICK_WHITE = new Fence(Material.WOOD, MapColor.WOOD);
    public static final FenceGate FENCE_PICKET_GATE_OAK = new FenceGate(EnumType.OAK);
    public static final FenceGate FENCE_PICKET_GATE_BIRCH = new FenceGate(EnumType.OAK);
    public static final FenceGate FENCE_PICKET_GATE_ACACIA = new FenceGate(EnumType.OAK);
    public static final FenceGate FENCE_PICKET_GATE_WHITE = new FenceGate(EnumType.OAK);
    public static final FenceGate FENCE_CROSS_GATE_OAK = new FenceGate(EnumType.OAK);
    public static final FenceGate FENCE_CROSS_GATE_BIRCH = new FenceGate(EnumType.OAK);
    public static final FenceGate FENCE_CROSS_GATE_ACACIA = new FenceGate(EnumType.OAK);
    public static final FenceGate FENCE_CROSS_GATE_WHITE = new FenceGate(EnumType.OAK);
    public static final FenceGate FENCE_BOLLARD_GATE_OAK = new FenceGate(EnumType.OAK);
    public static final FenceGate FENCE_BOLLARD_GATE_BIRCH = new FenceGate(EnumType.OAK);
    public static final FenceGate FENCE_BOLLARD_GATE_ACACIA = new FenceGate(EnumType.OAK);
    public static final FenceGate FENCE_BOLLARD_GATE_WHITE = new FenceGate(EnumType.OAK);
    public static final FenceGate FENCE_STICK_GATE_OAK = new FenceGate(EnumType.OAK);
    public static final FenceGate FENCE_STICK_GATE_BIRCH = new FenceGate(EnumType.OAK);
    public static final FenceGate FENCE_STICK_GATE_ACACIA = new FenceGate(EnumType.OAK);
    public static final FenceGate FENCE_STICK_GATE_WHITE = new FenceGate(EnumType.OAK);
    public static final Concrete CONCRETE_WHITE = new Concrete(Material.ROCK);
    public static final Concrete CONCRETE_ORANGE = new Concrete(Material.ROCK);
    public static final Concrete CONCRETE_MAGENTA = new Concrete(Material.ROCK);
    public static final Concrete CONCRETE_LIGHT_BLUE = new Concrete(Material.ROCK);
    public static final Concrete CONCRETE_YELLOW = new Concrete(Material.ROCK);
    public static final Concrete CONCRETE_LIME = new Concrete(Material.ROCK);
    public static final Concrete CONCRETE_PINK = new Concrete(Material.ROCK);
    public static final Concrete CONCRETE_GRAY = new Concrete(Material.ROCK);
    public static final Concrete CONCRETE_SILVER = new Concrete(Material.ROCK);
    public static final Concrete CONCRETE_CYAN = new Concrete(Material.ROCK);
    public static final Concrete CONCRETE_PURPLE = new Concrete(Material.ROCK);
    public static final Concrete CONCRETE_BLUE = new Concrete(Material.ROCK);
    public static final Concrete CONCRETE_BROWN = new Concrete(Material.ROCK);
    public static final Concrete CONCRETE_GREEN = new Concrete(Material.ROCK);
    public static final Concrete CONCRETE_RED = new Concrete(Material.ROCK);
    public static final Concrete CONCRETE_BLACK = new Concrete(Material.ROCK);
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
}