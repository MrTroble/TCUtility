package eu.gir.girutility;

import org.apache.logging.log4j.Logger;

import eu.gir.girutility.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = GirutilityMain.MODID, acceptedMinecraftVersions = "[1.12.2]", modLanguage = "java")

public class GirutilityMain {

	@Instance
	private static GirutilityMain instance;
	public static final String MODID = "girutility";

	public static GirutilityMain getInstance() {
		return instance;
	}

	@SidedProxy(serverSide = "eu.gir.girutility.proxy.CommonProxy", clientSide = "eu.gir.girutility.proxy.ClientProxy")
	public static CommonProxy PROXY;
	public static Logger LOG;

	@EventHandler
	public void preinit(FMLPreInitializationEvent event) {
		LOG = event.getModLog();
		PROXY.preinit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		PROXY.init(event);
	}

	@EventHandler
	public void postinit(FMLPostInitializationEvent event) {
		PROXY.postinit(event);
	}
}
