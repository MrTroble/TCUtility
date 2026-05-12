package com.troblecodings.tcutility.init;

import java.util.function.Supplier;

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

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * 1.20.1: CreativeModeTabs werden via {@link DeferredRegister} fuer
 * {@code Registries.CREATIVE_MODE_TAB} registriert; der 1.19.3-{@code CreativeModeTabEvent} ist
 * weg. Inhalt pro Tab kommt aus dem {@code displayItems(DisplayItemsGenerator)}-Callback und
 * filtert per instanceof, damit JSON-getriebene Bloecke automatisch im richtigen Tab landen.
 */
public final class TCTabs {

    private TCTabs() {
    }

    public static final DeferredRegister<CreativeModeTab> REGISTRY =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TCUtilityMain.MODID);

    public static final RegistryObject<CreativeModeTab> SPECIAL = REGISTRY.register("tcspecial",
            tab(() -> new ItemStack(Blocks.GLASS_PANE), "itemGroup.tcspecial",
                    TCTabs::populateSpecial));
    public static final RegistryObject<CreativeModeTab> BLOCKS = REGISTRY.register("tcblocks",
            tab(() -> new ItemStack(Blocks.OAK_PLANKS), "itemGroup.tcblocks",
                    TCTabs::populateBlocks));
    public static final RegistryObject<CreativeModeTab> SLABS = REGISTRY.register("tcslabs",
            tab(() -> new ItemStack(Blocks.OAK_SLAB), "itemGroup.tcslabs",
                    TCTabs::populateSlabs));
    public static final RegistryObject<CreativeModeTab> STAIRS = REGISTRY.register("tcstairs",
            tab(() -> new ItemStack(Blocks.OAK_STAIRS), "itemGroup.tcstairs",
                    TCTabs::populateStairs));
    public static final RegistryObject<CreativeModeTab> FENCE = REGISTRY.register("tcfence",
            tab(() -> new ItemStack(Blocks.OAK_FENCE), "itemGroup.tcfence",
                    TCTabs::populateFence));
    public static final RegistryObject<CreativeModeTab> DOORS = REGISTRY.register("tcdoors",
            tab(() -> new ItemStack(Items.OAK_DOOR), "itemGroup.tcdoors",
                    TCTabs::populateDoors));
    public static final RegistryObject<CreativeModeTab> ITEMS = REGISTRY.register("tcitems",
            tab(() -> new ItemStack(Items.PAPER), "itemGroup.tcitems",
                    TCTabs::populateItems));

    private static Supplier<CreativeModeTab> tab(final Supplier<ItemStack> icon,
            final String titleKey, final CreativeModeTab.DisplayItemsGenerator gen) {
        return () -> CreativeModeTab.builder()
                .icon(icon)
                .title(Component.translatable(titleKey))
                .displayItems(gen)
                .build();
    }

    private static void populateBlocks(final CreativeModeTab.ItemDisplayParameters params,
            final CreativeModeTab.Output out) {
        forEachModItem(item -> {
            final Block block = blockOf(item);
            if (block instanceof TCCube || block instanceof TCCubeRotation
                    || block instanceof TCCubeRotationAll) {
                out.accept(new ItemStack(item));
            }
        });
    }

    private static void populateSlabs(final CreativeModeTab.ItemDisplayParameters params,
            final CreativeModeTab.Output out) {
        forEachModItem(item -> {
            if (blockOf(item) instanceof TCSlab) {
                out.accept(new ItemStack(item));
            }
        });
    }

    private static void populateStairs(final CreativeModeTab.ItemDisplayParameters params,
            final CreativeModeTab.Output out) {
        forEachModItem(item -> {
            if (blockOf(item) instanceof TCStairs) {
                out.accept(new ItemStack(item));
            }
        });
    }

    private static void populateFence(final CreativeModeTab.ItemDisplayParameters params,
            final CreativeModeTab.Output out) {
        forEachModItem(item -> {
            final Block block = blockOf(item);
            if (block instanceof TCFence || block instanceof TCFenceGate
                    || block instanceof TCWall) {
                out.accept(new ItemStack(item));
            }
        });
    }

    private static void populateDoors(final CreativeModeTab.ItemDisplayParameters params,
            final CreativeModeTab.Output out) {
        forEachModItem(item -> {
            final Block block = blockOf(item);
            if (block instanceof TCDoor || block instanceof TCBigDoor
                    || block instanceof TCGarageDoor || block instanceof TCGarageGate
                    || block instanceof TCTrapDoor
                    || "TCDoorItem".equals(item.getClass().getSimpleName())
                    || "TCBigDoorItem".equals(item.getClass().getSimpleName())) {
                out.accept(new ItemStack(item));
            }
        });
    }

    private static void populateSpecial(final CreativeModeTab.ItemDisplayParameters params,
            final CreativeModeTab.Output out) {
        forEachModItem(item -> {
            final Block block = blockOf(item);
            if (block instanceof TCWindow || block instanceof TCLadder
                    || block instanceof TCHanging || block instanceof TCFluidBlock
                    || item instanceof BucketItem) {
                out.accept(new ItemStack(item));
            }
        });
    }

    private static void populateItems(final CreativeModeTab.ItemDisplayParameters params,
            final CreativeModeTab.Output out) {
        forEachModItem(item -> {
            final Block block = blockOf(item);
            if (block == null && !(item instanceof BucketItem) && !(item instanceof ArmorItem)) {
                out.accept(new ItemStack(item));
            }
        });
    }

    private static void forEachModItem(final java.util.function.Consumer<Item> sink) {
        for (final Item item : ForgeRegistries.ITEMS.getValues()) {
            final var rl = ForgeRegistries.ITEMS.getKey(item);
            if (rl != null && TCUtilityMain.MODID.equals(rl.getNamespace())) {
                sink.accept(item);
            }
        }
    }

    private static Block blockOf(final Item item) {
        return item instanceof BlockItem bi ? bi.getBlock() : null;
    }
}
