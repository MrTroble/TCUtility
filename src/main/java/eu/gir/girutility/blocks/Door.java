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
        if (this == GIRBlocks.DOOR1_BLOCK) {
            return GIRItems.DOOR1;
        } else if (this == GIRBlocks.DOOR_JAIL_BLOCK) {
            return GIRItems.DOOR_JAIL;
        } else {
            return Items.OAK_DOOR;
        }
    }
}
