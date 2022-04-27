package eu.gir.girutility.proxy;

import eu.gir.girutility.init.GIRBlocks;
import eu.gir.girutility.init.GIRItems;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

    public void preinit(FMLPreInitializationEvent event) {
        GIRItems.init();
        GIRBlocks.init();

        MinecraftForge.EVENT_BUS.register(GIRItems.class);
        MinecraftForge.EVENT_BUS.register(GIRBlocks.class);
    }

    public void init(FMLInitializationEvent event) {

    }

    public void postinit(FMLPostInitializationEvent event) {

    }

}
