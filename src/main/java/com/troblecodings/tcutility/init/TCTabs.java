package com.troblecodings.tcutility.init;

import com.troblecodings.tcutility.TCUtilityMain;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Custom-Creative-Tabs fuer den Mod. {@code Item.Properties.tab()} allein
 * reicht in 1.19.2 fuer JSON-getrieben registrierte Items nicht zuverlaessig
 * aus, daher fuellt jede Tab eigene Items in {@link #fillItemList} explizit
 * per Iteration ueber {@code ForgeRegistries.ITEMS}: alle Items im Mod-
 * Namespace, die {@link #accepts(Item)} fuer die jeweilige Kategorie
 * bestaetigt, landen in der Tab.
 */
public final class TCTabs {

    private TCTabs() {
    }

    /**
     * No-op-Aufruf, der das Klassen-Loading von {@link TCTabs} im Mod-
     * Konstruktor erzwingt. Dadurch landen alle Custom-{@link CreativeModeTab}
     * Instanzen via ihrer Static-Initializer in {@code CreativeModeTab.TABS},
     * bevor das Creative-Inventory-UI sie zum ersten Mal scannt.
     */
    public static void touch() {
    }

    public static final CreativeModeTab SPECIAL = new TCFilteredTab("tcspecial",
            () -> new ItemStack(Blocks.GLASS_PANE), TabKind.SPECIAL);

    public static final CreativeModeTab BLOCKS = new TCFilteredTab("tcblocks",
            () -> new ItemStack(Blocks.OAK_PLANKS), TabKind.BLOCKS);

    public static final CreativeModeTab SLABS = new TCFilteredTab("tcslabs",
            () -> new ItemStack(Blocks.OAK_SLAB), TabKind.SLABS);

    public static final CreativeModeTab STAIRS = new TCFilteredTab("tcstairs",
            () -> new ItemStack(Blocks.OAK_STAIRS), TabKind.STAIRS);

    public static final CreativeModeTab FENCE = new TCFilteredTab("tcfence",
            () -> new ItemStack(Blocks.OAK_FENCE), TabKind.FENCE);

    public static final CreativeModeTab DOORS = new TCFilteredTab("tcdoors",
            () -> new ItemStack(Items.OAK_DOOR), TabKind.DOORS);

    public static final CreativeModeTab ITEMS = new TCFilteredTab("tcitems",
            () -> new ItemStack(Items.PAPER), TabKind.ITEMS);

    /** Welche Items zu welcher Tab gehoeren -- entscheidet {@link TCFilteredTab#fillItemList}. */
    private enum TabKind {
        SPECIAL, BLOCKS, SLABS, STAIRS, FENCE, DOORS, ITEMS
    }

    /**
     * Iteriert beim Build der Tab-Inhalte einmal ueber {@code ForgeRegistries.ITEMS}
     * und filtert, welche Items in welche Kategorie gehoeren -- via Block-/Item-
     * Klassennamen, weil {@code Item.getItemCategory()} mit JSON-getriebenen
     * Registrierungen in 1.19.2 unzuverlaessig leer bleibt.
     */
    private static final class TCFilteredTab extends CreativeModeTab {

        private final java.util.function.Supplier<ItemStack> iconSupplier;
        private final TabKind kind;

        TCFilteredTab(final String label, final java.util.function.Supplier<ItemStack> icon,
                final TabKind kind) {
            super(label);
            this.iconSupplier = icon;
            this.kind = kind;
        }

        @Override
        public ItemStack makeIcon() {
            return iconSupplier.get();
        }

        @Override
        public void fillItemList(final NonNullList<ItemStack> list) {
            for (final Item item : ForgeRegistries.ITEMS.getValues()) {
                final net.minecraft.resources.ResourceLocation rl = ForgeRegistries.ITEMS
                        .getKey(item);
                if (rl == null || !TCUtilityMain.MODID.equals(rl.getNamespace())) {
                    continue;
                }
                if (matches(item)) {
                    list.add(new ItemStack(item));
                }
            }
        }

        private boolean matches(final Item item) {
            final Class<?> klass = item.getClass();
            final String simpleName = klass.getSimpleName();
            // BlockItems verraten den zugehoerigen Block-Type; Items im
            // Item-Tab haben keinen BlockItem-Wrapper.
            final net.minecraft.world.level.block.Block block;
            if (item instanceof net.minecraft.world.item.BlockItem) {
                block = ((net.minecraft.world.item.BlockItem) item).getBlock();
            } else {
                block = null;
            }
            switch (kind) {
                case SLABS:
                    return block instanceof com.troblecodings.tcutility.blocks.TCSlab;
                case STAIRS:
                    return block instanceof com.troblecodings.tcutility.blocks.TCStairs;
                case FENCE:
                    return block instanceof com.troblecodings.tcutility.blocks.TCFence
                            || block instanceof com.troblecodings.tcutility.blocks.TCFenceGate
                            || block instanceof com.troblecodings.tcutility.blocks.TCWall;
                case DOORS:
                    return block instanceof com.troblecodings.tcutility.blocks.TCDoor
                            || block instanceof com.troblecodings.tcutility.blocks.TCBigDoor
                            || block instanceof com.troblecodings.tcutility.blocks.TCGarageDoor
                            || block instanceof com.troblecodings.tcutility.blocks.TCGarageGate
                            || block instanceof com.troblecodings.tcutility.blocks.TCTrapDoor
                            || "TCDoorItem".equals(simpleName)
                            || "TCBigDoorItem".equals(simpleName);
                case SPECIAL:
                    return block instanceof com.troblecodings.tcutility.blocks.TCWindow
                            || block instanceof com.troblecodings.tcutility.blocks.TCLadder
                            || block instanceof com.troblecodings.tcutility.blocks.TCHanging
                            || item instanceof net.minecraft.world.item.BucketItem;
                case ITEMS:
                    // Pure Items (kein BlockItem, kein Bucket, kein Armor)
                    return block == null
                            && !(item instanceof net.minecraft.world.item.BucketItem)
                            && !(item instanceof net.minecraft.world.item.ArmorItem);
                case BLOCKS:
                default:
                    // Default-Bucket: alles, was sonst nirgends passt -- normale
                    // Cubes, CubeRotation, CubeRotationAll.
                    return block instanceof com.troblecodings.tcutility.blocks.TCCube
                            || block instanceof com.troblecodings.tcutility.blocks.TCCubeRotation
                            || block instanceof com.troblecodings.tcutility.blocks.TCCubeRotationAll;
            }
        }
    }
}
