package bluper.metallurgic.blocks.tiles;

import bluper.metallurgic.Metallurgic;
import bluper.metallurgic.util.Temperature;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class RefineryTile extends MachineTile
{

	public RefineryTile(BlockPos pos, BlockState state)
	{
		super(Metallurgic.REFINERY_TILE.get(), Temperature.CONCRETE_MELTING, new Direction[] {}, false, pos, state);
	}

}
