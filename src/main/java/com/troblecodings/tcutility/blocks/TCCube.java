package com.troblecodings.tcutility.blocks;

import com.troblecodings.tcutility.init.TCTabs;
import com.troblecodings.tcutility.utils.BlockCreateInfo;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TCCube extends Block {

    public TCCube(final BlockCreateInfo blockInfo) {
        super(blockInfo.material);
        this.setHardness(blockInfo.hardness);
        this.setSoundType(blockInfo.soundtype);
        this.setLightOpacity(blockInfo.opacity);
        this.setCreativeTab(TCTabs.BLOCKS);
    }

    @SuppressWarnings("deprecation")
    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        if (this.getMaterial(getDefaultState()).equals(Material.GLASS)) {
            return BlockRenderLayer.TRANSLUCENT;
        }
        return BlockRenderLayer.SOLID;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(final IBlockState state) {
        if (this.getMaterial(getDefaultState()).equals(Material.GLASS)) {
            return false;
        } else
            return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(final IBlockState state) {
        if (this.getMaterial(getDefaultState()).equals(Material.GLASS)) {
            return false;
        } else
            return true;
    }

}
