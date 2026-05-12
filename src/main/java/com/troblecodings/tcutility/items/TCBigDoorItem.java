package com.troblecodings.tcutility.items;

import com.troblecodings.tcutility.blocks.TCBigDoor;
import com.troblecodings.tcutility.blocks.TCBigDoor.BigDoorThird;
import com.troblecodings.tcutility.init.TCTabs;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;

/**
 * Setzt beim Klicken den dreistoeckigen Cluster eines {@link TCBigDoor}.
 * 1:1-Port der Vanilla-1.12-Door-Placement-Heuristik (Block neben Tuer +
 * normalcube-Counts links/rechts), damit Doppel-Tueren wieder als
 * Doppel-Tuer einrasten und nicht ineinander rendern.
 */
public class TCBigDoorItem extends Item {

    private final TCBigDoor door;

    public TCBigDoorItem(final Block block) {
        super(new Item.Properties().tab(TCTabs.DOORS));
        this.door = (TCBigDoor) block;
        this.door.setItem(this);
    }

    public Block getBlock() {
        return door;
    }

    @Override
    public InteractionResult useOn(final UseOnContext ctx) {
        if (ctx.getClickedFace() != Direction.UP) {
            return InteractionResult.FAIL;
        }
        final Level world = ctx.getLevel();
        final Player player = ctx.getPlayer();
        if (player == null) {
            return InteractionResult.FAIL;
        }
        final ItemStack stack = player.getItemInHand(ctx.getHand());

        // Wenn der angeklickte Block ersetzbar ist (Gras/Schnee), wird er
        // selbst zur LOWER-Position; sonst LOWER = clicked + face. Vanilla-
        // Verhalten aus 1.12.
        BlockPos pos = ctx.getClickedPos();
        final BlockState clicked = world.getBlockState(pos);
        if (!clicked.getMaterial().isReplaceable()) {
            pos = pos.relative(ctx.getClickedFace());
        }
        final BlockPos middle = pos.above();
        final BlockPos upper = pos.above(2);

        if (!world.getBlockState(pos).getMaterial().isReplaceable()
                || !world.getBlockState(middle).getMaterial().isReplaceable()
                || !world.getBlockState(upper).getMaterial().isReplaceable()) {
            return InteractionResult.FAIL;
        }
        // Solider Untergrund noetig.
        if (!world.getBlockState(pos.below()).getMaterial().isSolid()) {
            return InteractionResult.FAIL;
        }
        if (!player.mayUseItemAt(pos, ctx.getClickedFace(), stack)) {
            return InteractionResult.FAIL;
        }

        final Direction facing = player.getDirection();
        final Vec3 hit = ctx.getClickLocation();
        final double hitX = hit.x - ctx.getClickedPos().getX();
        final double hitZ = hit.z - ctx.getClickedPos().getZ();

        // Initiale isRightHinge-Vermutung aus relativer Klick-Position --
        // 1:1 wie in der Vanilla-1.12-Door-Logik.
        final int xOff = facing.getStepX();
        final int zOff = facing.getStepZ();
        final boolean initialRightHinge = (xOff < 0 && hitZ < 0.5D)
                || (xOff > 0 && hitZ > 0.5D)
                || (zOff < 0 && hitX > 0.5D)
                || (zOff > 0 && hitX < 0.5D);

        placeDoor(world, pos, facing, door, initialRightHinge);

        final BlockState placedLower = world.getBlockState(pos);
        final SoundType soundtype = placedLower.getBlock().getSoundType(placedLower, world, pos,
                player);
        world.playSound(player, pos, soundtype.getPlaceSound(), SoundSource.BLOCKS,
                (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);

        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        return InteractionResult.SUCCESS;
    }

    /**
     * Port der Vanilla-1.12-{@code BlockDoor.placeDoor}-Hinge-Heuristik,
     * mit der Original-Door-Klasse als Vergleichsblock (statt
     * {@code instanceof TCBigDoor}, sonst stoeren sich benachbarte
     * Door-Varianten gegenseitig).
     */
    private static void placeDoor(final Level world, final BlockPos pos, final Direction facing,
            final TCBigDoor door, boolean isRightHinge) {
        final BlockPos right = pos.relative(facing.getClockWise());
        final BlockPos left = pos.relative(facing.getCounterClockWise());
        final int rightCubes = (world.getBlockState(right).isRedstoneConductor(world, right) ? 1 : 0)
                + (world.getBlockState(right.above()).isRedstoneConductor(world, right.above()) ? 1 : 0);
        final int leftCubes = (world.getBlockState(left).isRedstoneConductor(world, left) ? 1 : 0)
                + (world.getBlockState(left.above()).isRedstoneConductor(world, left.above()) ? 1 : 0);
        final boolean rightIsDoor = world.getBlockState(right).getBlock() == door
                || world.getBlockState(right.above()).getBlock() == door;
        final boolean leftIsDoor = world.getBlockState(left).getBlock() == door
                || world.getBlockState(left.above()).getBlock() == door;

        if ((!leftIsDoor || rightIsDoor) && rightCubes <= leftCubes) {
            if ((rightIsDoor && !leftIsDoor) || rightCubes < leftCubes) {
                isRightHinge = false;
            }
        } else {
            isRightHinge = true;
        }

        final BlockPos middle = pos.above();
        final BlockPos upper = pos.above(2);
        final boolean powered = world.hasNeighborSignal(pos) || world.hasNeighborSignal(middle);
        final BlockState base = door.defaultBlockState()
                .setValue(TCBigDoor.FACING, facing)
                .setValue(TCBigDoor.HINGE,
                        isRightHinge ? DoorHingeSide.RIGHT : DoorHingeSide.LEFT)
                .setValue(TCBigDoor.POWERED, Boolean.valueOf(powered))
                .setValue(TCBigDoor.OPEN, Boolean.valueOf(powered));
        world.setBlock(pos, base.setValue(TCBigDoor.THIRD, BigDoorThird.LOWER), 2);
        world.setBlock(middle, base.setValue(TCBigDoor.THIRD, BigDoorThird.MIDDLE), 2);
        world.setBlock(upper, base.setValue(TCBigDoor.THIRD, BigDoorThird.UPPER), 2);
        world.updateNeighborsAt(pos, door);
        world.updateNeighborsAt(middle, door);
        world.updateNeighborsAt(upper, door);
    }
}
