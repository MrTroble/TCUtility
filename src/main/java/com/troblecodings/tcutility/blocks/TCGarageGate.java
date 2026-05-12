package com.troblecodings.tcutility.blocks;

import com.troblecodings.tcutility.utils.BlockCreateInfo;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

/**
 * Garage-Gate: einzelnes Segment in der Rolltor-Saeule eines
 * {@link TCGarageDoor}. Vom rotierten Cube her vom {@link TCCubeRotation}
 * abgeleitet (FACING-State + direction-spezifische Hitbox); zusaetzlich
 * (wie im 1.12.2-Original):
 *
 * <ul>
 *   <li>Klickt der Spieler auf ein Segment, sucht der Block bis zu 10
 *       Bloecke nach oben den passenden {@link TCGarageDoor}-Header und
 *       triggert dort das Auf-/Zumachen -- Folge: man kann das Tor an
 *       jedem Punkt der Saeule oeffnen, nicht nur am Header.</li>
 *   <li>Wird ein Segment zerstoert, raeumt der Block die uebrigen Gate-
 *       Segmente in der gleichen Saeule (oben + unten bis 10) sowie den
 *       Header darueber mit auf. So bleibt nie ein "halbes" Rolltor
 *       freischwebend uebrig.</li>
 * </ul>
 */
public class TCGarageGate extends TCCubeRotation {

    private static final int MAX_REACH = 10;

    public TCGarageGate(final BlockCreateInfo blockInfo) {
        super(blockInfo);
    }

    @Override
    public ActionResultType onBlockActivated(final BlockState state, final World world,
            final BlockPos pos, final PlayerEntity player, final Hand hand,
            final BlockRayTraceResult hit) {
        for (int i = 1; i < MAX_REACH; i++) {
            final BlockPos above = pos.up(i);
            final BlockState aboveState = world.getBlockState(above);
            if (aboveState.getBlock() instanceof TCGarageDoor) {
                ((TCGarageDoor) aboveState.getBlock()).toggleAt(world, above, aboveState);
                return ActionResultType.func_233537_a_(world.isRemote);
            }
            if (!(aboveState.getBlock() instanceof TCGarageGate)) {
                // Block dazwischen, der weder Gate noch Header ist -- abbrechen.
                break;
            }
        }
        // Hier folgt das 1.12.2-Verhalten: ein Klick auf ein Gate wird auch
        // dann als verarbeitet gemeldet, wenn kein Header gefunden wurde --
        // verhindert, dass man "durch" das Gate ungewollt einen Block setzt.
        return ActionResultType.func_233537_a_(world.isRemote);
    }

    @Override
    public void onBlockHarvested(final World world, final BlockPos pos, final BlockState state,
            final PlayerEntity player) {
        // Saeule nach unten: weitere Gate-Segmente entfernen.
        for (int i = 1; i < MAX_REACH; i++) {
            final BlockPos below = pos.down(i);
            if (!(world.getBlockState(below).getBlock() instanceof TCGarageGate)) {
                break;
            }
            world.setBlockState(below, Blocks.AIR.getDefaultState(), 35);
        }
        // Saeule nach oben: weitere Gate-Segmente plus den Header daruber.
        for (int i = 1; i < MAX_REACH; i++) {
            final BlockPos above = pos.up(i);
            final BlockState aboveState = world.getBlockState(above);
            if (aboveState.getBlock() instanceof TCGarageGate) {
                world.setBlockState(above, Blocks.AIR.getDefaultState(), 35);
                continue;
            }
            if (aboveState.getBlock() instanceof TCGarageDoor) {
                world.setBlockState(above, Blocks.AIR.getDefaultState(), 35);
            }
            break;
        }
        super.onBlockHarvested(world, pos, state, player);
    }
}
