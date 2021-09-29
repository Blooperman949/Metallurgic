package bluper.metallurgic.blocks;

import bluper.metallurgic.Metallurgic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.FireChargeItem;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoulFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LithiumFireBlock extends SoulFireBlock
{
	public LithiumFireBlock(Properties builder)
	{
		super(builder);
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onPlayerInteract(PlayerInteractEvent.RightClickBlock e)
	{
		if ((e.getItemStack().getItem() instanceof FireChargeItem
				| e.getItemStack().getItem() instanceof FlintAndSteelItem))
		{
			Level world = e.getWorld();
			Direction dir = e.getHitVec().getDirection();
			BlockPos pos = e.getHitVec().getBlockPos().relative(dir);
			BlockState state = world.getBlockState(pos.below());
			if (shouldLightLithiumFire(state.getBlock()))
			{
				e.setCanceled(true);
				e.setCancellationResult(InteractionResult.SUCCESS);
				world.setBlockAndUpdate(pos, Metallurgic.LITHIUM_FIRE.get().defaultBlockState());
			}
		}
	}

	public static boolean shouldLightLithiumFire(Block block)
	{
		return block.equals(Metallurgic.ALKALINE_ORE_BLOCK.get());
	}

	@Override
	protected boolean canBurn(BlockState state)
	{
		return true;
	}
}
