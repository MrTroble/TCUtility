package eu.gir.girutility.blocks;

import eu.gir.girutility.init.GIRTabs;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class Bin extends Block {

	private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(2 * 0.0625, 0.0, 2 * 0.0625, 14 * 0.0625,
			13 * 0.0625, 14 * 0.0625);

	public Bin() {
		super(Material.ANVIL);
		this.setSoundType(SoundType.ANVIL);
		this.setHardness(0.5F);
		setCreativeTab(GIRTabs.tab);
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
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return BOUNDING_BOX;
	}

}
