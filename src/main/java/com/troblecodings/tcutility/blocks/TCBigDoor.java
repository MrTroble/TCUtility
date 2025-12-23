package com.troblecodings.tcutility.blocks;

import java.util.Random;

import com.troblecodings.tcutility.utils.BlockCreateInfo;

import net.minecraft.block.Block;
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
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TCBigDoor extends TCCube {

    protected Item item;

    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool OPEN = PropertyBool.create("open");
    public static final PropertyEnum<EnumHingePosition> HINGE =
            PropertyEnum.<EnumHingePosition>create("hinge", EnumHingePosition.class);
    public static final PropertyBool POWERED = PropertyBool.create("powered");
    public static final PropertyEnum<EnumDoorThird> THIRD =
            PropertyEnum.<EnumDoorThird>create("third", EnumDoorThird.class);

    protected static final AxisAlignedBB SOUTH_L_AABB =
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.5D, 1.0D, 0.1875D);
    protected static final AxisAlignedBB NORTH_L_AABB =
            new AxisAlignedBB(0.0D, 0.0D, 0.8125D, 1.5D, 1.0D, 1.0D);
    protected static final AxisAlignedBB WEST_L_AABB =
            new AxisAlignedBB(0.8125D, 0.0D, 0.0D, 1.0D, 1.0D, 1.5D);
    protected static final AxisAlignedBB EAST_L_AABB =
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.1875D, 1.0D, 1.5D);
    protected static final AxisAlignedBB SOUTH_R_AABB =
            new AxisAlignedBB(-0.5D, 0.0D, 0.0D, 1.0D, 1.0D, 0.1875D);
    protected static final AxisAlignedBB NORTH_R_AABB =
            new AxisAlignedBB(-0.5D, 0.0D, 0.8125D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB WEST_R_AABB =
            new AxisAlignedBB(0.8125D, 0.0D, -0.5D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB EAST_R_AABB =
            new AxisAlignedBB(0.0D, 0.0D, -0.5D, 0.1875D, 1.0D, 1.0D);

    public TCBigDoor(final BlockCreateInfo blockInfo) {
        super(blockInfo);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH)
                .withProperty(OPEN, Boolean.valueOf(false))
                .withProperty(HINGE, EnumHingePosition.LEFT)
                .withProperty(POWERED, Boolean.valueOf(false))
                .withProperty(THIRD, EnumDoorThird.LOWER));
        setCreativeTab(null);
    }

    public void setItem(final Item item) {
        this.item = item;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, final IBlockAccess source,
            final BlockPos pos) {
        state = state.getActualState(source, pos);
        final EnumFacing enumfacing = state.getValue(FACING);
        final boolean flag = !state.getValue(OPEN).booleanValue();
        final boolean flag1 = state.getValue(HINGE).equals(EnumHingePosition.RIGHT);

        switch (enumfacing) {
            case EAST:
            default:
                if (flag == false && flag1 == false)
                    return SOUTH_L_AABB; // east, left, open
                else if (flag == false && flag1 == true)
                    return NORTH_L_AABB; // east, right, open
                else if (flag == true && flag1 == false)
                    return EAST_L_AABB; // east, left, close
                else
                    return EAST_R_AABB; // east, right, close
            case SOUTH:
                if (flag == false && flag1 == false)
                    return WEST_L_AABB; // south, left, open
                else if (flag == false && flag1 == true)
                    return EAST_L_AABB; // south, right, open
                else if (flag == true && flag1 == false)
                    return SOUTH_R_AABB; // south, left, close
                else
                    return SOUTH_L_AABB; // south, right, close
            case WEST:
                if (flag == false && flag1 == false)
                    return NORTH_R_AABB; // west, left, open
                else if (flag == false && flag1 == true)
                    return SOUTH_R_AABB; // west, right, open
                else if (flag == true && flag1 == false)
                    return WEST_R_AABB; // west, left, close
                else
                    return WEST_L_AABB; // west, right, close
            case NORTH:
                if (flag == false && flag1 == false)
                    return EAST_R_AABB; // north, left, open
                else if (flag == false && flag1 == true)
                    return WEST_R_AABB; // north, right, open
                else if (flag == true && flag1 == false)
                    return NORTH_L_AABB; // north, left, close
                else
                    return NORTH_R_AABB; // north, right, close
        }
    }

    @Override
    public boolean isOpaqueCube(final IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(final IBlockState state) {
        return false;
    }

    private int getCloseSound() {
        return this.blockMaterial.equals(Material.IRON) ? 1011 : 1012;
    }

    private int getOpenSound() {
        return this.blockMaterial.equals(Material.IRON) ? 1005 : 1006;
    }

    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, IBlockState state,
            final EntityPlayer playerIn, final EnumHand hand, final EnumFacing facing,
            final float hitX, final float hitY, final float hitZ) {
        if (this.blockMaterial.equals(Material.IRON))
            return false;
        else {
            if (state.getValue(THIRD).equals(EnumDoorThird.LOWER)) {
                if (state.getBlock() instanceof TCBigDoor) {
                    state = state.cycleProperty(OPEN);
                }
                worldIn.setBlockState(pos, state, 10);
                worldIn.markBlockRangeForRenderUpdate(pos, pos);
                worldIn.playEvent(playerIn,
                        state.getValue(OPEN).booleanValue() ? getOpenSound() : getCloseSound(), pos,
                        0);
                return true;
            } else if (state.getValue(THIRD).equals(EnumDoorThird.MIDDLE)) {
                final BlockPos blockpos = pos.down();
                final IBlockState iblockstate = worldIn.getBlockState(blockpos);
                if (iblockstate.getBlock() instanceof TCBigDoor) {
                    state = iblockstate.cycleProperty(OPEN);
                }
                worldIn.setBlockState(blockpos, state, 10);
                worldIn.markBlockRangeForRenderUpdate(blockpos, pos);
                worldIn.playEvent(playerIn,
                        state.getValue(OPEN).booleanValue() ? this.getOpenSound()
                                : this.getCloseSound(),
                        pos, 0);
                return true;
            } else {
                final BlockPos blockpos = pos.down(2);
                final IBlockState iblockstate = worldIn.getBlockState(blockpos);
                if (iblockstate.getBlock() instanceof TCBigDoor) {
                    state = iblockstate.cycleProperty(OPEN);
                }
                worldIn.setBlockState(blockpos, state, 10);
                worldIn.markBlockRangeForRenderUpdate(blockpos, pos);
                worldIn.markBlockRangeForRenderUpdate(blockpos, pos);
                worldIn.playEvent(playerIn,
                        state.getValue(OPEN).booleanValue() ? this.getOpenSound()
                                : this.getCloseSound(),
                        pos, 0);
                return true;
            }
        }
    }

    @Override
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos,
            final Block blockIn, final BlockPos fromPos) {
        switch (state.getValue(THIRD)) {
            case UPPER:
                final BlockPos blockposUp = pos.down();
                final BlockPos blockposUp1 = pos.down(2);
                final IBlockState iblockstateUp = worldIn.getBlockState(blockposUp);
                final IBlockState iblockstateUp1 = worldIn.getBlockState(blockposUp1);

                if (!(iblockstateUp.getBlock() instanceof TCBigDoor
                        && iblockstateUp1.getBlock() instanceof TCBigDoor)) {
                    worldIn.setBlockToAir(pos);
                } else if (!(blockIn instanceof TCBigDoor)) {
                    iblockstateUp.neighborChanged(worldIn, blockposUp, blockIn, fromPos);
                }
                break;
            case MIDDLE:
                final BlockPos blockposMiddle = pos.down();
                final BlockPos blockposMiddle1 = pos.up();
                final IBlockState iblockstateMiddle = worldIn.getBlockState(blockposMiddle);
                final IBlockState iblockstateMiddle1 = worldIn.getBlockState(blockposMiddle1);

                if (!(iblockstateMiddle.getBlock() instanceof TCBigDoor
                        && iblockstateMiddle1.getBlock() instanceof TCBigDoor)) {
                    worldIn.setBlockToAir(pos);
                } else if (!(blockIn instanceof TCBigDoor)) {
                    iblockstateMiddle.neighborChanged(worldIn, blockposMiddle, blockIn, fromPos);
                }
                break;
            case LOWER:
            default:
                boolean flag1 = false;
                final BlockPos blockposDown = pos.up();
                final BlockPos blockposDown1 = pos.up(2);
                final IBlockState iblockstateDown = worldIn.getBlockState(blockposDown);
                final IBlockState iblockstateDown1 = worldIn.getBlockState(blockposDown1);

                if (!(iblockstateDown.getBlock() instanceof TCBigDoor)) {
                    worldIn.setBlockToAir(pos);
                    flag1 = true;
                }

                if (!(iblockstateDown1.getBlock() instanceof TCBigDoor)) {
                    worldIn.setBlockToAir(pos);
                    flag1 = true;
                }

                if (!worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos.down(),
                        EnumFacing.UP)) {
                    worldIn.setBlockToAir(pos);
                    flag1 = true;

                    if (iblockstateDown.getBlock() instanceof TCBigDoor) {
                        worldIn.setBlockToAir(blockposDown);
                        worldIn.setBlockToAir(blockposDown1);
                    }
                }

                if (flag1) {
                    if (!worldIn.isRemote) {
                        this.dropBlockAsItem(worldIn, pos, state, 0);
                    }
                } else {
                    final boolean flag =
                            worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(blockposDown)
                                    || worldIn.isBlockPowered(blockposDown1);
                    final boolean powerd = iblockstateDown.getValue(POWERED);

                    if (!(blockIn instanceof TCBigDoor)
                            && (flag || blockIn.getDefaultState().canProvidePower())
                            && (flag != powerd || flag != powerd)) {
                        worldIn.setBlockState(blockposDown,
                                iblockstateDown.withProperty(POWERED, Boolean.valueOf(flag)), 2);
                        worldIn.setBlockState(blockposDown1,
                                iblockstateDown1.withProperty(POWERED, Boolean.valueOf(flag)), 2);

                        if (flag != state.getValue(OPEN).booleanValue()) {
                            worldIn.setBlockState(pos,
                                    state.withProperty(OPEN, Boolean.valueOf(flag)), 2);
                            worldIn.markBlockRangeForRenderUpdate(pos, pos);
                            worldIn.playEvent((EntityPlayer) null,
                                    flag ? this.getOpenSound() : this.getCloseSound(), pos, 0);
                        }
                    }
                }
                break;
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

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state,
            final BlockPos pos, final EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public void onBlockHarvested(final World worldIn, final BlockPos pos, final IBlockState state,
            final EntityPlayer player) {
        final EnumDoorThird part = state.getValue(THIRD);
        BlockPos blockPos1 = pos;
        BlockPos blockPos2 = pos;
        switch (part) {
            case LOWER:
                blockPos1 = pos.up();
                blockPos2 = pos.up(2);
                if (player.capabilities.isCreativeMode
                        && worldIn.getBlockState(blockPos1).getBlock() instanceof TCBigDoor
                        && worldIn.getBlockState(blockPos2).getBlock() instanceof TCBigDoor) {
                    worldIn.setBlockToAir(blockPos1);
                    worldIn.setBlockToAir(blockPos2);
                }
                break;
            case MIDDLE:
                blockPos1 = pos.down();
                blockPos2 = pos.up();
                if (player.capabilities.isCreativeMode
                        && worldIn.getBlockState(blockPos1).getBlock() instanceof TCBigDoor
                        && worldIn.getBlockState(blockPos2).getBlock() instanceof TCBigDoor) {
                    worldIn.setBlockToAir(blockPos1);
                    worldIn.setBlockToAir(blockPos2);
                }
                break;
            case UPPER:
                blockPos1 = pos.down(2);
                blockPos2 = pos.down();
                if (player.capabilities.isCreativeMode
                        && worldIn.getBlockState(blockPos1).getBlock() instanceof TCBigDoor
                        && worldIn.getBlockState(blockPos2).getBlock() instanceof TCBigDoor) {
                    worldIn.setBlockToAir(blockPos1);
                    worldIn.setBlockToAir(blockPos2);
                }
                break;
        }
    }

    @Override
    public IBlockState getActualState(final IBlockState stateIn, final IBlockAccess worldIn,
            final BlockPos pos) {
        IBlockState state = stateIn;
        switch (state.getValue(THIRD)) {
            case UPPER:
                final IBlockState iblockstateUp = worldIn.getBlockState(pos.down(2));
                if (iblockstateUp.getBlock() instanceof TCBigDoor) {
                    state = state.withProperty(FACING, iblockstateUp.getValue(FACING))
                            .withProperty(OPEN, iblockstateUp.getValue(OPEN));
                }
                break;
            case MIDDLE:
                final IBlockState iblockstateMiddle = worldIn.getBlockState(pos.down());
                final IBlockState iblockstateMiddle1 = worldIn.getBlockState(pos.up());
                if (iblockstateMiddle1.getBlock() instanceof TCBigDoor) {
                    state = state.withProperty(HINGE, iblockstateMiddle1.getValue(HINGE))
                            .withProperty(POWERED, iblockstateMiddle1.getValue(POWERED));
                }
                if (iblockstateMiddle.getBlock() instanceof TCBigDoor) {
                    state = state.withProperty(FACING, iblockstateMiddle.getValue(FACING))
                            .withProperty(OPEN, iblockstateMiddle.getValue(OPEN));
                }
                break;
            case LOWER:
            default:
                final IBlockState iblockstateDown = worldIn.getBlockState(pos.up(2));
                if (iblockstateDown.getBlock() instanceof TCBigDoor) {
                    state = state.withProperty(HINGE, iblockstateDown.getValue(HINGE))
                            .withProperty(POWERED, iblockstateDown.getValue(POWERED));
                }
                break;
        }
        return state;
    }

    @Override
    public IBlockState withRotation(final IBlockState state, final Rotation rot) {
        return !state.getValue(THIRD).equals(EnumDoorThird.LOWER) ? state
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
        if (((meta & 8) > 0) && ((meta & 12) < 12))
            return this.getDefaultState().withProperty(THIRD, EnumDoorThird.UPPER)
                    .withProperty(HINGE,
                            (meta & 1) > 0 ? EnumHingePosition.RIGHT : EnumHingePosition.LEFT)
                    .withProperty(POWERED, Boolean.valueOf((meta & 2) > 0));
        else if ((meta & 0) >= 0 && ((meta & 12) < 12))
            return this.getDefaultState().withProperty(THIRD, EnumDoorThird.LOWER)
                    .withProperty(FACING, EnumFacing.getHorizontal(meta & 3).rotateYCCW())
                    .withProperty(OPEN, Boolean.valueOf((meta & 4) > 0));
        else
            return this.getDefaultState().withProperty(THIRD, EnumDoorThird.MIDDLE);
    }

    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        switch (state.getValue(THIRD)) {
            case UPPER:
                i = i | 8;
                if (state.getValue(HINGE).equals(EnumHingePosition.RIGHT)) {
                    i |= 1;
                }
                if (state.getValue(POWERED).booleanValue()) {
                    i |= 2;
                }
                break;
            case MIDDLE:
                i = i | 12;
                break;
            case LOWER:
            default:
                i = i | state.getValue(FACING).rotateY().getHorizontalIndex();

                if (state.getValue(OPEN).booleanValue()) {
                    i |= 4;
                }
                break;
        }
        return i;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {
                THIRD, FACING, OPEN, HINGE, POWERED
        });
    }

    public static enum EnumDoorThird implements IStringSerializable {
        UPPER, MIDDLE, LOWER;

        @Override
        public String toString() {
            return this.getName();
        }

        @Override
        public String getName() {
            switch (this) {
                case UPPER:
                    return "upper";
                case MIDDLE:
                    return "middle";
                case LOWER:
                default:
                    return "lower";
            }
        }
    }
}
