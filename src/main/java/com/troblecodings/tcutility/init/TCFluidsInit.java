package com.troblecodings.tcutility.init;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicReference;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.troblecodings.tcutility.TCUtilityMain;
import com.troblecodings.tcutility.fluids.TCFluidBlock;
import com.troblecodings.tcutility.fluids.TCUpwardFlowingFluid;
import com.troblecodings.tcutility.utils.FluidCreateInfo;
import com.troblecodings.tcutility.utils.FluidProperties;

import java.util.function.Consumer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

/**
 * 1.19.2-Fluid-Pipeline. Fluid-Konstruktoren rufen intern
 * {@code createIntrusiveHolder} und scheitern, sobald die Registry gefroren
 * ist. Wir parsen daher in {@link #initJsonFiles} nur die Properties; die
 * eigentliche Konstruktion + Registrierung von {@link FluidType},
 * {@link ForgeFlowingFluid.Source}/{@code Flowing}, {@link TCFluidBlock} und
 * {@link BucketItem} passiert erst in den passenden {@link RegisterEvent}-
 * Frames, in denen die Registry noch offen ist.
 */
public final class TCFluidsInit {

    private TCFluidsInit() {
    }

    private static final class FluidEntry {
        final String name;
        final FluidCreateInfo info;
        final AtomicReference<FluidType> typeRef = new AtomicReference<>();
        final AtomicReference<FlowingFluid> sourceRef = new AtomicReference<>();
        final AtomicReference<FlowingFluid> flowingRef = new AtomicReference<>();
        final AtomicReference<LiquidBlock> blockRef = new AtomicReference<>();
        final AtomicReference<Item> bucketRef = new AtomicReference<>();

        FluidEntry(final String name, final FluidCreateInfo info) {
            this.name = name;
            this.info = info;
        }
    }

    private static final List<FluidEntry> entries = new ArrayList<>();

    /**
     * Reine Block-Liste fuer das client-seitige Render-Layer-Setup.
     * TCRenderTypes liest sie waehrend FMLClientSetupEvent, also nachdem
     * der BLOCKS-RegisterEvent sie hier befuellt hat.
     */
    public static final List<Block> blocksToRegister = new ArrayList<>();

    public static void initJsonFiles() {
        final Map<String, FluidProperties> fluids = getFromJson("fluiddefinitions");
        for (final Entry<String, FluidProperties> entry : fluids.entrySet()) {
            entries.add(new FluidEntry(entry.getKey(), entry.getValue().getFluidInfo()));
        }
    }

