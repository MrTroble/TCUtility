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

    public static final PropertyDirection FACING = PropertyDirection.create("facing",
            EnumFacing.Plane.HORIZONTAL);
    public static final PropertyBool OPEN = PropertyBool.create("open");

    public TCGarageDoor(BlockCreateInfo blockInfo) {
        super(blockInfo);
        this.setCreativeTab(TCTabs.DOORS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH)
                .withProperty(OPEN, Boolean.valueOf(true)));
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return FULL_BLOCK_AABB;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    public int getCloseSound() {
        return this.blockMaterial == Material.IRON ? 1011 : 1012;
    }

    public int getOpenSound() {
        return this.blockMaterial == Material.IRON ? 1005 : 1006;
    }

    public void changeState(World worldIn, BlockPos pos, IBlockState state) {
        if (state.getValue(OPEN).booleanValue()) {
            for (int i = 1; i < 10; i++) {
                final BlockPos posDown = pos.down(i);
                final IBlockState blockState = worldIn.getBlockState(posDown);

                if (!isGateBlock(blockState))
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
                        TCGarageGate.getBlockFromName(this.getRegistryName().toString() + "_gate")
                                .getDefaultState().withProperty(FACING, state.getValue(FACING)));
            }
        }
    }

    public void changeNeighbor(World worldIn, BlockPos pos, IBlockState state) {
        if (state.getValue(FACING).equals(EnumFacing.NORTH)
                || state.getValue(FACING).equals(EnumFacing.SOUTH)) {
            for (int i = 1; i < 10; i++) {
                final BlockPos posEast = pos.east(i);
                IBlockState stateEast = worldIn.getBlockState(posEast);

                if (stateEast.getBlock().equals(this)) {
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

                if (stateWest.getBlock().equals(this)) {
                    stateWest = stateWest.cycleProperty(OPEN);
                    changeState(worldIn, posWest, stateWest);
                    worldIn.setBlockState(posWest, stateWest, 10);
                } else {
                    break;
                }
            }
        } else if (state.getValue(FACING).equals(EnumFacing.WEST)
                || state.getValue(FACING).equals(EnumFacing.EAST)) {
            for (int i = 1; i < 10; i++) {
                final BlockPos posNorth = pos.north(i);
                IBlockState stateNorth = worldIn.getBlockState(posNorth);

                if (stateNorth.getBlock().equals(this)) {
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

                if (stateSouth.getBlock().equals(this)) {
                    stateSouth = stateSouth.cycleProperty(OPEN);
                    changeState(worldIn, posSouth, stateSouth);
                    worldIn.setBlockState(posSouth, stateSouth, 10);
                } else {
                    break;
                }
            }
        }
    }

    private boolean isGateBlock(IBlockState blockState) {
            return blockState.getBlock() instanceof TCGarageGate;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state,
            EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY,
            float hitZ) {
        if (this.blockMaterial.equals(Material.IRON)) {
            return false;
        }
        final BlockPos blockPos = pos;
        final IBlockState iBlockState = pos.equals(blockPos) ? state
                : worldIn.getBlockState(blockPos);
        if (iBlockState.getBlock().equals(this)) {
            state = iBlockState.cycleProperty(OPEN);
            changeState(worldIn, blockPos, state);
            changeNeighbor(worldIn, pos, state);
        }

        worldIn.setBlockState(blockPos, state, 10);
        worldIn.markBlockRangeForRenderUpdate(pos, pos);
        worldIn.playEvent(playerIn,
                state.getValue(OPEN).booleanValue() ? this.getOpenSound() : this.getCloseSound(),
                pos, 0);
        return true;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn,
            BlockPos fromPos) {
        if (!worldIn.isRemote) {
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
                            state.getValue(OPEN).booleanValue() ? this.getOpenSound() : this.getCloseSound(),
                            pos, 0);
                }
            }
        }
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state,
            EntityPlayer player) {
        for (int i = 1; i < 10; i++) {
            final BlockPos posDown = pos.down(i);
            final IBlockState blockState = worldIn.getBlockState(posDown);

            if (!isGateBlock(blockState))
                break;
            worldIn.setBlockToAir(posDown);
        }
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing,
            float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing())
                .withProperty(OPEN, Boolean.valueOf(true));
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState()
                .withProperty(FACING, EnumFacing.getHorizontal(meta & 3).rotateYCCW())
                .withProperty(OPEN, Boolean.valueOf((meta & 4) > 0));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
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

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos,
            EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

}
