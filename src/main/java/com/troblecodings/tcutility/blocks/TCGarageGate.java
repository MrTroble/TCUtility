package com.troblecodings.tcutility.blocks;

import com.troblecodings.tcutility.utils.BlockCreateInfo;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.Level;

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
    public InteractionResult use(final BlockState state, final Level world,
            final BlockPos pos, final Player player, final InteractionHand hand,
            final BlockHitResult hit) {
        for (int i = 1; i < MAX_REACH; i++) {
            final BlockPos above = pos.above(i);
            final BlockState aboveState = world.getBlockState(above);
            if (aboveState.getBlock() instanceof TCGarageDoor) {
                ((TCGarageDoor) aboveState.getBlock()).toggleAt(world, above, aboveState);
                return InteractionResult.sidedSuccess(world.isClientSide);
            }
            if (!(aboveState.getBlock() instanceof TCGarageGate)) {
                // Block dazwischen, der weder Gate noch Header ist -- abbrechen.
                break;
            }
        }
        // Hier folgt das 1.12.2-Verhalten: ein Klick auf ein Gate wird auch
        // dann als verarbeitet gemeldet, wenn kein Header gefunden wurde --
        // verhindert, dass man "durch" das Gate ungewollt einen Block setzt.
        return InteractionResult.sidedSuccess(world.isClientSide);
    }

    @Override
    public void playerWillDestroy(final Level world, final BlockPos pos, final BlockState state,
            final Player player) {
        // Saeule nach unten: weitere Gate-Segmente entfernen.
        for (int i = 1; i < MAX_REACH; i++) {
            final BlockPos below = pos.below(i);
            if (!(world.getBlockState(below).getBlock() instanceof TCGarageGate)) {
                break;
            }
            world.setBlock(below, Blocks.AIR.defaultBlockState(), 35);
        }
        // Saeule nach oben: weitere Gate-Segmente plus den Header daruber.
        for (int i = 1; i < MAX_REACH; i++) {
            final BlockPos above = pos.above(i);
            final BlockState aboveState = world.getBlockState(above);
            if (aboveState.getBlock() instanceof TCGarageGate) {
                world.setBlock(above, Blocks.AIR.defaultBlockState(), 35);
                continue;
            }
            if (aboveState.getBlock() instanceof TCGarageDoor) {
                world.setBlock(above, Blocks.AIR.defaultBlockState(), 35);
            }
            break;
        }
        super.playerWillDestroy(world, pos, state, player);
    }
}
