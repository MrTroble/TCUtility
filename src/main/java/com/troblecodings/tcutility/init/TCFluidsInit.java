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
import com.troblecodings.tcutility.fluids.TCFluids;
import com.troblecodings.tcutility.fluids.TCUpwardFlowingFluid;
import com.troblecodings.tcutility.utils.FluidCreateInfo;
import com.troblecodings.tcutility.utils.FluidProperties;

import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * 1.14.4-Fluid-Pipeline: pro JSON-Eintrag in {@code fluiddefinitions/} werden
 * eine {@link ForgeFlowingFluid.Source}, eine {@link ForgeFlowingFluid.Flowing},
 * ein {@link TCFluidBlock} (mit optionalem Status-Effekt aus dem JSON) und
 * ein {@link BucketItem} angelegt. Die zirkulaeren Source-/Flowing-/Block-/
 * Bucket-Suppliers werden ueber {@link AtomicReference}-Holder aufgeloest,
 * die nach der Konstruktion alle vier Instanzen kennen.
 *
 * Die Blockstate- und Modell-JSONs fuer den Fluid-Block werden zur Build-Zeit
 * von {@code com.troblecodings.build.steps.FluidAssetGenStep} aus den
 * gleichen JSON-Definitionen generiert, sodass die Source-Resources
 * unveraendert bleiben.
 */
@Mod.EventBusSubscriber(modid = TCUtilityMain.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class TCFluidsInit {

    private TCFluidsInit() {
    }

    public static final List<TCFluids> fluidSets = new ArrayList<>();
    public static final List<Fluid> fluidsToRegister = new ArrayList<>();
    public static final List<Block> blocksToRegister = new ArrayList<>();
    public static final List<Item> itemsToRegister = new ArrayList<>();

    @SubscribeEvent
    public static void registerFluid(final RegistryEvent.Register<Fluid> event) {
        final IForgeRegistry<Fluid> registry = event.getRegistry();
        fluidsToRegister.forEach(registry::register);
    }

    @SubscribeEvent
    public static void registerBlock(final RegistryEvent.Register<Block> event) {
        final IForgeRegistry<Block> registry = event.getRegistry();
        blocksToRegister.forEach(registry::register);
    }

    @SubscribeEvent
    public static void registerItem(final RegistryEvent.Register<Item> event) {
        final IForgeRegistry<Item> registry = event.getRegistry();
        itemsToRegister.forEach(registry::register);
    }

    public static void initJsonFiles() {
        final Map<String, FluidProperties> fluids = getFromJson("fluiddefinitions");
        for (final Entry<String, FluidProperties> entry : fluids.entrySet()) {
            buildFluid(entry.getKey(), entry.getValue());
        }
    }

    private static void buildFluid(final String name, final FluidProperties properties) {
        final FluidCreateInfo info = properties.getFluidInfo();
        final ResourceLocation rlSource = new ResourceLocation(TCUtilityMain.MODID, name);
        final ResourceLocation rlFlowing = new ResourceLocation(TCUtilityMain.MODID,
                name + "_flowing");
        final ResourceLocation rlBucket = new ResourceLocation(TCUtilityMain.MODID,
                name + "_bucket");

        final FluidAttributes.Builder attrs = FluidAttributes
                .builder(new ResourceLocation(TCUtilityMain.MODID, "blocks/" + name + "_still"),
                        new ResourceLocation(TCUtilityMain.MODID, "blocks/" + name + "_flow"))
                .density(info.density)
                .viscosity(info.viscosity)
                .luminosity(info.luminosity)
                .temperature(info.temperature);
        if (info.density < 0) {
            attrs.gaseous();
        }

        // Holder pro Komponente, weil ForgeFlowingFluid.Properties die
        // anderen Komponenten als Supplier ueber gegenseitige Referenzen
        // einbindet (Source <-> Flowing <-> Block <-> Bucket).
        final AtomicReference<FlowingFluid> sourceRef = new AtomicReference<>();
        final AtomicReference<FlowingFluid> flowingRef = new AtomicReference<>();
        final AtomicReference<FlowingFluidBlock> blockRef = new AtomicReference<>();
        final AtomicReference<Item> bucketRef = new AtomicReference<>();

        // 1.12.2-Verhalten in 1.14+ replizieren:
        //   - density < 0: Fluid spreadet aufwaerts (Steam o.ae.)
        //   - viscosity steuert Tickrate; Vanilla water=1000/tick5,
        //     lava=6000/tick30 -> tickRate ~ viscosity / 200
        final int tickRate = Math.max(1, info.viscosity / 200);
        final ForgeFlowingFluid.Properties fluidProps = new ForgeFlowingFluid.Properties(
                sourceRef::get, flowingRef::get, attrs)
                        .block(blockRef::get)
                        .bucket(bucketRef::get)
                        .slopeFindDistance(Math.max(1, info.flowLength))
                        .tickRate(tickRate)
                        .explosionResistance(100f);
        if (info.canCreateSource) {
            fluidProps.canMultiply();
        }

        final boolean upward = info.density < 0;
        final FlowingFluid source = upward
                ? new TCUpwardFlowingFluid.Source(fluidProps, info.flowLength)
                : new ForgeFlowingFluid.Source(fluidProps);
        source.setRegistryName(rlSource);
        sourceRef.set(source);

        final FlowingFluid flowing = upward
                ? new TCUpwardFlowingFluid.Flowing(fluidProps, info.flowLength)
                : new ForgeFlowingFluid.Flowing(fluidProps);
        flowing.setRegistryName(rlFlowing);
        flowingRef.set(flowing);

        final TCFluidBlock block = new TCFluidBlock(sourceRef::get,
                Block.Properties.create(Material.WATER)
                        .doesNotBlockMovement()
                        .hardnessAndResistance(100f)
                        .noDrops()
                        .lightValue(info.luminosity),
                info.effect, info.effectDuration, info.effectAmplifier);
        block.setRegistryName(rlSource);
        blockRef.set(block);

        final BucketItem bucket = new BucketItem(sourceRef::get,
                new Item.Properties().group(TCTabs.SPECIAL).maxStackSize(1)
                        .containerItem(Items.BUCKET));
        bucket.setRegistryName(rlBucket);
        bucketRef.set(bucket);

        fluidSets.add(new TCFluids(name, source, flowing, block, bucket));
        fluidsToRegister.add(source);
        fluidsToRegister.add(flowing);
        blocksToRegister.add(block);
        itemsToRegister.add(bucket);
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
