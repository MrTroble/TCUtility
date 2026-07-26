package com.troblecodings.tcutility.items;

import com.troblecodings.tcutility.blocks.TCBigDoor;
import com.troblecodings.tcutility.blocks.TCBigDoor.BigDoorThird;
import com.troblecodings.tcutility.init.TCTabs;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.properties.DoorHingeSide;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Setzt beim Klicken den dreistoeckigen Cluster eines {@link TCBigDoor}.
 * 1:1-Port der Vanilla-1.12-Door-Placement-Heuristik (Block neben Tuer +
 * normalcube-Counts links/rechts), damit Doppel-Tueren wieder als
 * Doppel-Tuer einrasten und nicht ineinander rendern.
 */
public class TCBigDoorItem extends Item {

    private final TCBigDoor door;

    public TCBigDoorItem(final Block block) {
        super(new Item.Properties().group(TCTabs.DOORS));
        this.door = (TCBigDoor) block;
        this.door.setItem(this);
    }

    public Block getBlock() {
        return door;
    }

    @Override
    public ActionResultType onItemUse(final ItemUseContext ctx) {
        if (ctx.getFace() != Direction.UP) {
            return ActionResultType.FAIL;
        }
        final World world = ctx.getWorld();
        final PlayerEntity player = ctx.getPlayer();
        if (player == null) {
            return ActionResultType.FAIL;
        }
        final ItemStack stack = player.getHeldItem(ctx.getHand());

        // Wenn der angeklickte Block ersetzbar ist (Gras/Schnee), wird er
        // selbst zur LOWER-Position; sonst LOWER = clicked + face. Vanilla-
        // Verhalten aus 1.12.
        BlockPos pos = ctx.getPos();
        final BlockState clicked = world.getBlockState(pos);
        if (!clicked.getMaterial().isReplaceable()) {
            pos = pos.offset(ctx.getFace());
        }
        final BlockPos middle = pos.up();
        final BlockPos upper = pos.up(2);

        if (!world.getBlockState(pos).getMaterial().isReplaceable()
                || !world.getBlockState(middle).getMaterial().isReplaceable()
                || !world.getBlockState(upper).getMaterial().isReplaceable()) {
            return ActionResultType.FAIL;
        }
        // Solider Untergrund noetig.
        if (!world.getBlockState(pos.down()).getMaterial().isSolid()) {
            return ActionResultType.FAIL;
        }
        if (!player.canPlayerEdit(pos, ctx.getFace(), stack)) {
            return ActionResultType.FAIL;
        }

        final Direction facing = player.getHorizontalFacing();
        final Vec3d hit = ctx.getHitVec();
        final double hitX = hit.x - ctx.getPos().getX();
        final double hitZ = hit.z - ctx.getPos().getZ();

        // Initiale isRightHinge-Vermutung aus relativer Klick-Position --
        // 1:1 wie in der Vanilla-1.12-Door-Logik.
        final int xOff = facing.getXOffset();
        final int zOff = facing.getZOffset();
        final boolean initialRightHinge = (xOff < 0 && hitZ < 0.5D)
                || (xOff > 0 && hitZ > 0.5D)
                || (zOff < 0 && hitX > 0.5D)
                || (zOff > 0 && hitX < 0.5D);

        placeDoor(world, pos, facing, door, initialRightHinge);

        final BlockState placedLower = world.getBlockState(pos);
        final SoundType soundtype = placedLower.getBlock().getSoundType(placedLower, world, pos,
                player);
        world.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS,
                (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);

        if (!player.abilities.isCreativeMode) {
            stack.shrink(1);
        }
        return ActionResultType.SUCCESS;
    }

    /**
     * Port der Vanilla-1.12-{@code BlockDoor.placeDoor}-Hinge-Heuristik,
     * mit der Original-Door-Klasse als Vergleichsblock (statt
     * {@code instanceof TCBigDoor}, sonst stoeren sich benachbarte
     * Door-Varianten gegenseitig).
     */
    private static void placeDoor(final World world, final BlockPos pos, final Direction facing,
            final TCBigDoor door, boolean isRightHinge) {
        final BlockPos right = pos.offset(facing.rotateY());
        final BlockPos left = pos.offset(facing.rotateYCCW());
        final int rightCubes = (world.getBlockState(right).isNormalCube(world, right) ? 1 : 0)
                + (world.getBlockState(right.up()).isNormalCube(world, right.up()) ? 1 : 0);
        final int leftCubes = (world.getBlockState(left).isNormalCube(world, left) ? 1 : 0)
                + (world.getBlockState(left.up()).isNormalCube(world, left.up()) ? 1 : 0);
        final boolean rightIsDoor = world.getBlockState(right).getBlock() == door
                || world.getBlockState(right.up()).getBlock() == door;
        final boolean leftIsDoor = world.getBlockState(left).getBlock() == door
                || world.getBlockState(left.up()).getBlock() == door;

        if ((!leftIsDoor || rightIsDoor) && rightCubes <= leftCubes) {
            if ((rightIsDoor && !leftIsDoor) || rightCubes < leftCubes) {
                isRightHinge = false;
            }
        } else {
            isRightHinge = true;
        }

        final BlockPos middle = pos.up();
        final BlockPos upper = pos.up(2);
        final boolean powered = world.isBlockPowered(pos) || world.isBlockPowered(middle);
        final BlockState base = door.getDefaultState()
                .with(TCBigDoor.FACING, facing)
                .with(TCBigDoor.HINGE,
                        isRightHinge ? DoorHingeSide.RIGHT : DoorHingeSide.LEFT)
                .with(TCBigDoor.POWERED, Boolean.valueOf(powered))
                .with(TCBigDoor.OPEN, Boolean.valueOf(powered));
        world.setBlockState(pos, base.with(TCBigDoor.THIRD, BigDoorThird.LOWER), 2);
        world.setBlockState(middle, base.with(TCBigDoor.THIRD, BigDoorThird.MIDDLE), 2);
        world.setBlockState(upper, base.with(TCBigDoor.THIRD, BigDoorThird.UPPER), 2);
        world.notifyNeighborsOfStateChange(pos, door);
        world.notifyNeighborsOfStateChange(middle, door);
        world.notifyNeighborsOfStateChange(upper, door);
    }
}
