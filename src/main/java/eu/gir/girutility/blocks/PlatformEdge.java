package eu.gir.girutility.blocks;

import eu.gir.girutility.init.GIRTabs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import scala.reflect.internal.Trees.If;

public class PlatformEdge extends Block {
	
	public static final PropertyDirection FACING = BlockHorizontal.FACING;

	public PlatformEdge() {
		super(Material.ROCK);
		setDefaultState(getDefaultState().withProperty(FACING, EnumFacing.NORTH));
		setCreativeTab(GIRTabs.tab);
	}
	
	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
		return layer.equals(BlockRenderLayer.CUTOUT_MIPPED);
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getIndex();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
	}
	
}
