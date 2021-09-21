package bluper.metallurgic.blocks;

import bluper.metallurgic.Metallurgic;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoulFireBlock;
import net.minecraft.item.FireChargeItem;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
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
			World world = e.getWorld();
			Direction dir = e.getHitVec().getFace();
			BlockPos pos = e.getHitVec().getPos().offset(dir);
			BlockState state = world.getBlockState(pos.down());
			if (shouldLightLithiumFire(state.getBlock()))
			{
				e.setCanceled(true);
				e.setCancellationResult(ActionResultType.SUCCESS);
				world.setBlockState(pos, Metallurgic.LITHIUM_FIRE.get().getDefaultState());
			}
		}
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos)
	{
		return shouldLightLithiumFire(worldIn.getBlockState(pos.down()).getBlock());
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
