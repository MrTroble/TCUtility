package eu.gir.girutility.init;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.gir.girutility.GirutilityMain;
import eu.gir.girutility.blocks.Bin;
import eu.gir.girutility.blocks.Crate;
import eu.gir.girutility.blocks.Door;
import eu.gir.girutility.blocks.DoubleSlab;
import eu.gir.girutility.blocks.HalfSlab;
import eu.gir.girutility.blocks.PlatformEdge;
import eu.gir.girutility.blocks.Stairs;
import eu.gir.girutility.blocks.TrafficCone;
import eu.gir.girutility.blocks.TrapDoor;
import eu.gir.girutility.blocks.Wall;
import eu.gir.girutility.blocks.WoodenWindow;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
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
    public static final BlockSlab SLAB1_HALF = new HalfSlab(Material.ROCK);
    public static final BlockSlab SLAB1_DOUBLE = new DoubleSlab(Material.ROCK);
    public static final Stairs STAIR1 = new Stairs(Blocks.CONCRETE.getDefaultState());
    public static final Wall WALL1 = new Wall(Material.ROCK);
    public static final Door DOOR1 = new Door(Material.WOOD);


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
    
    public static Optional<Path> getRessourceLocation() {
        final URL url = GIRBlocks.class.getResource("/assets/.mcassetsroot");
        try {
            if (url != null) {
                final URI uri = url.toURI();
                Path path;

                if ("file".equals(uri.getScheme())) {
                    path = Paths.get(GIRBlocks.class.getResource("/assets/girutility/xyz").toURI());
                } else {
                    if (!"jar".equals(uri.getScheme())) {
                        return Optional.empty();
                    }

                    final FileSystem filesystem = FileSystems.newFileSystem(uri,
                            Collections.emptyMap());
                    path = filesystem.getPath("/assets/girutility/xyz");
                    return Optional.of(path);
                }
            }
        } catch (IOException | URISyntaxException e1) {
            e1.printStackTrace();
        }
        return Optional.empty();
    }
}