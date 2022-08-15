package eu.gir.girutility.blocks;

import eu.gir.girutility.init.GIRTabs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class Door extends Block {
    
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool OPEN = PropertyBool.create("open");
    public static final PropertyEnum<Door.EnumHingePosition> HINGE = PropertyEnum.<Door.EnumHingePosition>create("hinge", Door.EnumHingePosition.class);
    public static final PropertyBool POWERED = PropertyBool.create("powered");
    public static final PropertyEnum<Door.EnumDoorHalf> HALF = PropertyEnum.<Door.EnumDoorHalf>create("half", Door.EnumDoorHalf.class);
    
    public Door(Material material) {
        super (material);
        setCreativeTab(GIRTabs.tab);
    }
    
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }
    
    @Override
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return true;
    }
    
    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }
    
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(this.getItem());
    }
    
    private Item getItem() {
        return Items.OAK_DOOR;
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {HALF, FACING, OPEN, HINGE, POWERED});
    }
    
    public static enum EnumHingePosition implements IStringSerializable {
        LEFT, RIGHT;
        
        public String toString() {
            return this.name();
        }
        
        public String getName() {
            return this == LEFT ? "left" : "right";
        }
    }
    
    public static enum EnumDoorHalf implements IStringSerializable {
        UPPER, LOWER;
        
        public String toString() {
            return this.name();
        }
        
        public String getName() {
            return this == UPPER ? "upper" : "lower";
        } 
    }
}
