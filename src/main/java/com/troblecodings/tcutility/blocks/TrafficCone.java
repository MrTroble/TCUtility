package com.troblecodings.tcutility.blocks;

import com.troblecodings.tcutility.init.TCTabs;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class TrafficCone extends Block {

    private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0.125, -0.25, 0.125, 0.875,
            14 * 0.0625 - 0.25, 0.875);

    public TrafficCone() {
        super(Material.CLAY, MapColor.ORANGE_STAINED_HARDENED_CLAY);
        this.setHardness(0.5F);
        setCreativeTab(TCTabs.SPECIAL);
    }

    @Override
    public boolean isOpaqueCube(final IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(final IBlockState state) {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source,
            final BlockPos pos) {
        return BOUNDING_BOX;
    }

}
