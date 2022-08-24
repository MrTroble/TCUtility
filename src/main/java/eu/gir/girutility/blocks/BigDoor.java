package eu.gir.girutility.blocks;

import java.util.Random;

import eu.gir.girutility.init.GIRBlocks;
import eu.gir.girutility.init.GIRItems;
import net.minecraft.block.Block;
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
import net.minecraft.init.Items;
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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BigDoor extends Block {
    
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool OPEN = PropertyBool.create("open");
    public static final PropertyEnum<BigDoor.EnumHingePosition> HINGE = PropertyEnum.<BigDoor.EnumHingePosition>create("hinge", BigDoor.EnumHingePosition.class);
    public static final PropertyBool POWERED = PropertyBool.create("powered");
    public static final PropertyEnum<BigDoor.EnumDoorThird> THIRD = PropertyEnum.<BigDoor.EnumDoorThird>create("third", BigDoor.EnumDoorThird.class);
    
    protected static final AxisAlignedBB SOUTH_L_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.5D, 1.0D, 0.1875D);
    protected static final AxisAlignedBB NORTH_L_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.8125D, 1.5D, 1.0D, 1.0D);
    protected static final AxisAlignedBB WEST_L_AABB = new AxisAlignedBB(0.8125D, 0.0D, 0.0D, 1.0D, 1.0D, 1.5D);
    protected static final AxisAlignedBB EAST_L_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.1875D, 1.0D, 1.5D);
    protected static final AxisAlignedBB SOUTH_R_AABB = new AxisAlignedBB(-0.5D, 0.0D, 0.0D, 1.0D, 1.0D, 0.1875D);
    protected static final AxisAlignedBB NORTH_R_AABB = new AxisAlignedBB(-0.5D, 0.0D, 0.8125D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB WEST_R_AABB = new AxisAlignedBB(0.8125D, 0.0D, -0.5D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB EAST_R_AABB = new AxisAlignedBB(0.0D, 0.0D, -0.5D, 0.1875D, 1.0D, 1.0D);

    public BigDoor(Material material) {
        super(material);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(OPEN, Boolean.valueOf(false)).withProperty(HINGE, BigDoor.EnumHingePosition.LEFT).withProperty(POWERED, Boolean.valueOf(false)).withProperty(THIRD, BigDoor.EnumDoorThird.LOWER));
    }
    
    private Item getItem() {
        if (this == GIRBlocks.BIGDOOR1_BLOCK) {
            return GIRItems.BIGDOOR1;
        } else {
            return Items.OAK_DOOR;
        }
    }
    
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        state = state.getActualState(source, pos);
        EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
        boolean flag = !((Boolean)state.getValue(OPEN)).booleanValue();
        boolean flag1 = state.getValue(HINGE) == BigDoor.EnumHingePosition.RIGHT;
        
        switch (enumfacing) {
            case EAST:
            default:
                if (flag == false && flag1 == false) {
                    return SOUTH_L_AABB; //east, left, open
                } else if (flag == false && flag1 == true) {
                    return NORTH_L_AABB; //east, right, open
                } else if (flag == true && flag1 == false) {
                    return EAST_L_AABB; //east, left, close
                } else {
                    return EAST_R_AABB; //east, right, close
                }
            case SOUTH:
                if (flag == false && flag1 == false) {
                    return WEST_L_AABB; //south, left, open
                } else if (flag == false && flag1 == true) {
                    return EAST_L_AABB; //south, right, open
                } else if (flag == true && flag1 == false) {
                    return SOUTH_R_AABB; //south, left, close
                } else {
                    return SOUTH_L_AABB; //south, right, close
                }
            case WEST:
                if (flag == false && flag1 == false) {
                    return NORTH_R_AABB; //west, left, open
                } else if (flag == false && flag1 == true) {
                    return SOUTH_R_AABB; //west, right, open
                } else if (flag == true && flag1 == false) {
                    return WEST_R_AABB; //west, left, close
                } else {
                    return WEST_L_AABB; //west, right, close
                }
            case NORTH:
                if (flag == false && flag1 == false) {
                    return EAST_R_AABB; //north, left, open
                } else if (flag == false && flag1 == true) {
                    return WEST_R_AABB; //north, right, open
                } else if (flag == true && flag1 == false) {
                    return NORTH_L_AABB; //north, left, close
                } else {
                    return NORTH_R_AABB; //north, right, close
                }
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
    
    private int getCloseSound() {
        return this.blockMaterial == Material.IRON ? 1011 : 1012;
    }
    
    private int getOpenSound() {
        return this.blockMaterial == Material.IRON ? 1005 : 1006;
    }
    
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (this.blockMaterial == Material.IRON) {
            return false;
        } else {
            if (state.getValue(THIRD) == BigDoor.EnumDoorThird.LOWER) {
                BlockPos blockpos = pos;
                IBlockState iblockstate = pos.equals(blockpos) ? state : worldIn.getBlockState(blockpos);
                if (iblockstate.getBlock() == this) {
                    state = iblockstate.cycleProperty(OPEN);   
                }
                worldIn.setBlockState(blockpos, state, 10);
                worldIn.markBlockRangeForRenderUpdate(blockpos, pos);
                worldIn.playEvent(playerIn, ((Boolean)state.getValue(OPEN)).booleanValue() ? this.getOpenSound() : this.getCloseSound(), pos, 0);
                return true;
            } else if (state.getValue(THIRD) == BigDoor.EnumDoorThird.MIDDLE) {
                BlockPos blockpos = pos.down();
                IBlockState iblockstate = pos.equals(blockpos) ? state : worldIn.getBlockState(blockpos);
                if (iblockstate.getBlock() == this) {
                    state = iblockstate.cycleProperty(OPEN);   
                }
                worldIn.setBlockState(blockpos, state, 10);
                worldIn.markBlockRangeForRenderUpdate(blockpos, pos);
                worldIn.playEvent(playerIn, ((Boolean)state.getValue(OPEN)).booleanValue() ? this.getOpenSound() : this.getCloseSound(), pos, 0);
                return true;
            } else {
                BlockPos blockpos = pos.down(2);
                IBlockState iblockstate = pos.equals(blockpos) ? state : worldIn.getBlockState(blockpos);
                if (iblockstate.getBlock() == this) {
                    state = iblockstate.cycleProperty(OPEN);   
                }
                worldIn.setBlockState(blockpos, state, 10);
                worldIn.markBlockRangeForRenderUpdate(blockpos, pos);
                worldIn.markBlockRangeForRenderUpdate(blockpos, pos);
                worldIn.playEvent(playerIn, ((Boolean)state.getValue(OPEN)).booleanValue() ? this.getOpenSound() : this.getCloseSound(), pos, 0);
                return true; 
            }
        }
    }
    
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (state.getValue(THIRD) == BigDoor.EnumDoorThird.UPPER) {
            BlockPos blockpos = pos.down();
            BlockPos blockpos1 = pos.down(2);
            IBlockState iblockstate = worldIn.getBlockState(blockpos);
            IBlockState iblockstate1 = worldIn.getBlockState(blockpos1);

            if ((iblockstate.getBlock() != this) || (iblockstate1.getBlock() != this)) {
                worldIn.setBlockToAir(pos);
            } else if (blockIn != this) {
                iblockstate.neighborChanged(worldIn, blockpos, blockIn, fromPos);
            }
        } else if (state.getValue(THIRD) == BigDoor.EnumDoorThird.MIDDLE) {
            BlockPos blockpos = pos.down();
            BlockPos blockpos1 = pos.up();
            IBlockState iblockstate = worldIn.getBlockState(blockpos);
            IBlockState iblockstate1 = worldIn.getBlockState(blockpos1);
            
            if ((iblockstate.getBlock() != this) || (iblockstate1.getBlock() != this)) {
                worldIn.setBlockToAir(pos);
            } else if (blockIn != this) {
                iblockstate.neighborChanged(worldIn, blockpos, blockIn, fromPos);
            }
        } else {
            boolean flag1 = false;
            BlockPos blockpos1 = pos.up();
            BlockPos blockpos2 = pos.up(2);
            IBlockState iblockstate1 = worldIn.getBlockState(blockpos1);
            IBlockState iblockstate2 = worldIn.getBlockState(blockpos2);

            if (iblockstate1.getBlock() != this) {
                worldIn.setBlockToAir(pos);
                flag1 = true;
            }
            
            if (iblockstate2.getBlock() != this) {
                worldIn.setBlockToAir(pos);
                flag1 = true;
            }

            if (!worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos.down(), EnumFacing.UP)) {
                worldIn.setBlockToAir(pos);
                flag1 = true;

                if (iblockstate1.getBlock() == this) {
                    worldIn.setBlockToAir(blockpos1);
                    worldIn.setBlockToAir(blockpos2);
                }
            }

            if (flag1) {
                if (!worldIn.isRemote) {
                    this.dropBlockAsItem(worldIn, pos, state, 0);
                }
            }
            else {
                boolean flag = worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(blockpos1) || worldIn.isBlockPowered(blockpos2);

                if (blockIn != this && (flag || blockIn.getDefaultState().canProvidePower()) && (flag != ((Boolean)iblockstate1.getValue(POWERED)).booleanValue() || flag != ((Boolean)iblockstate2.getValue(POWERED)).booleanValue())) {
                    worldIn.setBlockState(blockpos1, iblockstate1.withProperty(POWERED, Boolean.valueOf(flag)), 2);
                    worldIn.setBlockState(blockpos2, iblockstate2.withProperty(POWERED, Boolean.valueOf(flag)), 2);

                    if (flag != ((Boolean)state.getValue(OPEN)).booleanValue()) {
                        worldIn.setBlockState(pos, state.withProperty(OPEN, Boolean.valueOf(flag)), 2);
                        worldIn.markBlockRangeForRenderUpdate(pos, pos);
                        worldIn.playEvent((EntityPlayer)null, flag ? this.getOpenSound() : this.getCloseSound(), pos, 0);
                    }
                }
            }
        }
    }
    
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        if (state.getValue(THIRD) == BigDoor.EnumDoorThird.LOWER) {
            return this.getItem();
        } else {
            return Items.AIR;
        }
    }
    
    @Override
    public EnumPushReaction getMobilityFlag(IBlockState state) {
        return EnumPushReaction.DESTROY;
    }
    
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(this.getItem());
    }
    
    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        BigDoor.EnumDoorThird part = state.getValue(THIRD);
        BlockPos blockPos1 = pos;
        BlockPos blockPos2 = pos;
        switch(part) {
           case LOWER:
               blockPos1 = pos.up();
               blockPos2 = pos.up(2);
               if (player.capabilities.isCreativeMode && worldIn.getBlockState(blockPos1).getBlock() == this && worldIn.getBlockState(blockPos2).getBlock() == this) {
                   worldIn.setBlockToAir(blockPos1);
                   worldIn.setBlockToAir(blockPos2);
               }
               break;
           case MIDDLE:
               blockPos1 = pos.down();
               blockPos2 = pos.up();
               if (player.capabilities.isCreativeMode && worldIn.getBlockState(blockPos1).getBlock() == this && worldIn.getBlockState(blockPos2).getBlock() == this) {
                   worldIn.setBlockToAir(blockPos1);
                   worldIn.setBlockToAir(blockPos2);
               }
               break;
           case UPPER:
               blockPos1 = pos.down(2);
               blockPos2 = pos.down();
               if (player.capabilities.isCreativeMode && worldIn.getBlockState(blockPos1).getBlock() == this && worldIn.getBlockState(blockPos2).getBlock() == this) {
                   worldIn.setBlockToAir(blockPos1);
                   worldIn.setBlockToAir(blockPos2);
               }
               break;
        }
    }
    
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }
    
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        if (state.getValue(THIRD) == BigDoor.EnumDoorThird.LOWER) {
            IBlockState iblockstate = worldIn.getBlockState(pos.up(2));
            if (iblockstate.getBlock() == this) {
                state = state.withProperty(HINGE, iblockstate.getValue(HINGE)).withProperty(POWERED, iblockstate.getValue(POWERED));
            }
        } else if (state.getValue(THIRD) == BigDoor.EnumDoorThird.MIDDLE) {
            IBlockState iblockstate = worldIn.getBlockState(pos.down());
            IBlockState iblockstate1 = worldIn.getBlockState(pos.up());
            if (iblockstate1.getBlock() == this) {
                state = state.withProperty(HINGE, iblockstate1.getValue(HINGE)).withProperty(POWERED, iblockstate1.getValue(POWERED));
            }
            if (iblockstate.getBlock() == this) {
                state = state.withProperty(FACING, iblockstate.getValue(FACING)).withProperty(OPEN, iblockstate.getValue(OPEN));
            }
        } else if (state.getValue(THIRD) == BigDoor.EnumDoorThird.UPPER) {
            IBlockState iblockstate = worldIn.getBlockState(pos.down(2));
            if (iblockstate.getBlock() == this) {
                state = state.withProperty(FACING, iblockstate.getValue(FACING)).withProperty(OPEN, iblockstate.getValue(OPEN));
            }
        }
        return state;
    }
    
    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.getValue(THIRD) != BigDoor.EnumDoorThird.LOWER ? state : state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
    }
    
    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return mirrorIn == Mirror.NONE ? state : state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING))).cycleProperty(HINGE);
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
        if (((meta & 8) > 0) && ((meta & 12) < 12)) {
            return this.getDefaultState()
                    .withProperty(THIRD, BigDoor.EnumDoorThird.UPPER)
                    .withProperty(HINGE, (meta & 1) > 0 ? BigDoor.EnumHingePosition.RIGHT : BigDoor.EnumHingePosition.LEFT)
                    .withProperty(POWERED, Boolean.valueOf((meta & 2) > 0));
        } else if ((meta & 0) >= 0 && ((meta & 12) < 12)){
            return this.getDefaultState()
                    .withProperty(THIRD, BigDoor.EnumDoorThird.LOWER)
                    .withProperty(FACING, EnumFacing.getHorizontal(meta & 3).rotateYCCW())
                    .withProperty(OPEN, Boolean.valueOf((meta & 4) > 0));
        } else {
            return this.getDefaultState().withProperty(THIRD, BigDoor.EnumDoorThird.MIDDLE);
        }
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        if (state.getValue(THIRD) == BigDoor.EnumDoorThird.UPPER) {
            i = i | 8;
            if (state.getValue(HINGE) == BigDoor.EnumHingePosition.RIGHT) {
                i |= 1;
            }
            if (((Boolean)state.getValue(POWERED)).booleanValue()) {
                i |= 2;
            }
        } else if (state.getValue(THIRD) == BigDoor.EnumDoorThird.MIDDLE) {
            i = i | 12;
        } else {
            i = i | ((EnumFacing)state.getValue(FACING)).rotateY().getHorizontalIndex();

            if (((Boolean)state.getValue(OPEN)).booleanValue()) {
                i |= 4;
            }
        }
        return i;
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {THIRD, FACING, OPEN, HINGE, POWERED});
    }
    
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
    
    public static enum EnumDoorThird implements IStringSerializable {
        UPPER,
        MIDDLE,
        LOWER;

        public String toString() {
            return this.getName();
        }

        public String getName() {
            if (this == UPPER) {
                return "upper";
            } else if (this == MIDDLE) {
                return "middle";
            } else {
                return "lower";
            }
        }
    }
    
    public static enum EnumHingePosition implements IStringSerializable {
        LEFT,
        RIGHT;

        public String toString() {
            return this.getName();
        }

        public String getName() {
            return this == LEFT ? "left" : "right";
        }
    }
}
