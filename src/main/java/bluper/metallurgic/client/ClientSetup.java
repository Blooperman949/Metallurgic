package bluper.metallurgic.client;

import bluper.metallurgic.Metallurgic;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientSetup
{
	@SubscribeEvent(priority = EventPriority.LOW)
	public static void registerRenderers(final FMLClientSetupEvent e)
	{
		BlockEntityRenderers.register(Metallurgic.HOTPLATE_TILE.get(), HotplateTileRenderer::new);
	}
}
