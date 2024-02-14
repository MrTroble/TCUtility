package com.troblecodings.tcutility;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Optional;

import org.apache.logging.log4j.Logger;

import com.troblecodings.contentpacklib.ContentPackHandler;
import com.troblecodings.tcutility.init.TCBlocks;
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

    public TCUtilityMain() {
        instance = this;
        fileHandler = new ContentPackHandler(MODID, "assets/" + MODID, LOG,
                name -> getRessourceLocation(name).get().toAbsolutePath());
    }

    public static TCUtilityMain getInstance() {
        return instance;
    }

    @SidedProxy(serverSide = "com.troblecodings.tcutility.proxy.CommonProxy", clientSide = "com.troblecodings.tcutility.proxy.ClientProxy")
    public static CommonProxy PROXY;
    public static Logger LOG;
    public static ContentPackHandler fileHandler;

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

    private static FileSystem fileSystemCache = null;

    private static Optional<Path> getRessourceLocation(final String location) {
        String filelocation = location;
        final URL url = TCBlocks.class.getResource("/assets/" + MODID);
        try {
            if (url != null) {
                final URI uri = url.toURI();
                if ("file".equals(uri.getScheme())) {
                    if (!location.startsWith("/"))
                        filelocation = "/" + filelocation;
                    final URL resource = TCBlocks.class.getResource(filelocation);
                    if (resource == null)
                        return Optional.empty();
                    return Optional.of(Paths.get(resource.toURI()));
                } else {
                    if (!"jar".equals(uri.getScheme())) {
                        return Optional.empty();
                    }
                    if (fileSystemCache == null) {
                        fileSystemCache = FileSystems.newFileSystem(uri, Collections.emptyMap());
                    }
                    return Optional.of(fileSystemCache.getPath(filelocation));
                }
            }
        } catch (final IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
