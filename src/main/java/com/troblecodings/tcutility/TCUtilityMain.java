package com.troblecodings.tcutility;

import org.apache.logging.log4j.Logger;

import com.troblecodings.tcutility.proxy.CommonProxy;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = TCUtilityMain.MODID, acceptedMinecraftVersions = "[1.12.2]", modLanguage = "java")

public class TCUtilityMain {

    @Instance
    private static TCUtilityMain instance;
    public static final String MODID = "tcutility";

    public static TCUtilityMain getInstance() {
        return instance;
    }

    @SidedProxy(serverSide = "com.troblecodings.tcutility.proxy.CommonProxy",
            clientSide = "com.troblecodings.tcutility.proxy.ClientProxy")
    public static CommonProxy PROXY;
    public static Logger LOG;

    @EventHandler
    public void preinit(final FMLPreInitializationEvent event) {
        LOG = event.getModLog();
        PROXY.preinit(event);
    }

    @EventHandler
    public void init(final FMLInitializationEvent event) {
        PROXY.init(event);
    }

    @EventHandler
    public void postinit(final FMLPostInitializationEvent event) {
        PROXY.postinit(event);
    }
}
