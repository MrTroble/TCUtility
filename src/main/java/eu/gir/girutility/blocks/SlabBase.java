package eu.gir.girutility.blocks;

import eu.gir.girutility.init.GIRTabs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

public class SlabBase extends BlockSlab {
    
    Block half;
    public static final PropertyEnum<Variant> VARIANT = PropertyEnum.<Variant>create("variant", Variant.class);
    
    public SlabBase(Material material, BlockSlab half) {
        super (material);
        setCreativeTab(GIRTabs.tab);
        this.useNeighborBrightness = !this.isDouble();
        IBlockState state = this.blockState.getBaseState().withProperty(VARIANT, Variant.DEFAULT);
        if (!this.isDouble()) state = state.withProperty(HALF, EnumBlockHalf.BOTTOM);
        this.half = half;
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
        IBlockState state = this.blockState.getBaseState().withProperty(VARIANT, Variant.DEFAULT);
        if (!this.isDouble()) {
            EnumBlockHalf value = EnumBlockHalf.BOTTOM;
            if ((meta & 1) !=0 ) value = EnumBlockHalf.TOP;
            state = state.withProperty(HALF, value);
        }
        return state;
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = 0;
        if (!this.isDouble() && state.getValue(HALF) == EnumBlockHalf.TOP) meta |= 1;
        return meta;
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        if (!this.isDouble()) return new BlockStateContainer(this, new IProperty[] {VARIANT, HALF});
        else return new BlockStateContainer(this, new IProperty[] {VARIANT});
    }
    
    @Override
    public IProperty<?> getVariantProperty() {
        return VARIANT;
    }
    
    public static enum Variant implements IStringSerializable {
        DEFAULT;
        
        @Override
        public String getName() {
            return "default";
        }
    }

    @Override
    public String getUnlocalizedName(int meta) {
        return super.getUnlocalizedName();
    }

    @Override
    public Comparable<?> getTypeForItem(ItemStack stack) {
        return Variant.DEFAULT;
    }

    @Override
    public boolean isDouble() {
        return false;
    }

}
