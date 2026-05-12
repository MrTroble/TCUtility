package com.troblecodings.tcutility.fluids;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

/**
 * 1:1-Replikation des 1.12.2-Forge-Verhaltens fuer "Gas"-Fluids (negative density), portiert
 * auf die Vanilla-{@link net.minecraft.world.level.material.FlowingFluid}-API:
 *
 * <ul>
 *   <li><b>Vertikal nach oben</b> erhaelt {@code LEVEL=quantaPerBlock-1} (=7) -- jede Zelle mit
 *       gleichem Fluid darunter bekommt automatisch vollen Quanta-Wert. Dadurch waechst die
 *       Spalte unbegrenzt nach oben (bis Block oder Build-Limit), waehrend horizontal limitiert
 *       bleibt.</li>
 *   <li><b>Horizontal</b> dekrementiert wie Vanilla um {@code dropOff}; die Reichweite ist durch
 *       {@code sourceQuanta = clamp(flowLength, 2, 8)} aus der JSON-{@code fluiddefinitions}
 *       gesteuert: Nachbarn der Quelle erfahren {@code sourceQuanta} statt 8 (Vanilla-Source) und
 *       der Strom bricht nach {@code sourceQuanta - 1} Bloeken ab.</li>
 *   <li><b>Mitte einer Aufwaerts-Spalte</b> (above ist dieselbe Fluid-Type, Up ist also blockiert
 *       weil same-fluid nicht ersetzbar ist) verteilt <i>nicht</i> horizontal -- nur Source und
 *       die Spitze einer Spalte spreaden seitlich. Vermeidet, dass jede Zelle der Saeule lateral
 *       wuchert.</li>
 * </ul>
 *
 * <p>Source-Konvergenz zu neuen Source-Bloecken (wie bei Wasser) gibt es bewusst nicht; gas-
 * artige Fluids im 1.12.2-Modell vermehren sich nur ausgehend von der gesetzten Quelle.
 */
public abstract class TCUpwardFlowingFluid extends BaseFlowingFluid {

    /** Wie viele Quanta die Quelle effektiv ans Umfeld weitergibt (limitiert horizontale Reichweite). */
    private final int sourceQuanta;

    protected TCUpwardFlowingFluid(final Properties properties, final int flowLength) {
        super(properties);
        this.sourceQuanta = Math.max(2, Math.min(8, flowLength));
    }

    /** Vanilla-{@link net.minecraft.world.level.material.FlowingFluid#LEVEL} ist 1..8; 7 ist
     *  "voll-fliessend". Ein Block in einer Aufwaerts-Spalte rendert damit als full block. */
    private static final int COLUMN_LEVEL = 7;

    @Override
    protected FluidState getNewLiquid(final ServerLevel level, final BlockPos pos,
            final BlockState state) {
        // 1.12.2-hasVerticalFlow: same fluid darunter -> Level wird auf voll geclampt
        final FluidState below = level.getFluidState(pos.below());
        if (below.getType().isSame(this) && !below.isEmpty()) {
            return this.getFlowing(COLUMN_LEVEL, false);
        }

        // Sonst: max horizontaler Nachbar - dropOff
        int maxAdjacent = 0;
        for (final Direction dir : Direction.Plane.HORIZONTAL) {
            final FluidState neighbor = level.getFluidState(pos.relative(dir));
            if (neighbor.getType().isSame(this) && !neighbor.isEmpty()) {
                final int q = neighbor.isSource() ? this.sourceQuanta : neighbor.getAmount();
                maxAdjacent = Math.max(maxAdjacent, q);
            }
        }
        final int newLevel = maxAdjacent - this.getDropOff(level);
        if (newLevel <= 0) {
            return Fluids.EMPTY.defaultFluidState();
        }
        return this.getFlowing(newLevel, false);
    }

    @Override
    protected void spread(final ServerLevel level, final BlockPos pos, final BlockState here,
            final FluidState state) {
        if (state.isEmpty()) {
            return;
        }

        // 1) UP first: Spalte waechst mit fixed COLUMN_LEVEL (Quanta 7).
        //    1.21.4: canSpreadTo ist privat geworden -- wir pruefen direkt, ob das
        //    Ziel-FluidState durch unseren neuen Fluid ersetzt werden kann.
        final BlockPos abovePos = pos.above();
        final BlockState aboveState = level.getBlockState(abovePos);
        final FluidState aboveFluid = level.getFluidState(abovePos);
        final FluidState upFlow = this.getFlowing(COLUMN_LEVEL, false);
        if (canFlowInto(level, abovePos, aboveState, aboveFluid, upFlow.getType(), Direction.UP)) {
            this.spreadTo(level, abovePos, aboveState, Direction.UP, upFlow);
            return; // 1.12 early-return: vertikaler Flow unterdrueckt horizontalen im selben Tick
        }

        // 2) Up versperrt durch eigene Fluid-Saeule -> wir sind in der Mitte, NICHT seitlich spreaden.
        if (aboveFluid.getType().isSame(this)) {
            return;
        }

        // 3) Top der Saeule (oder Source unter Decke): seitlich mit dekrementiertem Level streuen.
        final int currentLevel = state.isSource() ? this.sourceQuanta : state.getAmount();
        final int sideLevel = currentLevel - this.getDropOff(level);
        if (sideLevel <= 0) {
            return;
        }
        final FluidState sideFlow = this.getFlowing(sideLevel, false);
        for (final Direction dir : Direction.Plane.HORIZONTAL) {
            final BlockPos sidePos = pos.relative(dir);
            final BlockState sideState = level.getBlockState(sidePos);
            final FluidState sideFluid = level.getFluidState(sidePos);
            if (canFlowInto(level, sidePos, sideState, sideFluid, sideFlow.getType(), dir)) {
                this.spreadTo(level, sidePos, sideState, dir, sideFlow);
            }
        }
    }

    /**
     * Vereinfachter Ersatz fuer das in 1.21.4 privat gewordene {@code canSpreadTo}: wir pruefen,
     * ob (a) das Ziel-FluidState durch den neuen Fluid in der gegebenen Richtung ersetzbar ist,
     * (b) der Block dort Air ist oder selbst replaceable. Wand-Pass-Through-Logik (Fences/Walls)
     * lassen wir aus -- fuer Steam ist das in der Praxis irrelevant.
     */
    private boolean canFlowInto(final ServerLevel level, final BlockPos targetPos,
            final BlockState targetState, final FluidState targetFluid, final Fluid newFluid,
            final Direction direction) {
        if (!targetFluid.canBeReplacedWith(level, targetPos, newFluid, direction)) {
            return false;
        }
        return targetState.isAir() || targetState.canBeReplaced();
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
        public int getAmount(final FluidState state) {
            return 8;
        }
    }

    public static class Flowing extends TCUpwardFlowingFluid {

        public Flowing(final Properties properties, final int flowLength) {
            super(properties, flowLength);
        }

        @Override
        protected void createFluidStateDefinition(
                final StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }

        @Override
        public boolean isSource(final FluidState state) {
            return false;
        }

        @Override
        public int getAmount(final FluidState state) {
            return state.getValue(LEVEL);
        }
    }
}
