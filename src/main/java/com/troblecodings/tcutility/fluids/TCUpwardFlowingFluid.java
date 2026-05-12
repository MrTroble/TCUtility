package com.troblecodings.tcutility.fluids;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.fluids.ForgeFlowingFluid;

/**
 * 1:1-Replikation des 1.12.2-Forge-Verhaltens fuer "Gas"-Fluids (negative density), portiert
 * auf die 1.16.5-{@link net.minecraft.fluid.FlowingFluid}-API:
 *
 * <ul>
 *   <li>vertikal nach oben: jede Zelle mit gleichem Fluid darunter wird auf Level 7 geclampt
 *       -- die Spalte waechst unbegrenzt nach oben (bis Block oder Build-Limit).</li>
 *   <li>horizontal: dekrementiert um {@code getLevelDecreasePerBlock}, Reichweite gesteuert
 *       durch {@code sourceQuanta = clamp(flowLength, 2, 8)} aus den fluiddefinitions; Nachbarn
 *       der Source erfahren {@code sourceQuanta} statt 8.</li>
 *   <li>Mitte einer Aufwaerts-Spalte (above ist dieselbe Fluid-Type) verteilt nicht horizontal
 *       -- nur Source und Saulen-Spitze spreaden seitlich.</li>
 * </ul>
 *
 * <p>1.16.5-Mappings: {@code spread} -> {@code flowAround},
 * {@code getNewLiquid} -> {@code calculateCorrectFlowingState},
 * {@code spreadTo} -> {@code flowInto}, {@code canSpreadTo} -> {@code canFlow},
 * {@code getDropOff} -> {@code getLevelDecreasePerBlock}.
 */
public abstract class TCUpwardFlowingFluid extends ForgeFlowingFluid {

    private final int sourceQuanta;

    protected TCUpwardFlowingFluid(final Properties properties, final int flowLength) {
        super(properties);
        this.sourceQuanta = Math.max(2, Math.min(8, flowLength));
    }

    private static final int COLUMN_LEVEL = 7;

    @Override
    protected FluidState calculateCorrectFlowingState(final IWorldReader world, final BlockPos pos,
            final BlockState state) {
        final FluidState below = world.getFluidState(pos.down());
        if (!below.isEmpty() && below.getFluid().isEquivalentTo(this)) {
            return this.getFlowingFluidState(COLUMN_LEVEL, false);
        }

        int maxAdjacent = 0;
        for (final Direction dir : Direction.Plane.HORIZONTAL) {
            final FluidState neighbor = world.getFluidState(pos.offset(dir));
            if (!neighbor.isEmpty() && neighbor.getFluid().isEquivalentTo(this)) {
                final int q = neighbor.isSource() ? this.sourceQuanta : neighbor.getLevel();
                maxAdjacent = Math.max(maxAdjacent, q);
            }
        }
        final int newLevel = maxAdjacent - this.getLevelDecreasePerBlock(world);
        if (newLevel <= 0) {
            return Fluids.EMPTY.getDefaultState();
        }
        return this.getFlowingFluidState(newLevel, false);
    }

    @Override
    protected void flowAround(final IWorld world, final BlockPos pos, final FluidState state) {
        if (state.isEmpty()) {
            return;
        }
        final BlockState here = world.getBlockState(pos);

        final BlockPos abovePos = pos.up();
        final BlockState aboveState = world.getBlockState(abovePos);
        final FluidState aboveFluid = world.getFluidState(abovePos);
        final FluidState upFlow = this.getFlowingFluidState(COLUMN_LEVEL, false);
        if (this.canFlow(world, pos, here, Direction.UP, abovePos, aboveState, aboveFluid,
                upFlow.getFluid())) {
            this.flowInto(world, abovePos, aboveState, Direction.UP, upFlow);
            return;
        }

        // Mitten in der Saeule (above ist same fluid) -> nicht seitlich verteilen.
        if (aboveFluid.getFluid().isEquivalentTo(this)) {
            return;
        }

        final int currentLevel = state.isSource() ? this.sourceQuanta : state.getLevel();
        final int sideLevel = currentLevel - this.getLevelDecreasePerBlock(world);
        if (sideLevel <= 0) {
            return;
        }
        final FluidState sideFlow = this.getFlowingFluidState(sideLevel, false);
        for (final Direction dir : Direction.Plane.HORIZONTAL) {
            final BlockPos sidePos = pos.offset(dir);
            final BlockState sideState = world.getBlockState(sidePos);
            final FluidState sideFluid = world.getFluidState(sidePos);
            if (this.canFlow(world, pos, here, dir, sidePos, sideState, sideFluid,
                    sideFlow.getFluid())) {
                this.flowInto(world, sidePos, sideState, dir, sideFlow);
            }
        }
    }

    public static class Source extends TCUpwardFlowingFluid {

        public Source(final Properties properties, final int flowLength) {
            super(properties, flowLength);
        }

        @Override
        public boolean isSource(final FluidState state) {
            return true;
        }

        @Override
        public int getLevel(final FluidState state) {
            return 8;
        }
    }

    public static class Flowing extends TCUpwardFlowingFluid {

        public Flowing(final Properties properties, final int flowLength) {
            super(properties, flowLength);
        }

        @Override
        protected void fillStateContainer(final StateContainer.Builder<Fluid, FluidState> builder) {
            super.fillStateContainer(builder);
            builder.add(LEVEL_1_8);
        }

        @Override
        public boolean isSource(final FluidState state) {
            return false;
        }

        @Override
        public int getLevel(final FluidState state) {
            return state.get(LEVEL_1_8);
        }
    }
}