    @SubscribeEvent
    public static void onRegister(final RegisterEvent event) {
        if (event.getRegistryKey().equals(ForgeRegistries.Keys.FLUID_TYPES)) {
            event.register(ForgeRegistries.Keys.FLUID_TYPES, helper -> {
                for (final FluidEntry e : entries) {
                    // 1.19: FluidType selbst kennt keine Texturen mehr; die
                    // werden client-seitig via IClientFluidTypeExtensions
                    // gehaengt. initializeClient(Consumer<...>) wird nur
                    // auf dem Client aufgerufen, daher ist der Capture der
                    // Resources hier server-safe.
                    final ResourceLocation still = new ResourceLocation(TCUtilityMain.MODID,
                            "blocks/" + e.name + "_still");
                    final ResourceLocation flow = new ResourceLocation(TCUtilityMain.MODID,
                            "blocks/" + e.name + "_flow");
                    final FluidType type = new FluidType(FluidType.Properties.create()
                            .density(e.info.density)
                            .viscosity(e.info.viscosity)
                            .lightLevel(e.info.luminosity)
                            .temperature(e.info.temperature)) {
                        @Override
                        public void initializeClient(
                                final Consumer<IClientFluidTypeExtensions> consumer) {
                            consumer.accept(new IClientFluidTypeExtensions() {
                                @Override
                                public ResourceLocation getStillTexture() {
                                    return still;
                                }

                                @Override
                                public ResourceLocation getFlowingTexture() {
                                    return flow;
                                }
                            });
                        }
                    };
                    e.typeRef.set(type);
                    helper.register(new ResourceLocation(TCUtilityMain.MODID, e.name + "_type"),
                            type);
                }
            });
        } else if (event.getRegistryKey().equals(ForgeRegistries.Keys.FLUIDS)) {
            event.register(ForgeRegistries.Keys.FLUIDS, helper -> {
                for (final FluidEntry e : entries) {
                    // 1.12.2-Verhalten in 1.14+ replizieren:
                    //   - density < 0: Fluid spreadet aufwaerts (Steam o.ae.)
                    //   - viscosity steuert Tickrate; Vanilla water=1000/tick5,
                    //     lava=6000/tick30 -> tickRate ~ viscosity / 200
                    final int tickRate = Math.max(1, e.info.viscosity / 200);
                    final ForgeFlowingFluid.Properties props = new ForgeFlowingFluid.Properties(
                            e.typeRef::get, e.sourceRef::get, e.flowingRef::get)
                                    .block(e.blockRef::get)
                                    .bucket(e.bucketRef::get)
                                    .slopeFindDistance(Math.max(1, e.info.flowLength))
                                    .tickRate(tickRate)
                                    .explosionResistance(100f);
                    final boolean upward = e.info.density < 0;
                    final FlowingFluid source = upward
                            ? new TCUpwardFlowingFluid.Source(props, e.info.flowLength)
                            : new ForgeFlowingFluid.Source(props);
                    e.sourceRef.set(source);
                    helper.register(new ResourceLocation(TCUtilityMain.MODID, e.name), source);

                    final FlowingFluid flowing = upward
                            ? new TCUpwardFlowingFluid.Flowing(props, e.info.flowLength)
                            : new ForgeFlowingFluid.Flowing(props);
                    e.flowingRef.set(flowing);
                    helper.register(
                            new ResourceLocation(TCUtilityMain.MODID, e.name + "_flowing"),
                            flowing);
                }
            });
        } else if (event.getRegistryKey().equals(ForgeRegistries.Keys.BLOCKS)) {
            event.register(ForgeRegistries.Keys.BLOCKS, helper -> {
                for (final FluidEntry e : entries) {
                    final TCFluidBlock block = new TCFluidBlock(e.sourceRef::get,
                            BlockBehaviour.Properties.of()
                                    .mapColor(MapColor.WATER)
                                    .replaceable()
                                    .pushReaction(net.minecraft.world.level.material.PushReaction.DESTROY)
                                    .liquid()
                                    .noCollission()
                                    .strength(100f)
                                    .noLootTable()
                                    .lightLevel(state -> e.info.luminosity),
                            e.info.effect, e.info.effectDuration, e.info.effectAmplifier);
                    e.blockRef.set(block);
                    blocksToRegister.add(block);
                    helper.register(new ResourceLocation(TCUtilityMain.MODID, e.name), block);
                }
            });
        } else if (event.getRegistryKey().equals(ForgeRegistries.Keys.ITEMS)) {
            event.register(ForgeRegistries.Keys.ITEMS, helper -> {
                for (final FluidEntry e : entries) {
                    final BucketItem bucket = new BucketItem(e.sourceRef::get,
                            new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET));
                    e.bucketRef.set(bucket);
                    helper.register(
                            new ResourceLocation(TCUtilityMain.MODID, e.name + "_bucket"),
                            bucket);
                }
            });
        }
    }

    private static Map<String, FluidProperties> getFromJson(final String directory) {
        final Gson gson = new Gson();
        final List<Entry<String, String>> entrySet = TCUtilityMain.fileHandler.getFiles(directory);
        final Map<String, FluidProperties> result = new HashMap<>();
        final Type typeOfHashMap = new TypeToken<Map<String, FluidProperties>>() {
        }.getType();
        if (entrySet != null) {
            entrySet.forEach(entry -> {
                final Map<String, FluidProperties> json = gson.fromJson(entry.getValue(),
                        typeOfHashMap);
                if (json != null) {
                    result.putAll(json);
                }
            });
        }
        return result;
    }
}
