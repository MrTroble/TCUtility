package eu.gir.girutility;

import java.util.HashMap;
import java.util.List;

import eu.gir.girutility.blocks.BlockCreateInfo;
import eu.gir.girutility.blocks.DefaultBlock;
import eu.gir.girutility.blocks.SlabBase;
import eu.gir.girutility.blocks.Stairs;
import eu.gir.girutility.blocks.Wall;
import eu.gir.girutility.items.ItemSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockProperties {
    
    private String resource;
    private float hardness;
    private String material;
    private String soundtype;
    private int opacity;
    private List<String> state;
    
    public static final HashMap<String, Material> materialTable = translateTableMaterial();
    public static final HashMap<String, SoundType> soundTable = translateTableSoundType();
    
    public static HashMap<String, Material> translateTableMaterial() {
        HashMap<String, Material> translateTable = new HashMap<>();
        translateTable.put("grass", Material.GRASS);
        translateTable.put("ground", Material.GROUND);
        translateTable.put("wood", Material.WOOD);
        translateTable.put("rock", Material.ROCK);
        translateTable.put("iron", Material.IRON);
        translateTable.put("cloth", Material.CLOTH);
        translateTable.put("sand", Material.SAND);
        translateTable.put("glass", Material.GLASS);
        translateTable.put("ice", Material.ICE);
        translateTable.put("packed_ice", Material.PACKED_ICE);
        translateTable.put("snow", Material.SNOW);
        translateTable.put("clay", Material.CLAY);
        return translateTable;   
    }
    
    public static HashMap<String, SoundType> translateTableSoundType() {
        HashMap<String, SoundType> translateTable = new HashMap<>();
        translateTable.put("wood", SoundType.WOOD);
        translateTable.put("ground", SoundType.GROUND);
        translateTable.put("stone", SoundType.STONE);
        translateTable.put("metal", SoundType.METAL);
        translateTable.put("glass", SoundType.GLASS);
        translateTable.put("cloth", SoundType.CLOTH);
        translateTable.put("sand", SoundType.SAND);
        translateTable.put("snow", SoundType.SNOW);
        translateTable.put("ladder", SoundType.LADDER);
        translateTable.put("slime", SoundType.SLIME);
        return translateTable;
    }
    
    public BlockCreateInfo getBlockInfo() {
        Material mat = materialTable.get(material);
        SoundType sound = soundTable.get(soundtype);
        if (mat == null) {
            GirutilityMain.LOG.error("The given material name [%s] is not valid.", material);
            return null;
        }
        if (sound == null) {
            GirutilityMain.LOG.error("The given sound type [%s] is not valid.", soundtype);
            return null;
        }
        BlockCreateInfo info = new BlockCreateInfo(mat, hardness, sound, opacity);
        return info;
    }
    
    public void addToList() {
        BlockCreateInfo blockInfo = getBlockInfo();
        DefaultBlock defaultBlock = new DefaultBlock(blockInfo);
        
        for (String states : state) {
            switch (states) {
                case "stair":
                    Stairs stair = new Stairs(defaultBlock.getDefaultState());
                    break;
                case "slab":
                    SlabBase slabHalf = new SlabBase.HalfSlab(slabHalf, slabHalf, blockInfo);
                    SlabBase slabDouble = new SlabBase.DoubleSlab(slabHalf, blockInfo);
                    ItemSlab slabItem = new ItemSlab(slabHalf, slabHalf, slabDouble);
                    break;
                case "wall":
                    Wall wall = new Wall(blockInfo);
                    break;
                default:
                    GirutilityMain.LOG.error("The given state [%s] is not valid.", state);
                    break;
            }
        }
    }
    
}
