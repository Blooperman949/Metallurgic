package bluper.metallurgic;

import java.io.File;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod.EventBusSubscriber
public class Config
{
	// Setup
	private static final ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();
	private static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();
	public static ForgeConfigSpec SERVER_CONFIG;
	public static ForgeConfigSpec CLIENT_CONFIG;

	static
	{
		init(SERVER_BUILDER, CLIENT_BUILDER);

		SERVER_CONFIG = SERVER_BUILDER.build();
		CLIENT_CONFIG = CLIENT_BUILDER.build();
	}

	public static void init(ForgeConfigSpec.Builder server, ForgeConfigSpec.Builder client)
	{
//		server.comment("Machine Config");
//		maxtemp = server
//				.comment("Hottest possible machine temp")
//				.defineInRange("machines.maxtemp", 1500, 1, Double.MAX_VALUE);
	}

	public static void loadConfig(ForgeConfigSpec config, String path)
	{
		Metallurgic.LOGGER.info("Loading Config: " + path);
		final CommentedFileConfig file = CommentedFileConfig.builder(new File(path)).sync().autosave()
				.writingMode(WritingMode.REPLACE).build();
		Metallurgic.LOGGER.info("Built Config: " + path);
		file.load();
		Metallurgic.LOGGER.info("Loaded Config: " + path);
		SERVER_CONFIG.setConfig(file);
	}

	@SubscribeEvent
	public static void onLoad(final ModConfig.Loading configEvent)
	{
	}

	@SubscribeEvent
	public static void onReload(final ModConfig.Reloading configEvent)
	{
	}

}
