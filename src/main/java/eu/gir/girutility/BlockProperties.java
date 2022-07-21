package eu.gir.girutility;

import java.util.List;

public class BlockProperties extends BlockDefinitons {
    
    private String resource;
    private float hardness;
    private String material;
    private String soundtype;
    private int opacity;
    private List<String> state;
    
    public String getResource() {
        return resource;
    }
    
    public float getHardness() {
        return hardness;
    }
    
    public String getMaterial() {
        return material;
    }
    
    public String getSoundType() {
        return soundtype;
    }
    
    public int getOpacity() {
        return opacity;
    }
    
    public List<String> getState() {
        return state;
    }
}
