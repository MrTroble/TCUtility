package com.troblecodings.tcutility;

import java.nio.file.Path;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.troblecodings.contentpacklib.ContentPackHandler;
import com.troblecodings.tcutility.init.TCBlocks;
import com.troblecodings.tcutility.init.TCFluidsInit;
import com.troblecodings.tcutility.init.TCItems;
import com.troblecodings.tcutility.init.TCTabs;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;

@Mod(TCUtilityMain.MODID)
public class TCUtilityMain {

    public static final String MODID = "tcutility";
    public static final Logger LOG = LogManager.getLogger();
    public static ContentPackHandler fileHandler;

    public TCUtilityMain(final IEventBus modBus, final ModContainer container) {
        fileHandler = new ContentPackHandler(MODID, "assets/" + MODID, LOG,
                name -> getRessourceLocation(name).orElse(null), modBus);

        // Block-/Item-/Fluid-Konstruktion ist seit 1.19 strikt an die jeweiligen
        // RegisterEvents gebunden (frozen registries) -- hier nur die JSON-Parse-Phase,
        // die echten Instanzen entstehen in den Subscribern.
        TCFluidsInit.initJsonFiles();
        TCItems.init();
        TCBlocks.init();
        TCBlocks.initJsonFiles();
        TCItems.initJsonFiles();

        // NeoForge 1.21: der Mod-Bus wird im @Mod-Konstruktor injiziert; manuelle
        // Subscriber-Registrierung weiter wie zuvor.
        TCTabs.REGISTRY.register(modBus);
        modBus.register(TCBlocks.class);
        modBus.register(TCItems.class);
        modBus.register(TCFluidsInit.class);
    }

    /**
     * 1.19+ verwendet einen Modul-Classloader, der Mod-Resources unter dem
     * {@code union:}-URL-Schema rausgibt; {@code Class.getResource} + {@code Paths.get(URI)}
     * schlaegt damit fehl. Wir holen die Pfade direkt aus dem ModFile, das fuer alle Schema-
     * Varianten einen NIO-Path liefert.
     */
    private static Optional<Path> getRessourceLocation(final String location) {
        try {
            final var modFileInfo = ModList.get().getModFileById(MODID);
            if (modFileInfo == null) {
                return Optional.empty();
            }
            final Path resolved = modFileInfo.getFile().findResource(location);
            if (resolved == null) {
                return Optional.empty();
            }
            return Optional.of(resolved);
        } catch (final Exception e) {
            LOG.error("[TCUtility] Failed to resolve mod resource '{}'", location, e);
            return Optional.empty();
        }
    }
}
