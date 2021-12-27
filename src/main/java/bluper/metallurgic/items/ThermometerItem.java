package bluper.metallurgic.items;

import bluper.metallurgic.blocks.tiles.MachineTile;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ThermometerItem extends Item
{
	public ThermometerItem()
	{
		super(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS).stacksTo(1));
	}

	@Override
	public InteractionResult useOn(UseOnContext context)
	{
		Level world = context.getLevel();
		Player player = context.getPlayer();
		BlockEntity te = null;
		if (!world.isClientSide)
		{
			te = world.getBlockEntity(context.getClickedPos());
			if (te != null && te instanceof MachineTile)
				sendToPlayer("Temperature: " + Math.floor(((MachineTile) te).getHeatStorage().getTemp() * 100) / 100
						+ "\u00b0K", (ServerPlayer) player);
		}
		return te instanceof MachineTile ? InteractionResult.SUCCESS : super.useOn(context);
	}

	public static void sendToPlayer(String text, ServerPlayer player)
	{
		if (player != null)
			player.connection.send(new ClientboundSetActionBarTextPacket(ForgeHooks.newChatWithLinks(text)));
	}
}
