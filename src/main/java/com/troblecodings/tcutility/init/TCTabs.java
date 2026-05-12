package com.troblecodings.tcutility.init;

import com.troblecodings.tcutility.TCUtilityMain;
import com.troblecodings.tcutility.blocks.TCBigDoor;
import com.troblecodings.tcutility.blocks.TCCube;
import com.troblecodings.tcutility.blocks.TCCubeRotation;
import com.troblecodings.tcutility.blocks.TCCubeRotationAll;
import com.troblecodings.tcutility.blocks.TCDoor;
import com.troblecodings.tcutility.blocks.TCFence;
import com.troblecodings.tcutility.blocks.TCFenceGate;
import com.troblecodings.tcutility.blocks.TCGarageDoor;
import com.troblecodings.tcutility.blocks.TCGarageGate;
import com.troblecodings.tcutility.blocks.TCHanging;
import com.troblecodings.tcutility.blocks.TCLadder;
import com.troblecodings.tcutility.blocks.TCSlab;
import com.troblecodings.tcutility.blocks.TCStairs;
import com.troblecodings.tcutility.blocks.TCTrapDoor;
import com.troblecodings.tcutility.blocks.TCWall;
import com.troblecodings.tcutility.blocks.TCWindow;
import com.troblecodings.tcutility.fluids.TCFluidBlock;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Custom-Creative-Tabs fuer 1.19.4. Forge ersetzt in 1.19.3+ das
 * {@code Registries.CREATIVE_MODE_TAB}-DeferredRegister-Schema (das gibt's
 * erst ab 1.20) durch zwei Mod-Bus-Events:
 * {@link CreativeModeTabEvent.Register} zum Registrieren neuer Tabs und
 * {@link CreativeModeTabEvent.BuildContents} zum Befuellen pro Tab.
 */
public final class TCTabs {

    private TCTabs() {
    }

    public static CreativeModeTab SPECIAL;
    public static CreativeModeTab BLOCKS;
    public static CreativeModeTab SLABS;
    public static CreativeModeTab STAIRS;
    public static CreativeModeTab FENCE;
    public static CreativeModeTab DOORS;
    public static CreativeModeTab ITEMS;

    @SubscribeEvent
    public static void onRegister(final CreativeModeTabEvent.Register event) {
        SPECIAL = event.registerCreativeModeTab(
                new ResourceLocation(TCUtilityMain.MODID, "tcspecial"),
                builder -> builder
                        .icon(() -> new ItemStack(Blocks.GLASS_PANE))
                        .title(Component.translatable("itemGroup.tcspecial")));
        BLOCKS = event.registerCreativeModeTab(
                new ResourceLocation(TCUtilityMain.MODID, "tcblocks"),
                builder -> builder
                        .icon(() -> new ItemStack(Blocks.OAK_PLANKS))
                        .title(Component.translatable("itemGroup.tcblocks")));
        SLABS = event.registerCreativeModeTab(
                new ResourceLocation(TCUtilityMain.MODID, "tcslabs"),
                builder -> builder
                        .icon(() -> new ItemStack(Blocks.OAK_SLAB))
                        .title(Component.translatable("itemGroup.tcslabs")));
        STAIRS = event.registerCreativeModeTab(
                new ResourceLocation(TCUtilityMain.MODID, "tcstairs"),
                builder -> builder
                        .icon(() -> new ItemStack(Blocks.OAK_STAIRS))
                        .title(Component.translatable("itemGroup.tcstairs")));
        FENCE = event.registerCreativeModeTab(
                new ResourceLocation(TCUtilityMain.MODID, "tcfence"),
                builder -> builder
                        .icon(() -> new ItemStack(Blocks.OAK_FENCE))
                        .title(Component.translatable("itemGroup.tcfence")));
        DOORS = event.registerCreativeModeTab(
                new ResourceLocation(TCUtilityMain.MODID, "tcdoors"),
                builder -> builder
                        .icon(() -> new ItemStack(Items.OAK_DOOR))
                        .title(Component.translatable("itemGroup.tcdoors")));
        ITEMS = event.registerCreativeModeTab(
                new ResourceLocation(TCUtilityMain.MODID, "tcitems"),
                builder -> builder
                        .icon(() -> new ItemStack(Items.PAPER))
                        .title(Component.translatable("itemGroup.tcitems")));
    }

    @SubscribeEvent
    public static void onBuildContents(final CreativeModeTabEvent.BuildContents event) {
        final TabKind kind = kindOf(event.getTab());
        if (kind == null) {
            return;
        }
        for (final Item item : ForgeRegistries.ITEMS.getValues()) {
            final ResourceLocation rl = ForgeRegistries.ITEMS.getKey(item);
            if (rl == null || !TCUtilityMain.MODID.equals(rl.getNamespace())) {
                continue;
            }
            if (matches(item, kind)) {
                event.accept(new ItemStack(item));
            }
        }
    }

    private enum TabKind {
        SPECIAL, BLOCKS, SLABS, STAIRS, FENCE, DOORS, ITEMS
    }

    private static TabKind kindOf(final CreativeModeTab tab) {
        if (tab == SPECIAL) return TabKind.SPECIAL;
        if (tab == BLOCKS) return TabKind.BLOCKS;
        if (tab == SLABS) return TabKind.SLABS;
        if (tab == STAIRS) return TabKind.STAIRS;
        if (tab == FENCE) return TabKind.FENCE;
        if (tab == DOORS) return TabKind.DOORS;
        if (tab == ITEMS) return TabKind.ITEMS;
        return null;
    }

    private static boolean matches(final Item item, final TabKind kind) {
        final Block block = (item instanceof BlockItem bi) ? bi.getBlock() : null;
        switch (kind) {
            case SLABS:
                return block instanceof TCSlab;
            case STAIRS:
                return block instanceof TCStairs;
            case FENCE:
                return block instanceof TCFence || block instanceof TCFenceGate
                        || block instanceof TCWall;
            case DOORS:
                return block instanceof TCDoor || block instanceof TCBigDoor
                        || block instanceof TCGarageDoor || block instanceof TCGarageGate
                        || block instanceof TCTrapDoor
                        || "TCDoorItem".equals(item.getClass().getSimpleName())
                        || "TCBigDoorItem".equals(item.getClass().getSimpleName());
            case SPECIAL:
                return block instanceof TCWindow || block instanceof TCLadder
                        || block instanceof TCHanging || block instanceof TCFluidBlock
                        || item instanceof BucketItem;
            case ITEMS:
                return block == null && !(item instanceof BucketItem)
                        && !(item instanceof ArmorItem);
            case BLOCKS:
            default:
                return block instanceof TCCube || block instanceof TCCubeRotation
                        || block instanceof TCCubeRotationAll;
        }
    }
}
