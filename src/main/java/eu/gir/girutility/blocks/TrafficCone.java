package eu.gir.girutility.blocks;

import java.util.List;

import javax.annotation.Nullable;

import eu.gir.girutility.init.GIRTabs;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class TrafficCone extends Block {

	private static final AxisAlignedBB COLLISION_BOX = new AxisAlignedBB(0.125, -0.25, 0.125, 0.875, 18 * 0.0625 - 0.25,
			0.875);
	private static final AxisAlignedBB SELECTION_BOX = new AxisAlignedBB(0.0625, -0.25, 0.0625, 0.9375,
			16 * 0.0625 - 0.25, 0.9375);

	public TrafficCone() {
		super(Material.CLAY, MapColor.ORANGE_STAINED_HARDENED_CLAY);
		this.setHardness(0.5F);
		setCreativeTab(GIRTabs.tab);

	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return SELECTION_BOX;
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
		Block.addCollisionBoxToList(pos, entityBox, collidingBoxes, COLLISION_BOX);
	}

}
