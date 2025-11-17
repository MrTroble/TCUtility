package com.troblecodings.tcutility.proxy;

import com.troblecodings.tcutility.init.TCFluidsInit;
import com.troblecodings.tcutility.init.TCModels;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void preinit(final FMLPreInitializationEvent event) {
        super.preinit(event);
        MinecraftForge.EVENT_BUS.register(TCModels.class);
        TCFluidsInit.registerCustomMeshesAndStates();
    }

    @Override
    public void init(final FMLInitializationEvent event) {
        super.init(event);
    }

    @Override
    public void postinit(final FMLPostInitializationEvent event) {
        super.postinit(event);
    }
}
