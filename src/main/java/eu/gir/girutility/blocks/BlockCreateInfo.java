package eu.gir.girutility.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockCreateInfo {
    
    public final Material material;
    public final float hardness;
    public final SoundType soundtype;
    public final int opacity;
    
    public BlockCreateInfo(final Material material, final float hardness, final SoundType soundtype, final int opacity) {
        this.material = material;
        this.hardness = hardness;
        this.soundtype = soundtype;
        this.opacity = opacity;
    }

}
