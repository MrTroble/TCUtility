package com.troblecodings.tcutility.init;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import com.troblecodings.tcutility.enums.BlockTypes;
import com.troblecodings.tcutility.items.TCBigDoorItem;
import com.troblecodings.tcutility.items.TCDoorItem;
import com.troblecodings.tcutility.items.TCSlabItem;
import com.troblecodings.tcutility.utils.BlockCreateInfo;
import com.troblecodings.tcutility.utils.BlockProperties;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

/**
 * Sammelt im Mod-Konstruktor nur Block-Spezifikationen (Name+Typ+Properties)
 * aus den blockdefinitions-JSONs; in 1.19+ darf {@code new Block(...)} erst
 * waehrend des BLOCKS-{@link RegisterEvent} aufgerufen werden -- vorher ruft
 * der Block-Konstruktor {@code createIntrusiveHolder} auf eine bereits
 * gefrorene Registry. Die eigentliche Block- und Item-Konstruktion und
 * -Registrierung passiert daher in den passenden Event-Handlern.
 */
public final class TCBlocks {

    private TCBlocks() {
    }

    private static final class BlockSpec {
        final String objectName;
        final ResourceLocation rl;
        final BlockTypes type;
        final BlockCreateInfo info;
        Block constructedBlock;
        Block gateBlock;

        BlockSpec(final String objectName, final ResourceLocation rl, final BlockTypes type,
                final BlockCreateInfo info) {
            this.objectName = objectName;
            this.rl = rl;
            this.type = type;
            this.info = info;
        }
    }

    private static final List<BlockSpec> blockSpecs = new ArrayList<>();

    /** Reine Liste registrierter Bloecke fuer das client-seitige Render-Layer-Setup. */
    public static final List<Block> blocksToRegister = new ArrayList<>();

    /**
     * Legacy-Kompatibilitaet -- TCRenderTypes pruefte bisher ueber blockEntries.
     * Wird waehrend des BLOCKS-RegisterEvent gefuellt.
     */
    public static final List<Entry<ResourceLocation, Block>> blockEntries = new ArrayList<>();

    public static void init() {
        // Reflection-Pfad fuer manuelle public-static-final-Felder ist mit
        // dem Defer-Modell nicht vereinbar (Block kann nicht im Mod-Ctor
        // konstruiert werden). Aktuell wird er ohnehin nicht genutzt --
        // bleibt als no-op fuer kuenftige Erweiterungen.
    }

    public static void initJsonFiles() {
        final Map<String, BlockProperties> blocks = getFromJson("blockdefinitions");
        for (final Entry<String, BlockProperties> blocksEntry : blocks.entrySet()) {
            final String objectname = blocksEntry.getKey();
            final BlockProperties property = blocksEntry.getValue();
            final BlockCreateInfo blockInfo = property.getBlockInfo();
            final List<String> states = property.getStates();
            for (final String state : states) {
                final BlockTypes type = Enum.valueOf(BlockTypes.class, state.toUpperCase());
                final String registryName = type.getRegistryName(objectname);
                final ResourceLocation rl = new ResourceLocation(TCUtilityMain.MODID, registryName);
                blockSpecs.add(new BlockSpec(objectname, rl, type, blockInfo));
            }
        }
    }

    @SubscribeEvent
    public static void onRegister(final RegisterEvent event) {
        if (event.getRegistryKey().equals(ForgeRegistries.Keys.BLOCKS)) {
            event.register(ForgeRegistries.Keys.BLOCKS, helper -> {
                for (final BlockSpec spec : blockSpecs) {
                    final Block block = constructBlock(spec.type, spec.info);
                    spec.constructedBlock = block;
                    blocksToRegister.add(block);
                    blockEntries.add(Map.entry(spec.rl, block));
                    helper.register(spec.rl, block);
                    if (spec.type == BlockTypes.GARAGE) {
                        final Block gate = new TCGarageGate(spec.info);
                        spec.gateBlock = gate;
                        blocksToRegister.add(gate);
                        final ResourceLocation gateRl = new ResourceLocation(
                                TCUtilityMain.MODID, spec.rl.getPath() + "_gate");
                        blockEntries.add(Map.entry(gateRl, gate));
                        helper.register(gateRl, gate);
                    }
                }
            });
        } else if (event.getRegistryKey().equals(ForgeRegistries.Keys.ITEMS)) {
            event.register(ForgeRegistries.Keys.ITEMS, helper -> {
                for (final BlockSpec spec : blockSpecs) {
                    final Block block = spec.constructedBlock;
                    if (block == null) {
                        continue;
                    }
                    if (block instanceof TCDoor) {
                        helper.register(new ResourceLocation(TCUtilityMain.MODID,
                                "door_" + spec.objectName), new TCDoorItem(block));
                        continue;
                    }
                    if (block instanceof TCBigDoor) {
                        helper.register(new ResourceLocation(TCUtilityMain.MODID,
                                "bigdoor_" + spec.objectName), new TCBigDoorItem(block));
                        continue;
                    }
                    final BlockItem blockItem = (block instanceof TCSlab) ? new TCSlabItem(block)
                            : new BlockItem(block, new Item.Properties());
                    helper.register(spec.rl, blockItem);
                }
            });
        }
    }

    private static Block constructBlock(final BlockTypes type, final BlockCreateInfo info) {
        switch (type) {
            case CUBE:
                return new TCCube(info);
            case CUBE_ROT:
                return new TCCubeRotation(info);
            case STAIR:
                return new TCStairs(info);
            case SLAB:
                return new TCSlab(info);
            case FENCE:
                return new TCFence(info);
            case FENCE_GATE:
                return new TCFenceGate(info);
            case WALL:
                return new TCWall(info);
            case TRAPDOOR:
                return new TCTrapDoor(info);
            case WINDOW:
                return new TCWindow(info);
            case LADDER:
                return new TCLadder(info);
            case DOOR:
                return new TCDoor(info);
            case BIGDOOR:
                return new TCBigDoor(info);
            case HANGING:
                return new TCHanging(info);
            case CUBE_ROT_ALL:
                return new TCCubeRotationAll(info);
            case GARAGE:
                return new TCGarageDoor(info);
            default:
                throw new IllegalStateException("The given state " + type + " is not valid.");
        }
    }

    private static Map<String, BlockProperties> getFromJson(final String directory) {
        final Gson gson = new Gson();
        final List<Entry<String, String>> entrySet = TCUtilityMain.fileHandler.getFiles(directory);
        final Map<String, BlockProperties> properties = new HashMap<>();
        final Type typeOfHashMap = new TypeToken<Map<String, BlockProperties>>() {
        }.getType();
        if (entrySet != null) {
            entrySet.forEach(entry -> {
                final Map<String, BlockProperties> json =
                        gson.fromJson(entry.getValue(), typeOfHashMap);
                if (json != null) {
                    properties.putAll(json);
                }
            });
        }
        return properties;
    }
}
