package com.troblecodings.tcutility.blocks;

import java.util.List;

import com.troblecodings.tcutility.init.TCTabs;
import com.troblecodings.tcutility.utils.BlockCreateInfo;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TCCube extends Block {
	
	private List<Integer> box;
    
    public TCCube(final BlockCreateInfo blockInfo) {
        super(blockInfo.material);
        this.setHardness(blockInfo.hardness);
        this.setSoundType(blockInfo.soundtype);
        this.setLightOpacity(blockInfo.opacity);
        this.setCreativeTab(TCTabs.BLOCKS);
        this.lightValue = blockInfo.lightValue;
        this.box = blockInfo.box;
    }

    @SuppressWarnings("deprecation")
    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        if (this.getMaterial(getDefaultState()).equals(Material.GLASS)) {
            return BlockRenderLayer.TRANSLUCENT;
        } else if (this.getMaterial(getDefaultState()).equals(Material.ANVIL)) {
            return BlockRenderLayer.CUTOUT_MIPPED;
        }
        return BlockRenderLayer.SOLID;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(final IBlockState state) {
        if (this.getMaterial(getDefaultState()).equals(Material.GLASS)
                || this.getMaterial(getDefaultState()).equals(Material.ANVIL)) {
            return false;
        } else
            return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(final IBlockState state) {
        if (this.getMaterial(getDefaultState()).equals(Material.GLASS)
                || this.getMaterial(getDefaultState()).equals(Material.ANVIL)) {
            return false;
        } else
            return true;
    }
    
    @Override
    public Block setLightLevel(final float value) {
        return super.setLightLevel(lightValue);
    }
    
    public int getIndexBox(int index) {
		return box.get(index);
    }
    
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    	return new AxisAlignedBB(getIndexBox(0) * 0.0625, getIndexBox(1) * 0.0625, getIndexBox(2) * 0.0625,
        		getIndexBox(3) * 0.0625, getIndexBox(4) * 0.0625, getIndexBox(5) * 0.0625);
    }
}
