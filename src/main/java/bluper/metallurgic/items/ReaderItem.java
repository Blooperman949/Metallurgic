package bluper.metallurgic.items;

import bluper.metallurgic.Metallurgic;
import bluper.metallurgic.blocks.MachineTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemUseContext;
import net.minecraft.network.play.server.STitlePacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ReaderItem extends Item
{
	public ReaderItem()
	{
		super(new Item.Properties().group(ItemGroup.TOOLS).maxStackSize(1));
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context)
	{
		World world = context.getWorld();
		PlayerEntity player = context.getPlayer();
		TileEntity te = null;
		if (!world.isRemote)
		{
			if (player.getHeldItem(context.getHand()).getItem().equals(Metallurgic.THERMOMETER.get())) // if player
																										// holding
																										// thermometer
			{
				te = world.getTileEntity(context.getPos());
				if (te != null && te instanceof MachineTile)
					sendToPlayer("Temperature: " + Math.floor(((MachineTile) te).getHeatStorage().getTemp() * 100) / 100
							+ "\u00b0K", (ServerPlayerEntity) player);
			}
		}
		return te instanceof MachineTile ? ActionResultType.SUCCESS : super.onItemUse(context);
	}

	public static void sendToPlayer(String text, ServerPlayerEntity player)
	{
		if (player != null)
			player.connection
					.sendPacket(new STitlePacket(STitlePacket.Type.ACTIONBAR, ForgeHooks.newChatWithLinks(text)));
	}

}
