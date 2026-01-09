package com.troblecodings.tcutility.blocks;

import java.util.Random;

import com.troblecodings.tcutility.utils.BlockCreateInfo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor.EnumDoorHalf;
import net.minecraft.block.BlockDoor.EnumHingePosition;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TCDoor extends TCCube {

    protected Item item;

    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool OPEN = PropertyBool.create("open");
    public static final PropertyEnum<EnumHingePosition> HINGE =
            PropertyEnum.<EnumHingePosition>create("hinge", EnumHingePosition.class);
    public static final PropertyBool POWERED = PropertyBool.create("powered");
    public static final PropertyEnum<EnumDoorHalf> HALF =
            PropertyEnum.<EnumDoorHalf>create("half", EnumDoorHalf.class);

    protected static final AxisAlignedBB SOUTH_AABB =
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.1875D);
    protected static final AxisAlignedBB NORTH_AABB =
            new AxisAlignedBB(0.0D, 0.0D, 0.8125D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB WEST_AABB =
            new AxisAlignedBB(0.8125D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB EAST_AABB =
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.1875D, 1.0D, 1.0D);

    public TCDoor(final BlockCreateInfo blockInfo) {
        super(blockInfo);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH)
                .withProperty(OPEN, Boolean.valueOf(false))
                .withProperty(HINGE, EnumHingePosition.LEFT)
                .withProperty(POWERED, Boolean.valueOf(false))
                .withProperty(HALF, EnumDoorHalf.LOWER));
        setCreativeTab(null);
    }

    public void setItem(final Item item) {
        this.item = item;
    }

    @Override
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source,
            final BlockPos pos) {
        final IBlockState states = state.getActualState(source, pos);
        final EnumFacing enumfacing = states.getValue(FACING);
        final boolean flag = !states.getValue(OPEN).booleanValue();
        final boolean flag1 = states.getValue(HINGE).equals(EnumHingePosition.RIGHT);

        switch (enumfacing) {
            case EAST:
            default:
                return flag ? EAST_AABB : (flag1 ? NORTH_AABB : SOUTH_AABB);
            case SOUTH:
                return flag ? SOUTH_AABB : (flag1 ? EAST_AABB : WEST_AABB);
            case WEST:
                return flag ? WEST_AABB : (flag1 ? SOUTH_AABB : NORTH_AABB);
            case NORTH:
                return flag ? NORTH_AABB : (flag1 ? WEST_AABB : EAST_AABB);
        }
    }

    @Override
    public boolean isPassable(final IBlockAccess worldIn, final BlockPos pos) {
        return isOpen(combineMetadata(worldIn, pos));
    }

    @Override
    public boolean isOpaqueCube(final IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(final IBlockState state) {
        return false;
    }

    protected int getCloseSound() {
        return this.blockMaterial.equals(Material.IRON) ? 1011 : 1012;
    }

    protected int getOpenSound() {
        return this.blockMaterial.equals(Material.IRON) ? 1005 : 1006;
    }

    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos,
            final IBlockState state, final EntityPlayer playerIn, final EnumHand hand,
            final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        if (this.blockMaterial.equals(Material.IRON))
            return false;
        else {
            final BlockPos blockpos =
                    state.getValue(HALF).equals(EnumDoorHalf.LOWER) ? pos : pos.down();
            final IBlockState iblockstate =
                    pos.equals(blockpos) ? state : worldIn.getBlockState(blockpos);

            if (!(iblockstate.getBlock() instanceof TCDoor))
                return false;
            else {
                final IBlockState state2 = iblockstate.cycleProperty(OPEN);
                worldIn.setBlockState(blockpos, state2, 10);
                worldIn.markBlockRangeForRenderUpdate(blockpos, pos);
                worldIn.playEvent(playerIn,
                        state2.getValue(OPEN).booleanValue() ? this.getOpenSound()
                                : this.getCloseSound(),
                        pos, 0);
                return true;
            }
        }
    }

    public void toggleDoor(final World worldIn, final BlockPos pos, final boolean open) {
        final IBlockState iblockstate = worldIn.getBlockState(pos);

        if (iblockstate.getBlock() instanceof TCDoor) {
            final BlockPos blockpos =
                    iblockstate.getValue(HALF).equals(EnumDoorHalf.LOWER) ? pos : pos.down();
            final IBlockState iblockstate1 =
                    pos.equals(blockpos) ? iblockstate : worldIn.getBlockState(blockpos);

            if (iblockstate1.getBlock() instanceof TCDoor
                    && iblockstate1.getValue(OPEN).booleanValue() != open) {
                worldIn.setBlockState(blockpos,
                        iblockstate1.withProperty(OPEN, Boolean.valueOf(open)), 10);
                worldIn.markBlockRangeForRenderUpdate(blockpos, pos);
                worldIn.playEvent((EntityPlayer) null,
                        open ? this.getOpenSound() : this.getCloseSound(), pos, 0);
            }
        }
    }

    @Override
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos,
            final Block blockIn, final BlockPos fromPos) {
        if (state.getValue(HALF).equals(EnumDoorHalf.UPPER)) {
            final BlockPos blockpos = pos.down();
            final IBlockState iblockstate = worldIn.getBlockState(blockpos);

            if (!(iblockstate.getBlock() instanceof TCDoor)) {
                worldIn.setBlockToAir(pos);
            } else if (!(blockIn instanceof TCDoor)) {
                iblockstate.neighborChanged(worldIn, blockpos, blockIn, fromPos);
            }
        } else {
            boolean flag1 = false;
            final BlockPos blockpos1 = pos.up();
            final IBlockState iblockstate1 = worldIn.getBlockState(blockpos1);

            if (!(iblockstate1.getBlock() instanceof TCDoor)) {
                worldIn.setBlockToAir(pos);
                flag1 = true;
            }

            if (!worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos.down(),
                    EnumFacing.UP)) {
                worldIn.setBlockToAir(pos);
                flag1 = true;

                if (iblockstate1.getBlock() instanceof TCDoor) {
                    worldIn.setBlockToAir(blockpos1);
                }
            }

            if (flag1) {
                if (!worldIn.isRemote) {
                    this.dropBlockAsItem(worldIn, pos, state, 0);
                }
            } else {
                final boolean flag =
                        worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(blockpos1);

                if (!(blockIn instanceof TCDoor)
                        && (flag || blockIn.getDefaultState().canProvidePower())
                        && flag != iblockstate1.getValue(POWERED).booleanValue()) {
                    worldIn.setBlockState(blockpos1,
                            iblockstate1.withProperty(POWERED, Boolean.valueOf(flag)), 2);

                    if (flag != state.getValue(OPEN).booleanValue()) {
                        worldIn.setBlockState(pos, state.withProperty(OPEN, Boolean.valueOf(flag)),
                                2);
                        worldIn.markBlockRangeForRenderUpdate(pos, pos);
                        worldIn.playEvent((EntityPlayer) null,
                                flag ? this.getOpenSound() : this.getCloseSound(), pos, 0);
                    }
                }
            }
        }
    }

    @Override
    public ItemStack getItem(final World worldIn, final BlockPos pos, final IBlockState state) {
        return new ItemStack(item);
    }

    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return item;
    }

    @Override
    public ItemStack getPickBlock(final IBlockState state, final RayTraceResult target,
            final World world, final BlockPos pos, final EntityPlayer player) {
        return getItem(world, pos, state);
    }

    @Override
    public EnumPushReaction getMobilityFlag(final IBlockState state) {
        return EnumPushReaction.DESTROY;
    }

    public static int combineMetadata(final IBlockAccess worldIn, final BlockPos pos) {
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        final int i = iblockstate.getBlock().getMetaFromState(iblockstate);
        final boolean flag = isTop(i);
        final IBlockState iblockstate1 = worldIn.getBlockState(pos.down());
        final int j = iblockstate1.getBlock().getMetaFromState(iblockstate1);
        final int k = flag ? j : i;
        final IBlockState iblockstate2 = worldIn.getBlockState(pos.up());
        final int l = iblockstate2.getBlock().getMetaFromState(iblockstate2);
        final int i1 = flag ? i : l;
        final boolean flag1 = (i1 & 1) != 0;
        final boolean flag2 = (i1 & 2) != 0;
        return removeHalfBit(k) | (flag ? 8 : 0) | (flag1 ? 16 : 0) | (flag2 ? 32 : 0);
    }

    @Override
    public void onBlockHarvested(final World worldIn, final BlockPos pos, final IBlockState state,
            final EntityPlayer player) {
        final BlockPos blockpos = pos.down();
        final BlockPos blockpos1 = pos.up();

        if (player.capabilities.isCreativeMode && state.getValue(HALF).equals(EnumDoorHalf.UPPER)
                && worldIn.getBlockState(blockpos).getBlock() instanceof TCDoor) {
            worldIn.setBlockToAir(blockpos);
        }

        if (state.getValue(HALF).equals(EnumDoorHalf.LOWER)
                && worldIn.getBlockState(blockpos1).getBlock() instanceof TCDoor) {
            if (player.capabilities.isCreativeMode) {
                worldIn.setBlockToAir(pos);
            }

            worldIn.setBlockToAir(blockpos1);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public IBlockState getActualState(final IBlockState state, final IBlockAccess worldIn,
            final BlockPos pos) {
        IBlockState state2 = state;
        if (state.getValue(HALF).equals(EnumDoorHalf.LOWER)) {
            final IBlockState iblockstate = worldIn.getBlockState(pos.up());

            if (iblockstate.getBlock() instanceof TCDoor) {
                state2 = state.withProperty(HINGE, iblockstate.getValue(HINGE))
                        .withProperty(POWERED, iblockstate.getValue(POWERED));
            }
        } else {
            final IBlockState iblockstate1 = worldIn.getBlockState(pos.down());

            if (iblockstate1.getBlock() instanceof TCDoor) {
                state2 = state.withProperty(FACING, iblockstate1.getValue(FACING))
                        .withProperty(OPEN, iblockstate1.getValue(OPEN));
            }
        }

        return state2;
    }

    @Override
    public IBlockState withRotation(final IBlockState state, final Rotation rot) {
        return !state.getValue(HALF).equals(EnumDoorHalf.LOWER) ? state
                : state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public IBlockState withMirror(final IBlockState state, final Mirror mirrorIn) {
        return mirrorIn.equals(Mirror.NONE) ? state
                : state.withRotation(mirrorIn.toRotation(state.getValue(FACING)))
                        .cycleProperty(HINGE);
    }

    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return (meta & 8) > 0
                ? this.getDefaultState().withProperty(HALF, EnumDoorHalf.UPPER)
                        .withProperty(HINGE,
                                (meta & 1) > 0 ? EnumHingePosition.RIGHT : EnumHingePosition.LEFT)
                        .withProperty(POWERED, Boolean.valueOf((meta & 2) > 0))
                : this.getDefaultState().withProperty(HALF, EnumDoorHalf.LOWER)
                        .withProperty(FACING, EnumFacing.getHorizontal(meta & 3).rotateYCCW())
                        .withProperty(OPEN, Boolean.valueOf((meta & 4) > 0));
    }

    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;

        if (state.getValue(HALF).equals(EnumDoorHalf.UPPER)) {
            i = i | 8;

            if (state.getValue(HINGE).equals(EnumHingePosition.RIGHT)) {
                i |= 1;
            }

            if (state.getValue(POWERED).booleanValue()) {
                i |= 2;
            }
        } else {
            i = i | state.getValue(FACING).rotateY().getHorizontalIndex();

            if (state.getValue(OPEN).booleanValue()) {
                i |= 4;
            }
        }

        return i;
    }

    protected static int removeHalfBit(final int meta) {
        return meta & 7;
    }

    protected static boolean isOpen(final int combinedMeta) {
        return (combinedMeta & 4) != 0;
    }

    protected static boolean isTop(final int meta) {
        return (meta & 8) != 0;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {
                HALF, FACING, OPEN, HINGE, POWERED
        });
    }

    @Override
    public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state,
            final BlockPos pos, final EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
}
