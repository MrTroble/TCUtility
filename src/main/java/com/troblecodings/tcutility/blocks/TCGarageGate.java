package com.troblecodings.tcutility.blocks;

import com.troblecodings.tcutility.utils.BlockCreateInfo;

import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class TCGarageGate extends TCCube {

    public static final PropertyDirection FACING = PropertyDirection.create("facing",
            EnumFacing.Plane.HORIZONTAL);

    private final AxisAlignedBB northBB = new AxisAlignedBB(getIndexBox(0) * 0.0625,
            getIndexBox(1) * 0.0625, getIndexBox(2) * 0.0625, getIndexBox(3) * 0.0625,
            getIndexBox(4) * 0.0625, getIndexBox(5) * 0.0625);
    private final AxisAlignedBB eastBB = new AxisAlignedBB(1 - getIndexBox(2) * 0.0625,
            getIndexBox(1) * 0.0625, getIndexBox(0) * 0.0625, 1 - getIndexBox(5) * 0.0625,
            getIndexBox(4) * 0.0625, getIndexBox(3) * 0.0625);
    private final AxisAlignedBB southBB = new AxisAlignedBB(getIndexBox(0) * 0.0625,
            getIndexBox(1) * 0.0625, 1 - getIndexBox(2) * 0.0625, getIndexBox(3) * 0.0625,
            getIndexBox(4) * 0.0625, 1 - getIndexBox(5) * 0.0625);
    private final AxisAlignedBB westBB = new AxisAlignedBB(getIndexBox(2) * 0.0625,
            getIndexBox(1) * 0.0625, getIndexBox(0) * 0.0625, getIndexBox(5) * 0.0625,
            getIndexBox(4) * 0.0625, getIndexBox(3) * 0.0625);

    public TCGarageGate(BlockCreateInfo blockInfo) {
        super(blockInfo);
        this.setCreativeTab(null);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    @SuppressWarnings("deprecation")
    @Override
    public AxisAlignedBB getBoundingBox(final IBlockState finalstate, final IBlockAccess source,
            final BlockPos pos) {
        IBlockState state = this.getActualState(finalstate, source, pos);
        final EnumFacing enumFacing = state.getValue(FACING);
        switch (enumFacing) {
            case EAST:
            default:
                return eastBB;
            case SOUTH:
                return southBB;
            case WEST:
                return westBB;
            case NORTH:
                return northBB;
        }
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
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    private boolean isGarageBlock(IBlockState blockState) {
        return this.getRegistryName().toString()
                .contains(blockState.getBlock().getRegistryName().toString());
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state,
            EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY,
            float hitZ) {
        for (int i = 1; i < 10; i++) {
            final BlockPos posUp = pos.up(i);
            IBlockState stateUp = worldIn.getBlockState(posUp);

            if (isGarageBlock(stateUp) && stateUp.getProperties().containsKey(TCGarageDoor.OPEN)) {
                stateUp = stateUp.cycleProperty(TCGarageDoor.OPEN);
                TCGarageDoor tcGarageDoor = (TCGarageDoor) stateUp.getBlock();
                tcGarageDoor.changeState(worldIn, posUp, stateUp);
                tcGarageDoor.changeNeighbor(worldIn, posUp, stateUp);

                worldIn.setBlockState(posUp, stateUp, 10);
                worldIn.markBlockRangeForRenderUpdate(pos, posUp);
                worldIn.playEvent(playerIn,
                        stateUp.getValue(TCGarageDoor.OPEN).booleanValue()
                                ? tcGarageDoor.getOpenSound()
                                : tcGarageDoor.getCloseSound(),
                        posUp, 0);
            }
        }
        return true;
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state,
            EntityPlayer player) {
        for (int i = 1; i < 10; i++) {
            final BlockPos posDown = pos.down(i);
            final BlockPos posUp = pos.up(i);
            final IBlockState stateDown = worldIn.getBlockState(posDown);
            final IBlockState stateUp = worldIn.getBlockState(posUp);

            if (stateUp.getBlock().equals(this) || isGarageBlock(stateUp)) {
                worldIn.setBlockToAir(posUp);
            }

            if (stateDown.getBlock().equals(this)) {
                worldIn.setBlockToAir(posDown);
            }
        }
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos,
            EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

}
