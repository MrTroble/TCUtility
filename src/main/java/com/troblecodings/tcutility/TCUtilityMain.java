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
     * 1.21.11: {@code IModFile#findResource(String)} ist entfernt. Resource-Lookup geht jetzt
     * ueber {@code getContents().findFile(String)} und liefert {@link Optional}{@code <URI>};
     * der URI wird dann via {@link Paths#get(URI)} zurueck zu einem NIO-Path konvertiert
     * (funktioniert sowohl fuer {@code file:} als auch fuer {@code union:}/{@code jar:}-Schemata).
     */
    /**
     * 1.21.11: {@code IModFile#findResource(String)} ist entfernt, und
     * {@code JarContents#findFile(String)} findet nur Dateien, nicht Verzeichnisse.
     * Fuer Directory-Listing muessen wir ueber {@code getContentRoots()} iterieren und
     * den ersten Root verwenden, der das angefragte Sub-Directory tatsaechlich enthaelt.
     */
    private static Optional<Path> getRessourceLocation(final String location) {
        try {
            final var modFileInfo = ModList.get().getModFileById(MODID);
            if (modFileInfo == null) {
                return Optional.empty();
            }
            final var contents = modFileInfo.getFile().getContents();
            for (final Path root : contents.getContentRoots()) {
                final Path candidate = root.resolve(location);
                if (java.nio.file.Files.exists(candidate)) {
                    return Optional.of(candidate);
                }
            }
            return Optional.empty();
        } catch (final Exception e) {
            LOG.error("[TCUtility] Failed to resolve mod resource '{}'", location, e);
            return Optional.empty();
        }
    }
}
