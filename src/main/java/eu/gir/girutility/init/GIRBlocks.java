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

import eu.gir.girutility.BlockProperties;
import eu.gir.girutility.GirutilityMain;
import eu.gir.girutility.blocks.BigDoor;
import eu.gir.girutility.blocks.Bin;
import eu.gir.girutility.blocks.Crate;
import eu.gir.girutility.blocks.Door;
import eu.gir.girutility.blocks.Ladder;
import eu.gir.girutility.blocks.Lantern;
import eu.gir.girutility.blocks.PlatformEdge;
import eu.gir.girutility.blocks.TrafficCone;
import eu.gir.girutility.blocks.TrapDoor;
import eu.gir.girutility.blocks.WoodenWindow;
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
    //public static final SlabBase SLAB1_HALF = new SlabBase.HalfSlab(Material.ROCK, GIRBlocks.SLAB1_HALF, GIRBlocks.SLAB1_DOUBLE);
    //public static final SlabBase SLAB1_DOUBLE = new SlabBase.DoubleSlab(Material.ROCK, GIRBlocks.SLAB1_HALF);
    //public static final Stairs STAIR1 = new Stairs(Blocks.CONCRETE.getDefaultState());
    //public static final Wall WALL1 = new Wall(Material.ROCK, 1.0f, SoundType.STONE, 0);
    public static final Door DOOR1_BLOCK = new Door(Material.WOOD);
    public static final BigDoor BIGDOOR1_BLOCK = new BigDoor(Material.WOOD);
    public static final Door DOOR_JAIL_BLOCK = new Door(Material.IRON);
    public static final Lantern LANTERN = new Lantern();
    public static final Ladder LADDER_IRON = new Ladder(Material.IRON);
    public static final Ladder LADDER_RUSTY = new Ladder(Material.IRON);
    public static final Ladder LADDER_ROOF = new Ladder(Material.IRON);

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
                Path path;
                if ("file".equals(uri.getScheme())) {
                    path = Paths.get(GIRBlocks.class.getResource(location).toURI());
                    return Optional.of(path);
                } else {
                    if (!"jar".equals(uri.getScheme())) {
                        return Optional.empty();
                    }
                    final FileSystem filesystem = FileSystems.newFileSystem(uri,
                            Collections.emptyMap());
                    path = filesystem.getPath(location);
                    return Optional.of(path);
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
    
    public static Map<String, BlockProperties> getFromJson(final String directory) {
        final Gson gson = new Gson();
        final Map<String, String> entrySet = readFiles(directory);
        final Map<String, BlockProperties> content = new HashMap<>();
        if (entrySet != null) {
            entrySet.forEach((filename, file) -> {
                final BlockProperties json = gson.fromJson(file, BlockProperties.class);
                content.put(filename, json);
            });
        }
        return content;
    }
    
    /* - Zusammenführen von Ordner mit Jsons und Auslesen der Infos
     * - Einmal registrieren für Stair, Slab und Wall (gefiltert auslesen), evtl getrennt registern
     * - Name auslesen und zum registrieren + Block [Purple_Glas_Stair]
     * - An BlockCreationInfo Methode die Blockdefinitions übergeben
     */
    public static void register() {
        Map<String, BlockProperties> fromJson = getFromJson("/assets/" + GirutilityMain.MODID + "/blockdefinitions");
    }
    
}