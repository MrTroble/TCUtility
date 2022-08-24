package eu.gir.girutility.blocks;

import eu.gir.girutility.init.GIRTabs;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class Slab extends Block {
    
    public static final PropertyEnum<Slab.EnumBlockHalf> HALF = PropertyEnum.<Slab.EnumBlockHalf>create("half", Slab.EnumBlockHalf.class);
    protected static final AxisAlignedBB AABB_BOTTOM_HALF = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
    protected static final AxisAlignedBB AABB_TOP_HALF = new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D);

    public Slab(BlockCreateInfo blockInfo) {
        super(blockInfo.material);
        setHardness(blockInfo.hardness);
        setSoundType(blockInfo.soundtype);
        setLightOpacity(blockInfo.opacity);
        IBlockState iblockstate = this.blockState.getBaseState();
        iblockstate = iblockstate.withProperty(HALF, Slab.EnumBlockHalf.BOTTOM);
        this.setCreativeTab(GIRTabs.tab);
        this.setDefaultState(iblockstate);
    }
    
    public Slab(Material material) {
        super(material);
        IBlockState iblockstate = this.blockState.getBaseState();
        iblockstate = iblockstate.withProperty(HALF, Slab.EnumBlockHalf.BOTTOM);
        this.setCreativeTab(GIRTabs.tab);
        this.setDefaultState(iblockstate);
    }
    
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
            return state.getValue(HALF) == Slab.EnumBlockHalf.TOP ? AABB_TOP_HALF : AABB_BOTTOM_HALF;
    }
    
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }
    
    public boolean isFullCube(IBlockState state) {
        return false;
    }
    
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        @SuppressWarnings("deprecation")
        IBlockState iblockstate = super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(HALF, Slab.EnumBlockHalf.BOTTOM);
            return facing != EnumFacing.DOWN && (facing == EnumFacing.UP || (double)hitY <= 0.5D) ? iblockstate : iblockstate.withProperty(HALF, Slab.EnumBlockHalf.TOP);
    }
    
    public IBlockState getStateFromMeta(int meta) {
        IBlockState iblockstate = this.getDefaultState();
        iblockstate = iblockstate.withProperty(HALF, (meta & 8) == 0 ? Slab.EnumBlockHalf.BOTTOM : Slab.EnumBlockHalf.TOP);
        return iblockstate;
    }
    
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        if (state.getValue(HALF) == Slab.EnumBlockHalf.TOP) {
            i |= 8;
        }
        return i;
    }
    
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {HALF});
    } 
    
    public static enum EnumBlockHalf implements IStringSerializable {
        TOP,
        BOTTOM;
        
        public String toString() {
            return this.getName();
        }
        
        public String getName() {
            return this == BOTTOM ? "bottom" : "top";
        }
    }
}
