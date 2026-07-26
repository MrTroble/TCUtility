package com.troblecodings.tcutility.blocks;

import java.util.List;

import com.troblecodings.tcutility.utils.BlockCreateInfo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

public class TCCube extends Block {

    private final VoxelShape shape;
    private final BlockRenderLayer renderLayer;

    public TCCube(final BlockCreateInfo blockInfo) {
        super(blockInfo.toProperties());
        this.shape = boxToShape(blockInfo.box);
        this.renderLayer = layerFor(blockInfo.material);
    }

    @Override
    public VoxelShape getShape(final BlockState state, final IBlockReader world, final BlockPos pos,
            final ISelectionContext context) {
        return this.shape;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return this.renderLayer;
    }

    static VoxelShape boxToShape(final List<Integer> box) {
        if (box != null && box.size() >= 6) {
            return Block.makeCuboidShape(box.get(0), box.get(1), box.get(2),
                    box.get(3), box.get(4), box.get(5));
        }
        return VoxelShapes.fullCube();
    }

    static int[] boxArr(final List<Integer> box) {
        if (box != null && box.size() >= 6) {
            return new int[] { box.get(0), box.get(1), box.get(2),
                    box.get(3), box.get(4), box.get(5) };
        }
        return new int[] { 0, 0, 0, 16, 16, 16 };
    }

    static BlockRenderLayer layerFor(final Material mat) {
        if (mat == Material.GLASS || mat == Material.ICE || mat == Material.PACKED_ICE) {
            return BlockRenderLayer.TRANSLUCENT;
        }
        if (mat == Material.LEAVES || mat == Material.PLANTS) {
            return BlockRenderLayer.CUTOUT_MIPPED;
        }
        return BlockRenderLayer.SOLID;
    }
}
