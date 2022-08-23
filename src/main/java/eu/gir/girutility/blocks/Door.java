package eu.gir.girutility.blocks;

import java.util.Random;

import eu.gir.girutility.init.GIRBlocks;
import eu.gir.girutility.init.GIRItems;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Door extends BlockDoor {
    
    public Door(Material material) {
        super (material);
    }
    
    @Override
    public String getLocalizedName() {
        return null;
    }
    
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        if (state.getValue(HALF) == Door.EnumDoorHalf.LOWER) {
            return this.getItem();
        } else {
            return Items.AIR;
        }
    }
    
    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(this.getItem());
    }
    
    private Item getItem() {
        if (this == GIRBlocks.DOOR_JAIL_BLOCK) {
            return GIRItems.DOOR_JAIL;
        } else if (this == GIRBlocks.DOOR_IRON_GLASS_BLOCK) {
            return GIRItems.DOOR_IRON_GLASS;
        } else if (this == GIRBlocks.DOOR_OAK_GLASS_BLOCK) {
            return GIRItems.DOOR_OAK_GLASS;
        } else if (this == GIRBlocks.DOOR_OAK_WINDOWED_BLOCK) {
            return GIRItems.DOOR_OAK_WINDOWED;
        } else if (this == GIRBlocks.DOOR_SPRUCE_WINDOWED_BLOCK) {
            return GIRItems.DOOR_SPRUCE_WINDOWED;
        } else if (this == GIRBlocks.DOOR_BIRCH_WINDOWED_BLOCK) {
            return GIRItems.DOOR_BIRCH_WINDOWED;
        } else if (this == GIRBlocks.DOOR_JUNGLE_WINDOWED_BLOCK) {
            return GIRItems.DOOR_JUNGLE_WINDOWED;
        } else if (this == GIRBlocks.DOOR_ACACIA_WINDOWED_BLOCK) {
            return GIRItems.DOOR_ACACIA_WINDOWED;
        } else if (this == GIRBlocks.DOOR_DARK_OAK_WINDOWED_BLOCK) {
            return GIRItems.DOOR_DARK_OAK_WINDOWED;
        } else if (this == GIRBlocks.DOOR_IRON_WINDOWED_BLOCK) {
            return GIRItems.DOOR_IRON_WINDOWED;
        } else if (this == GIRBlocks.DOOR_WHITE_WINDOWED_BLOCK) {
            return GIRItems.DOOR_WHITE_WINDOWED;
        } else if (this == GIRBlocks.DOOR_OAK_BLOCK) {
            return GIRItems.DOOR_OAK;
        } else if (this == GIRBlocks.DOOR_SPRUCE_BLOCK) {
            return GIRItems.DOOR_SPRUCE;
        } else if (this == GIRBlocks.DOOR_BIRCH_BLOCK) {
            return GIRItems.DOOR_BIRCH;
        } else if (this == GIRBlocks.DOOR_JUNGLE_BLOCK) {
            return GIRItems.DOOR_JUNGLE;
        } else if (this == GIRBlocks.DOOR_ACACIA_BLOCK) {
            return GIRItems.DOOR_ACACIA;
        } else if (this == GIRBlocks.DOOR_DARK_OAK_BLOCK) {
            return GIRItems.DOOR_DARK_OAK;
        } else if (this == GIRBlocks.DOOR_IRON_BLOCK) {
            return GIRItems.DOOR_IRON;
        } else if (this == GIRBlocks.DOOR_WHITE_BLOCK) {
            return GIRItems.DOOR_WHITE;
        } else if (this == GIRBlocks.DOOR_OLD_BLOCK) {
            return GIRItems.DOOR_OLD;
        } else if (this == GIRBlocks.DOOR_RUSTY_BLOCK) {
            return GIRItems.DOOR_RUSTY;
        } else {
            return Items.OAK_DOOR;
        }
    }
}
