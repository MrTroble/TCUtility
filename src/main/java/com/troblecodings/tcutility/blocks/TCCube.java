package com.troblecodings.tcutility.blocks;

import java.util.List;

import com.troblecodings.tcutility.utils.BlockCreateInfo;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;

/**
 * Basis-Cube. Render-Layer wird ab 1.15 nicht mehr im Block selbst, sondern
 * client-seitig in {@code TCRenderTypes} pro Material gesetzt.
 */
public class TCCube extends Block {

    private final VoxelShape shape;

    public TCCube(final BlockCreateInfo blockInfo) {
        super(blockInfo.toProperties());
        this.shape = boxToShape(blockInfo.box);
    }

    @Override
    public VoxelShape getShape(final BlockState state, final BlockGetter world, final BlockPos pos,
            final CollisionContext context) {
        return this.shape;
    }

    static VoxelShape boxToShape(final List<Integer> box) {
        if (box != null && box.size() >= 6) {
            return Block.box(box.get(0), box.get(1), box.get(2),
                    box.get(3), box.get(4), box.get(5));
        }
        return Shapes.block();
    }

    static int[] boxArr(final List<Integer> box) {
        if (box != null && box.size() >= 6) {
            return new int[] { box.get(0), box.get(1), box.get(2),
                    box.get(3), box.get(4), box.get(5) };
        }
        return new int[] { 0, 0, 0, 16, 16, 16 };
    }
}
