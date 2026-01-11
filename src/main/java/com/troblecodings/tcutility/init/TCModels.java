package com.troblecodings.tcutility.init;

import com.troblecodings.tcutility.fluids.FluidStateMapper;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class TCModels {

    private TCModels() {
    }

    @SubscribeEvent
    public static void register(final ModelRegistryEvent event) {
        for (int i = 0; i < TCBlocks.blocksToRegister.size(); i++) {
            if (!TCBlocks.blocksToRegister.get(i).toString().contains("door")) {
                registerModel(Item.getItemFromBlock(TCBlocks.blocksToRegister.get(i)));
            }
        }

        for (int k = 0; k < TCItems.itemsToRegister.size(); k++) {
            registerModel(TCItems.itemsToRegister.get(k));
        }
    }

    @SubscribeEvent
    @SuppressWarnings("deprecation")
    public static void registerBlockColor(final ColorHandlerEvent.Block event) {
        final BlockColors colors = event.getBlockColors();
        TCBlocks.blocksToRegister.forEach(block -> {

            if (block.getMaterial(block.getDefaultState()).equals(Material.GRASS)) {
                colors.registerBlockColorHandler((state, worldIn, pos, tintIndex) -> {
                    if (worldIn == null || pos == null)
                        return 0xFF00FF00;
                    return BiomeColorHelper.getGrassColorAtPos(worldIn, pos);
                }, block);
            }
        });
    }

    @SuppressWarnings("deprecation")
    @SubscribeEvent
    public static void registerItemColor(final ColorHandlerEvent.Item event) {
        final ItemColors colors = event.getItemColors();
        TCBlocks.blocksToRegister.forEach(block -> {

            if (block.getMaterial(block.getDefaultState()).equals(Material.GRASS)) {
                colors.registerItemColorHandler((stack, tintIndex) -> 0xFF5E7A39, block);
            }
        });
    }

    private static void registerModel(final Item item) {
        ModelLoader.setCustomModelResourceLocation(item, 0,
                new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }

    @SubscribeEvent
    public static void registerFluidModel(final ModelRegistryEvent event) {
        TCFluidsInit.blocksToRegister.forEach(block -> {
            String name = ((BlockFluidBase) block).getFluid().getName();
            FluidStateMapper mapper = new FluidStateMapper(name);

            Item item = Item.getItemFromBlock(block);
            ModelBakery.registerItemVariants(item);
            ModelLoader.setCustomMeshDefinition(item, mapper);
            ModelLoader.setCustomStateMapper(block, mapper);
        });
    }
}
