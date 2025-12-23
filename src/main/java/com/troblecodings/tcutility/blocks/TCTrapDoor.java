package com.troblecodings.tcutility.blocks;

import javax.annotation.Nullable;

import com.troblecodings.tcutility.init.TCTabs;
import com.troblecodings.tcutility.utils.BlockCreateInfo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TCTrapDoor extends TCCube {
    public static final PropertyEnum<DoorHalf> HALF =
            PropertyEnum.<DoorHalf>create("half", DoorHalf.class);
    public static final PropertyBool OPEN = PropertyBool.create("open");
    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    protected static final AxisAlignedBB EAST_OPEN_AABB =
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.1875D, 1.0D, 1.0D);
    protected static final AxisAlignedBB WEST_OPEN_AABB =
            new AxisAlignedBB(0.8125D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB SOUTH_OPEN_AABB =
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.1875D);
    protected static final AxisAlignedBB NORTH_OPEN_AABB =
            new AxisAlignedBB(0.0D, 0.0D, 0.8125D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB BOTTOM_AABB =
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.1875D, 1.0D);
    protected static final AxisAlignedBB TOP_AABB =
            new AxisAlignedBB(0.0D, 0.8125D, 0.0D, 1.0D, 1.0D, 1.0D);

    public TCTrapDoor(final BlockCreateInfo blockInfo) {
        super(blockInfo);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH)
                .withProperty(OPEN, Boolean.valueOf(false)).withProperty(HALF, DoorHalf.BOTTOM));
        this.setCreativeTab(TCTabs.DOORS);
    }

    @Override
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source,
            final BlockPos pos) {
        AxisAlignedBB axisalignedbb;

        if (state.getValue(OPEN).booleanValue()) {
            switch (state.getValue(FACING)) {
                case NORTH:
                default:
                    axisalignedbb = NORTH_OPEN_AABB;
                    break;
                case SOUTH:
                    axisalignedbb = SOUTH_OPEN_AABB;
                    break;
                case WEST:
                    axisalignedbb = WEST_OPEN_AABB;
                    break;
                case EAST:
                    axisalignedbb = EAST_OPEN_AABB;
            }
        } else if (state.getValue(HALF).equals(DoorHalf.TOP)) {
            axisalignedbb = TOP_AABB;
        } else {
            axisalignedbb = BOTTOM_AABB;
        }

        return axisalignedbb;
    }

    @Override
    public boolean isOpaqueCube(final IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(final IBlockState state) {
        return false;
    }

    @Override
    public boolean isPassable(final IBlockAccess worldIn, final BlockPos pos) {
        return !worldIn.getBlockState(pos).getValue(OPEN).booleanValue();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, IBlockState state,
            final EntityPlayer playerIn, final EnumHand hand, final EnumFacing facing,
            final float hitX, final float hitY, final float hitZ) {
        state = state.cycleProperty(OPEN);
        worldIn.setBlockState(pos, state, 2);
        this.playSound(playerIn, worldIn, pos, state.getValue(OPEN).booleanValue());
        return true;
    }

    protected void playSound(@Nullable final EntityPlayer player, final World worldIn,
            final BlockPos pos, final boolean p_185731_4_) {
        final int j = this.blockMaterial.equals(Material.IRON) ? 1036 : 1013;
        worldIn.playEvent(player, j, pos, 0);
    }

    @Override
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos,
            final Block blockIn, final BlockPos fromPos) {
        if (!worldIn.isRemote) {
            final boolean flag = worldIn.isBlockPowered(pos);

            if (flag || blockIn.getDefaultState().canProvidePower()) {
                final boolean flag1 = state.getValue(OPEN).booleanValue();

                if (flag1 != flag) {
                    worldIn.setBlockState(pos, state.withProperty(OPEN, Boolean.valueOf(flag)), 2);
                    this.playSound((EntityPlayer) null, worldIn, pos, flag);
                }
            }
        }
    }

    @Override
    public IBlockState getStateForPlacement(final World worldIn, final BlockPos pos,
            final EnumFacing facing, final float hitX, final float hitY, final float hitZ,
            final int meta, final EntityLivingBase placer) {
        IBlockState iblockstate = this.getDefaultState();

        if (facing.getAxis().isHorizontal()) {
            iblockstate = iblockstate.withProperty(FACING, facing).withProperty(OPEN,
                    Boolean.valueOf(false));
            iblockstate =
                    iblockstate.withProperty(HALF, hitY > 0.5F ? DoorHalf.TOP : DoorHalf.BOTTOM);
        } else {
            iblockstate =
                    iblockstate.withProperty(FACING, placer.getHorizontalFacing().getOpposite())
                            .withProperty(OPEN, Boolean.valueOf(false));
            iblockstate = iblockstate.withProperty(HALF,
                    facing.equals(EnumFacing.UP) ? DoorHalf.BOTTOM : DoorHalf.TOP);
        }

        if (worldIn.isBlockPowered(pos)) {
            iblockstate = iblockstate.withProperty(OPEN, Boolean.valueOf(true));
        }

        return iblockstate;
    }

    @Override
    public boolean canPlaceBlockOnSide(final World worldIn, final BlockPos pos,
            final EnumFacing side) {
        return true;
    }

    protected static EnumFacing getFacing(final int meta) {
        switch (meta & 3) {
            case 0:
                return EnumFacing.NORTH;
            case 1:
                return EnumFacing.SOUTH;
            case 2:
                return EnumFacing.WEST;
            case 3:
            default:
                return EnumFacing.EAST;
        }
    }

    protected static int getMetaForFacing(final EnumFacing facing) {
        switch (facing) {
            case NORTH:
                return 0;
            case SOUTH:
                return 1;
            case WEST:
                return 2;
            case EAST:
            default:
                return 3;
        }
    }

    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(FACING, getFacing(meta))
                .withProperty(OPEN, Boolean.valueOf((meta & 4) != 0))
                .withProperty(HALF, (meta & 8) == 0 ? DoorHalf.BOTTOM : DoorHalf.TOP);
    }

    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        i = i | getMetaForFacing(state.getValue(FACING));

        if (state.getValue(OPEN).booleanValue()) {
            i |= 4;
        }

        if (state.getValue(HALF).equals(DoorHalf.TOP)) {
            i |= 8;
        }

        return i;
    }

    @Override
    public IBlockState withRotation(final IBlockState state, final Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public IBlockState withMirror(final IBlockState state, final Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {
                FACING, OPEN, HALF
        });
    }

    @Override
    public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state,
            final BlockPos pos, final EnumFacing face) {
        boolean facing = face.equals(EnumFacing.UP) && state.getValue(HALF).equals(DoorHalf.TOP)
                || face.equals(EnumFacing.DOWN) && state.getValue(HALF).equals(DoorHalf.BOTTOM);
        return facing && !state.getValue(OPEN).booleanValue() ? BlockFaceShape.SOLID
                : BlockFaceShape.UNDEFINED;
    }

    @Override
    public boolean isLadder(final IBlockState state, final IBlockAccess world, final BlockPos pos,
            final EntityLivingBase entity) {
        if (state.getValue(OPEN)) {
            final IBlockState down = world.getBlockState(pos.down());
            if (down.getBlock().equals(Blocks.LADDER))
                return down.getValue(BlockLadder.FACING).equals(state.getValue(FACING));
            if (down.getBlock() instanceof TCLadder)
                return down.getValue(TCLadder.FACING).equals(state.getValue(FACING));
        }
        return false;
    }

    public static enum DoorHalf implements IStringSerializable {
        TOP("top"), BOTTOM("bottom");

        private final String name;

        private DoorHalf(final String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }

        @Override
        public String getName() {
            return this.name;
        }
    }
}
