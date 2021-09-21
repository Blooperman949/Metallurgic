package bluper.metallurgic.client;

import bluper.metallurgic.Metallurgic;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientSetup
{
	@SubscribeEvent(priority = EventPriority.LOW)
	public static void registerRenderers(final FMLClientSetupEvent e)
	{
		ClientRegistry.bindTileEntityRenderer(Metallurgic.HOTPLATE_TILE.get(), HotplateTileRenderer::new);
	}
}
