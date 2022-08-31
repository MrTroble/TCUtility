package eu.gir.girutility.init;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import com.google.gson.Gson;

import eu.gir.girutility.BlockDefinitons;
import eu.gir.girutility.GirutilityMain;
import eu.gir.girutility.blocks.BigDoor;
import eu.gir.girutility.blocks.Bin;
import eu.gir.girutility.blocks.Concrete;
import eu.gir.girutility.blocks.Crate;
import eu.gir.girutility.blocks.Door;
import eu.gir.girutility.blocks.Fence;
import eu.gir.girutility.blocks.FenceGate;
import eu.gir.girutility.blocks.Ladder;
import eu.gir.girutility.blocks.Lantern;
import eu.gir.girutility.blocks.PlatformEdge;
import eu.gir.girutility.blocks.TrafficCone;
import eu.gir.girutility.blocks.TrapDoor;
import eu.gir.girutility.blocks.WoodenWindow;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class GIRBlocks {
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
    
    public static final Concrete TEST_CONCRETE = new Concrete(Material.ROCK);
    
    //public static final Fence TEST_FENCE = new Fence(Material.WOOD, MapColor.WOOD);
    //public static final FenceGate TEST_FENCEGATE = new FenceGate(BlockPlanks.EnumType.OAK);
    //public static final Slab TEST_SLAB = new Slab(Material.ROCK);
    
    public static ArrayList<Block> blocksToRegister = new ArrayList<>();

    public static void init() {
        Field[] fields = GIRBlocks.class.getFields();
        for (Field field : fields) {
            int modifiers = field.getModifiers();
            if (Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers)
                    && Modifier.isPublic(modifiers)) {
                String name = field.getName().toLowerCase();
                try {
                    Block block = (Block) field.get(null);
                    block.setRegistryName(new ResourceLocation(GirutilityMain.MODID, name));
                    block.setUnlocalizedName(name);
                    blocksToRegister.add(block);
                    if (block instanceof ITileEntityProvider) {
                        ITileEntityProvider provider = (ITileEntityProvider) block;
                        try {
                            Class<? extends TileEntity> tileclass = provider
                                    .createNewTileEntity(null, 0).getClass();
                            TileEntity.register(tileclass.getSimpleName().toLowerCase(), tileclass);
                        } catch (NullPointerException ex) {
                            GirutilityMain.LOG.trace(
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
    public static void registerBlock(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> registry = event.getRegistry();
        blocksToRegister.forEach(registry::register);
    }

    @SubscribeEvent
    public static void registerItem(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
            blocksToRegister.forEach(block -> registry
                    .register(new ItemBlock(block).setRegistryName(block.getRegistryName())));
    }

    private static String toString(final List<String> text) {
        final StringBuilder stringbuilder = new StringBuilder();
        text.forEach(string -> {
            stringbuilder.append(string);
            stringbuilder.append("\n");
        });
        return stringbuilder.toString();
    }

    public static Optional<Path> getRessourceLocation(String location) {
        final URL url = GIRBlocks.class.getResource(location);
        try {
            if (url != null) {
                final URI uri = url.toURI();
                
                if ("file".equals(uri.getScheme())) {
                    if (!location.startsWith("/")) {
                        location = "/" + location;
                    }
                    final URL resource = GIRBlocks.class.getResource(location);
                    if (resource == null) {
                        return Optional.empty();
                    }
                    return Optional.of(Paths.get(resource.toURI()));
                } else {
                    if (!"jar".equals(uri.getScheme())) {
                        return Optional.empty();
                    }
                    try (final FileSystem filesystem = FileSystems.newFileSystem(uri, Collections.emptyMap())) {
                        return Optional.of(filesystem.getPath(location));
                    }
                }
            }
        } catch (IOException | URISyntaxException e1) {
            e1.printStackTrace();
        }
        return Optional.empty();
    }

    public static Map<String, String> readFiles(String location) {
        final Optional<Path> loc = getRessourceLocation(location);
        if (loc.isPresent()) {
            final Path path = loc.get();
            try {
                final Stream<Path> inputs = Files.list(path);
                final Map<String, String> files = new HashMap<>();
                inputs.forEach(file -> {
                    try {
                        final List<String> text = Files.readAllLines(file);
                        final String content = toString(text);
                        final String name = file.getFileName().toString();
                        files.put(name, content);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                inputs.close();
                return files;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    public static Map<String, BlockDefinitons> getFromJson(final String directory) {
        final Gson gson = new Gson();
        final Map<String, String> entrySet = readFiles(directory);
        final Map<String, BlockDefinitons> content = new HashMap<>();
        if (entrySet != null) {
            entrySet.forEach((filename, file) -> {
                final BlockDefinitons json = gson.fromJson(file, BlockDefinitons.class);
                content.put(filename, json);
            });
        }
        return content;
    }
    
    /*public static void register() {
        final Map<String, BlockDefinitons> fromJsonMap = getFromJson("/assets/" + GirutilityMain.MODID + "/blockdefinitions");
        fromJsonMap.forEach((filename, content) -> {
            content -> {
                content.getName().forEach(name, property) -> {
                }
            }
        });
    }*/
    
}