package eu.gir.girutility.items;

import eu.gir.girutility.blocks.BigDoor;
import eu.gir.girutility.init.GIRTabs;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBigDoor extends Item {

    private final Block block;
    
    public ItemBigDoor(Block block) {
        this.block = block;
        setCreativeTab(GIRTabs.tab);
    }
    
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (facing != EnumFacing.UP) {
            return EnumActionResult.FAIL;
        } else {
            IBlockState iblockstate = worldIn.getBlockState(pos);
            Block block = iblockstate.getBlock();

            if (!block.isReplaceable(worldIn, pos)) {
                pos = pos.offset(facing);
            }

            ItemStack itemstack = player.getHeldItem(hand);

            if (player.canPlayerEdit(pos, facing, itemstack) && this.block.canPlaceBlockAt(worldIn, pos)) {
                EnumFacing enumfacing = EnumFacing.fromAngle((double)player.rotationYaw);
                int i = enumfacing.getFrontOffsetX();
                int j = enumfacing.getFrontOffsetZ();
                boolean flag = i < 0 && hitZ < 0.5F || i > 0 && hitZ > 0.5F || j < 0 && hitX > 0.5F || j > 0 && hitX < 0.5F;
                placeDoor(worldIn, pos, enumfacing, this.block, flag);
                SoundType soundtype = worldIn.getBlockState(pos).getBlock().getSoundType(worldIn.getBlockState(pos), worldIn, pos, player);
                worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                itemstack.shrink(1);
                return EnumActionResult.SUCCESS;
            } else {
                return EnumActionResult.FAIL;
            }
        }
    }
    
    public static void placeDoor(World worldIn, BlockPos pos, EnumFacing facing, Block door, boolean isRightHinge) {
        BlockPos blockpos = pos.offset(facing.rotateY());
        BlockPos blockpos1 = pos.offset(facing.rotateYCCW());
        //BlockPos blockpos2 = pos.offset(facing.rotateYCCW());
        int i = (worldIn.getBlockState(blockpos).isNormalCube() ? 1 : 0) + (worldIn.getBlockState(blockpos.up()).isNormalCube() ? 1 : 0);
        int j = (worldIn.getBlockState(blockpos1).isNormalCube() ? 1 : 0) + (worldIn.getBlockState(blockpos1.up()).isNormalCube() ? 1 : 0);
        //int k = (worldIn.getBlockState(blockpos2).isNormalCube() ? 1 : 0) + (worldIn.getBlockState(blockpos2.up(2)).isNormalCube() ? 1 : 0);
        boolean flag = worldIn.getBlockState(blockpos).getBlock() == door || worldIn.getBlockState(blockpos.up()).getBlock() == door;
        boolean flag1 = worldIn.getBlockState(blockpos1).getBlock() == door || worldIn.getBlockState(blockpos1.up()).getBlock() == door;
        //boolean flag2 = worldIn.getBlockState(blockpos2).getBlock() == door || worldIn.getBlockState(blockpos2.up(2)).getBlock() == door;
        
        if ((!flag1 || flag) && i <= j) {
            if (flag && !flag1 || i < j) {
                isRightHinge = false;
            }
        } else {
            isRightHinge = true;
        }

        BlockPos blockpos3 = pos.up();
        BlockPos blockpos4 = pos.up(2);
        boolean flag3 = worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(blockpos3);
        IBlockState iblockstate = door.getDefaultState().withProperty(BigDoor.FACING, facing).withProperty(BigDoor.HINGE, isRightHinge ? BigDoor.EnumHingePosition.RIGHT : BigDoor.EnumHingePosition.LEFT).withProperty(BigDoor.POWERED, Boolean.valueOf(flag3)).withProperty(BigDoor.OPEN, Boolean.valueOf(flag3));
        worldIn.setBlockState(pos, iblockstate.withProperty(BigDoor.THIRD, BigDoor.EnumDoorThird.LOWER), 2);
        worldIn.setBlockState(blockpos3, iblockstate.withProperty(BigDoor.THIRD, BigDoor.EnumDoorThird.MIDDLE), 2);
        worldIn.setBlockState(blockpos4, iblockstate.withProperty(BigDoor.THIRD, BigDoor.EnumDoorThird.UPPER), 2);
        worldIn.notifyNeighborsOfStateChange(pos, door, false);
        worldIn.notifyNeighborsOfStateChange(blockpos3, door, false);
        worldIn.notifyNeighborsOfStateChange(blockpos4, door, false);
    }
}
