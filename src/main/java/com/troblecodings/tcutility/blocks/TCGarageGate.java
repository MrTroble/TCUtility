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

    protected static final AxisAlignedBB NORTH = new AxisAlignedBB(0, 0, 0, 1, 1, 1);
    protected static final AxisAlignedBB EAST = new AxisAlignedBB(0, 0, 0, 1, 1, 1);
    protected static final AxisAlignedBB SOUTH = new AxisAlignedBB(0, 0, 0, 1, 1, 1);
    protected static final AxisAlignedBB WEST = new AxisAlignedBB(0, 0, 0, 1, 1, 1);

    public TCGarageGate(BlockCreateInfo blockInfo) {
        super(blockInfo);
        this.setCreativeTab(null);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        state = state.getActualState(source, pos);
        final EnumFacing enumFacing = state.getValue(FACING);
        switch (enumFacing) {
            case EAST:
            default:
                return EAST;
            case SOUTH:
                return SOUTH;
            case WEST:
                return WEST;
            case NORTH:
                return NORTH;
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

    public void changeState(World worldIn, BlockPos pos, IBlockState state) {
        if (state.getValue(TCGarageDoor.OPEN).booleanValue()) {
            for (int i = 1; i < 10; i++) {
                final BlockPos posDown = pos.down(i);
                final IBlockState blockState = worldIn.getBlockState(posDown);

                if (!blockState.getBlock().equals(state.getBlock()))
                    break;
                worldIn.setBlockToAir(posDown);
            }
        } else {
            for (int i = 1; i < 10; i++) {
                final BlockPos posDown = pos.down(i);
                final IBlockState blockState = worldIn.getBlockState(posDown);

                if (!isAir(blockState, worldIn, posDown))
                    break;

                worldIn.setBlockState(posDown,
                        this.getDefaultState().withProperty(FACING, state.getValue(FACING)));
            }
        }
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

            if (stateDown.getBlock().equals(this) || stateUp.getBlock().equals(this)
                    || isGarageBlock(stateUp)) {
                worldIn.setBlockToAir(posUp);
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
