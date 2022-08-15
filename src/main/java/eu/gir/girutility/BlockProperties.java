package eu.gir.girutility;

import java.util.List;

import eu.gir.girutility.blocks.SlabBase;
import eu.gir.girutility.blocks.Stairs;
import eu.gir.girutility.blocks.Wall;
import eu.gir.girutility.items.SlabItem;

public class BlockProperties extends BlockDefinitons {
    
    private String resource;
    private float hardness;
    private String material;
    private String soundtype;
    private int opacity;
    private List<String> state;
    
    public void addToList() {
        String name = "Test";
        for (String states : state) {
            switch (states) {
                case "stair":
                    Stairs stair = new Stairs(null, null);
                    break;
                case "slab":
                    SlabBase slabHalf = new SlabBase.HalfSlab(slabHalf, slabHalf, null);
                    SlabBase slabDouble = new SlabBase.DoubleSlab(slabHalf, null);
                    SlabItem slabItem = new SlabItem(slabHalf, slabHalf, slabDouble);
                case "wall":
                    Wall wall = new Wall(null);
                default:
                    break;
            }
        }
    }
    
}
