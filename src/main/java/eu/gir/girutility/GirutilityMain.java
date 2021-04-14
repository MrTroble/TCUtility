package eu.gir.girutility;

import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "girutility", name = "GIRUtility", version = "1.0.0", acceptedMinecraftVersions = "[1.12.2]", modLanguage = "java")

public class GirutilityMain {

	public static Logger LOG;

	@EventHandler
	public void preinit(FMLPreInitializationEvent event) {
		LOG = event.getModLog();
	}
}
