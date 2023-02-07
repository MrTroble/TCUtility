package com.troblecodings.tcutility.proxy;

import com.troblecodings.tcutility.init.TCBlocks;
import com.troblecodings.tcutility.init.TCItems;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

    public void preinit(final FMLPreInitializationEvent event) {
        TCItems.init();
        TCBlocks.init();
        TCBlocks.initJsonFiles();

        MinecraftForge.EVENT_BUS.register(TCItems.class);
        MinecraftForge.EVENT_BUS.register(TCBlocks.class);
    }

    public void init(final FMLInitializationEvent event) {

    }

    public void postinit(final FMLPostInitializationEvent event) {

    }

}
