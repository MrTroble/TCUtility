package com.troblecodings.tcutility.items;

import com.troblecodings.tcutility.blocks.TCSlab;
import com.troblecodings.tcutility.blocks.TCSlab.SlabType;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TCSlabItem extends ItemBlock {

    public TCSlabItem(final Block block) {
        super(block);
        ((TCSlab) block).setItem(this);
    }

    @Override
    public EnumActionResult onItemUse(final EntityPlayer player, final World world,
            final BlockPos pos, final EnumHand hand, final EnumFacing facing, final float hitX,
            final float hitY, final float hitZ) {
        ItemStack stack = player.getHeldItem(hand);

        if (tryMakeDouble(world, pos, facing, hitY, player, stack))
            return EnumActionResult.SUCCESS;

        BlockPos offsetPos = pos.offset(facing);
        if (tryMakeDouble(world, offsetPos, facing.getOpposite(), hitY, player, stack))
            return EnumActionResult.SUCCESS;

        return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean canPlaceBlockOnSide(final World world, final BlockPos pos, final EnumFacing side,
            final EntityPlayer player, final ItemStack stack) {
        BlockPos originalPos = pos;
        IBlockState state = world.getBlockState(pos);

        if (state.getBlock().equals(this.block)) {
            SlabType type = state.getValue(TCSlab.TYPE);
            if ((side.equals(EnumFacing.UP) && type.equals(SlabType.BOTTOM))
                    || (side.equals(EnumFacing.DOWN) && type.equals(SlabType.TOP)))
                return true;
        }
        BlockPos neighborPos = pos.offset(side);
        IBlockState neighborState = world.getBlockState(neighborPos);

        if (neighborState.getBlock().equals(this.block)) {
            SlabType neighborType = neighborState.getValue(TCSlab.TYPE);
            if ((side.equals(EnumFacing.DOWN) && neighborType.equals(SlabType.BOTTOM))
                    || (side.equals(EnumFacing.UP) && neighborType.equals(SlabType.TOP)))
                return true;
        }
        return super.canPlaceBlockOnSide(world, originalPos, side, player, stack);
    }

    private boolean tryMakeDouble(final World world, final BlockPos pos, final EnumFacing facing,
            final Float hitY, final EntityPlayer player, final ItemStack stack) {
        IBlockState state = world.getBlockState(pos);
        if (!state.getBlock().equals(this.block))
            return false;

        SlabType type = state.getValue(TCSlab.TYPE);
        if (type.equals(SlabType.DOUBLE))
            return false;

        boolean combine = (type.equals(SlabType.BOTTOM)
                && (facing.equals(EnumFacing.UP) || hitY > 0.5F))
                || (type.equals(SlabType.TOP) && (facing.equals(EnumFacing.DOWN) || hitY < 0.5F));

        if (!combine)
            return false;

        if (!world.isRemote) {
            world.setBlockState(pos, state.withProperty(TCSlab.TYPE, SlabType.DOUBLE), 3);

            if (!player.capabilities.isCreativeMode) {
                stack.shrink(1);
            }
        }
        return true;
    }

    @Override
    public int getMetadata(final int damage) {
        return 0;
    }
}
