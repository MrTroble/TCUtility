package com.troblecodings.tcutility.blocks;

import com.troblecodings.tcutility.init.TCTabs;
import com.troblecodings.tcutility.utils.BlockCreateInfo;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class TCGarageDoor extends TCCube {

    public static final PropertyDirection FACING =
            PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyBool OPEN = PropertyBool.create("open");

    public TCGarageDoor(final BlockCreateInfo blockInfo) {
        super(blockInfo);
        this.setCreativeTab(TCTabs.DOORS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH)
                .withProperty(OPEN, Boolean.valueOf(true)));
    }

    @Override
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source,
            final BlockPos pos) {
        return FULL_BLOCK_AABB;
    }

    @Override
    public boolean isOpaqueCube(final IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(final IBlockState state) {
        return false;
    }

    public int getCloseSound() {
        return this.blockMaterial.equals(Material.IRON) ? 1011 : 1012;
    }

    public int getOpenSound() {
        return this.blockMaterial.equals(Material.IRON) ? 1005 : 1006;
    }

    public void changeState(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (state.getValue(OPEN).booleanValue()) {
            for (int i = 1; i < 10; i++) {
                final BlockPos posDown = pos.down(i);
                final IBlockState blockState = worldIn.getBlockState(posDown);

                if (!isGateBlock(blockState)) {
                    break;
                }
                worldIn.setBlockToAir(posDown);
            }
        } else {
            for (int i = 1; i < 10; i++) {
                final BlockPos posDown = pos.down(i);
                final IBlockState blockState = worldIn.getBlockState(posDown);

                if (!isAir(blockState, worldIn, posDown)) {
                    break;
                }

                worldIn.setBlockState(posDown,
                        Block.getBlockFromName(this.getRegistryName().toString() + "_gate")
                                .getDefaultState().withProperty(FACING, state.getValue(FACING)));
            }
        }
    }

    public void changeNeighbor(final World worldIn, final BlockPos pos, final IBlockState state) {
        final EnumFacing facing = state.getValue(FACING);
        if (facing.equals(EnumFacing.NORTH) || facing.equals(EnumFacing.SOUTH)) {
            for (int i = 1; i < 10; i++) {
                final BlockPos posEast = pos.east(i);
                IBlockState stateEast = worldIn.getBlockState(posEast);

                if (stateEast.getBlock() instanceof TCGarageDoor) {
                    stateEast = stateEast.cycleProperty(OPEN);
                    changeState(worldIn, posEast, stateEast);
                    worldIn.setBlockState(posEast, stateEast, 10);
                } else {
                    break;
                }
            }
            for (int i = 1; i < 10; i++) {
                final BlockPos posWest = pos.west(i);
                IBlockState stateWest = worldIn.getBlockState(posWest);

                if (stateWest.getBlock() instanceof TCGarageDoor) {
                    stateWest = stateWest.cycleProperty(OPEN);
                    changeState(worldIn, posWest, stateWest);
                    worldIn.setBlockState(posWest, stateWest, 10);
                } else {
                    break;
                }
            }
        } else if (facing.equals(EnumFacing.WEST) || facing.equals(EnumFacing.EAST)) {
            for (int i = 1; i < 10; i++) {
                final BlockPos posNorth = pos.north(i);
                IBlockState stateNorth = worldIn.getBlockState(posNorth);

                if (stateNorth.getBlock() instanceof TCGarageDoor) {
                    stateNorth = stateNorth.cycleProperty(OPEN);
                    changeState(worldIn, posNorth, stateNorth);
                    worldIn.setBlockState(posNorth, stateNorth, 10);
                } else {
                    break;
                }
            }
            for (int i = 1; i < 10; i++) {
                final BlockPos posSouth = pos.south(i);
                IBlockState stateSouth = worldIn.getBlockState(posSouth);

                if (stateSouth.getBlock() instanceof TCGarageDoor) {
                    stateSouth = stateSouth.cycleProperty(OPEN);
                    changeState(worldIn, posSouth, stateSouth);
                    worldIn.setBlockState(posSouth, stateSouth, 10);
                } else {
                    break;
                }
            }
        }
    }

    private boolean isGateBlock(final IBlockState blockState) {
        return blockState.getBlock() instanceof TCGarageGate;
    }

    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos,
            final IBlockState state, final EntityPlayer playerIn, final EnumHand hand,
            final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        if (this.blockMaterial.equals(Material.IRON))
            return false;
        final BlockPos blockPos = pos;
        IBlockState iBlockState = state;
        if (iBlockState.getBlock() instanceof TCGarageDoor) {
            iBlockState = iBlockState.cycleProperty(OPEN);
            changeState(worldIn, blockPos, iBlockState);
            changeNeighbor(worldIn, pos, iBlockState);
        }

        worldIn.setBlockState(blockPos, iBlockState, 10);
        worldIn.markBlockRangeForRenderUpdate(pos, pos);
        worldIn.playEvent(playerIn, iBlockState.getValue(OPEN).booleanValue() ? this.getOpenSound()
                : this.getCloseSound(), pos, 0);
        return true;
    }

    @Override
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos,
            final Block blockIn, final BlockPos fromPos) {
        if (worldIn.isRemote)
            return;
        boolean flag = worldIn.isBlockPowered(pos);

        if (flag || blockIn.getDefaultState().canProvidePower()) {
            boolean flag1 = state.getValue(OPEN).booleanValue();

            if (flag1 != flag) {
                IBlockState blockState = state.withProperty(OPEN, Boolean.valueOf(flag));
                worldIn.setBlockState(pos, blockState, 2);
                changeState(worldIn, pos, blockState);
                changeNeighbor(worldIn, pos, blockState);
                worldIn.markBlockRangeForRenderUpdate(pos, pos);
                worldIn.playEvent((EntityPlayer) null,
                        state.getValue(OPEN).booleanValue() ? this.getOpenSound()
                                : this.getCloseSound(),
                        pos, 0);
            }
        }
    }

    @Override
    public void onBlockHarvested(final World worldIn, final BlockPos pos, final IBlockState state,
            final EntityPlayer player) {
        for (int i = 1; i < 10; i++) {
            final BlockPos posDown = pos.down(i);
            final IBlockState blockState = worldIn.getBlockState(posDown);

            if (!isGateBlock(blockState)) {
                break;
            }
            worldIn.setBlockToAir(posDown);
        }
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state,
            final BlockPos pos, final EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public IBlockState withRotation(final IBlockState state, final Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public IBlockState getStateForPlacement(final World world, final BlockPos pos,
            final EnumFacing facing, final float hitX, final float hitY, final float hitZ,
            final int meta, final EntityLivingBase placer, final EnumHand hand) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing())
                .withProperty(OPEN, Boolean.valueOf(true));
    }

    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState()
                .withProperty(FACING, EnumFacing.getHorizontal(meta & 3).rotateYCCW())
                .withProperty(OPEN, Boolean.valueOf((meta & 4) > 0));
    }

    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        i = i | state.getValue(FACING).rotateY().getHorizontalIndex();
        if (state.getValue(OPEN).booleanValue()) {
            i |= 4;
        }
        return i;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {
                FACING, OPEN
        });
    }

}
