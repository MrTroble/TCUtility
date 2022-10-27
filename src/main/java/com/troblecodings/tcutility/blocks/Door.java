package com.troblecodings.tcutility.blocks;

import java.util.Random;

import com.troblecodings.tcutility.init.TCBlocks;
import com.troblecodings.tcutility.init.TCItems;

import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Door extends BlockDoor {

    public Door(final Material material) {
        super(material);
    }

    @Override
    public String getLocalizedName() {
        return null;
    }

    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        if (state.getValue(HALF) == Door.EnumDoorHalf.LOWER) {
            return this.getItem();
        } else {
            return Items.AIR;
        }
    }

    @Override
    public ItemStack getItem(final World worldIn, final BlockPos pos, final IBlockState state) {
        return new ItemStack(this.getItem());
    }

    private Item getItem() {
        if (this == TCBlocks.DOOR_JAIL_BLOCK) {
            return TCItems.DOOR_JAIL;
        } else if (this == TCBlocks.DOOR_IRON_GLASS_BLOCK) {
            return TCItems.DOOR_IRON_GLASS;
        } else if (this == TCBlocks.DOOR_OAK_GLASS_BLOCK) {
            return TCItems.DOOR_OAK_GLASS;
        } else if (this == TCBlocks.DOOR_OAK_WINDOWED_BLOCK) {
            return TCItems.DOOR_OAK_WINDOWED;
        } else if (this == TCBlocks.DOOR_SPRUCE_WINDOWED_BLOCK) {
            return TCItems.DOOR_SPRUCE_WINDOWED;
        } else if (this == TCBlocks.DOOR_BIRCH_WINDOWED_BLOCK) {
            return TCItems.DOOR_BIRCH_WINDOWED;
        } else if (this == TCBlocks.DOOR_JUNGLE_WINDOWED_BLOCK) {
            return TCItems.DOOR_JUNGLE_WINDOWED;
        } else if (this == TCBlocks.DOOR_ACACIA_WINDOWED_BLOCK) {
            return TCItems.DOOR_ACACIA_WINDOWED;
        } else if (this == TCBlocks.DOOR_DARK_OAK_WINDOWED_BLOCK) {
            return TCItems.DOOR_DARK_OAK_WINDOWED;
        } else if (this == TCBlocks.DOOR_IRON_WINDOWED_BLOCK) {
            return TCItems.DOOR_IRON_WINDOWED;
        } else if (this == TCBlocks.DOOR_WHITE_WINDOWED_BLOCK) {
            return TCItems.DOOR_WHITE_WINDOWED;
        } else if (this == TCBlocks.DOOR_OAK_BLOCK) {
            return TCItems.DOOR_OAK;
        } else if (this == TCBlocks.DOOR_SPRUCE_BLOCK) {
            return TCItems.DOOR_SPRUCE;
        } else if (this == TCBlocks.DOOR_BIRCH_BLOCK) {
            return TCItems.DOOR_BIRCH;
        } else if (this == TCBlocks.DOOR_JUNGLE_BLOCK) {
            return TCItems.DOOR_JUNGLE;
        } else if (this == TCBlocks.DOOR_ACACIA_BLOCK) {
            return TCItems.DOOR_ACACIA;
        } else if (this == TCBlocks.DOOR_DARK_OAK_BLOCK) {
            return TCItems.DOOR_DARK_OAK;
        } else if (this == TCBlocks.DOOR_IRON_BLOCK) {
            return TCItems.DOOR_IRON;
        } else if (this == TCBlocks.DOOR_WHITE_BLOCK) {
            return TCItems.DOOR_WHITE;
        } else if (this == TCBlocks.DOOR_OLD_BLOCK) {
            return TCItems.DOOR_OLD;
        } else if (this == TCBlocks.DOOR_RUSTY_BLOCK) {
            return TCItems.DOOR_RUSTY;
        } else {
            return Items.OAK_DOOR;
        }
    }
}
