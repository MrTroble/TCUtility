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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.troblecodings.contentpacklib.ContentPackHandler;
import com.troblecodings.tcutility.init.TCBlocks;
import com.troblecodings.tcutility.init.TCFluidsInit;
import com.troblecodings.tcutility.init.TCItems;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(TCUtilityMain.MODID)
public class TCUtilityMain {

    public static final String MODID = "tcutility";
    public static final Logger LOG = LogManager.getLogger();
    public static ContentPackHandler fileHandler;

    private static FileSystem fileSystemCache = null;

    public TCUtilityMain() {
        // ContentPackHandler in 1.16.5 registriert NetworkContentPackHandler
        // intern und haengt sich auf Client-Setup an die ResourcePackList,
        // sodass kein separater "new NetworkContentPackHandler" mehr
        // notwendig ist.
        fileHandler = new ContentPackHandler(MODID, "assets/" + MODID, LOG,
                name -> getRessourceLocation(name).map(Path::toAbsolutePath).orElse(null));

        // Mod-Bus-Registration der Registry-Subscriber. Die @Mod.EventBusSubscriber
        // Annotationen koennten das auch tun, aber explizite Registrierung passt
        // besser zur JSON-getriebenen Pipeline und macht die Reihenfolge sichtbar.
        TCFluidsInit.initJsonFiles();
        TCItems.init();
        TCBlocks.init();
        TCBlocks.initJsonFiles();
        TCItems.initJsonFiles();

        FMLJavaModLoadingContext.get().getModEventBus().register(TCBlocks.class);
        FMLJavaModLoadingContext.get().getModEventBus().register(TCItems.class);
        FMLJavaModLoadingContext.get().getModEventBus().register(TCFluidsInit.class);
    }

    private static Optional<Path> getRessourceLocation(final String location) {
        String filelocation = location;
        final URL url = TCBlocks.class.getResource("/assets/" + MODID);
        try {
            if (url != null) {
                final URI uri = url.toURI();
                if ("file".equals(uri.getScheme())) {
                    if (!location.startsWith("/")) {
                        filelocation = "/" + filelocation;
                    }
                    final URL resource = TCBlocks.class.getResource(filelocation);
                    if (resource == null) {
                        return Optional.empty();
                    }
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
