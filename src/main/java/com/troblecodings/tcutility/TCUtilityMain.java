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

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(TCUtilityMain.MODID)
public class TCUtilityMain {

    public static final String MODID = "tcutility";
    public static final Logger LOG = LogManager.getLogger();
    public static ContentPackHandler fileHandler;

    public TCUtilityMain() {
        fileHandler = new ContentPackHandler(MODID, "assets/" + MODID, LOG,
                name -> getRessourceLocation(name).orElse(null));

        // Force-load TCTabs damit die Custom-Creative-Tabs in TABS[] landen,
        // bevor irgendein Item via Properties.tab() darauf zeigt.
        TCTabs.touch();

        // Block-/Item-/Fluid-Konstruktion ist seit 1.19 strikt an die
        // jeweiligen RegisterEvents gebunden (frozen registries) -- hier nur
        // die JSON-Parse-Phase, die echten Instanzen entstehen in den
        // Subscribern.
        TCFluidsInit.initJsonFiles();
        TCItems.init();
        TCBlocks.init();
        TCBlocks.initJsonFiles();
        TCItems.initJsonFiles();

        // 1.19's modlauncher findet @Mod.EventBusSubscriber-Annotationen in
        // Subpackages nicht zuverlaessig auto-discovern -- daher manuell.
        final var modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.register(TCBlocks.class);
        modBus.register(TCItems.class);
        modBus.register(TCFluidsInit.class);
    }

    /**
     * 1.19+ verwendet einen Modul-Classloader, der Mod-Resources unter dem
     * {@code union:}-URL-Schema rausgibt -- {@code Class.getResource} +
     * {@code Paths.get(URI)} schlaegt damit fehl. Stattdessen besorgen wir
     * uns die Pfade direkt aus dem Forge-{@code IModFile}, das fuer alle
     * Schema-Varianten einen NIO-{@code Path} liefert.
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
