package bluper.metallurgic.blocks.tiles;

import bluper.metallurgic.Metallurgic;
import bluper.metallurgic.util.Temperature;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class HeatstoneTile extends MachineTile
{
	public HeatstoneTile(BlockPos pos, BlockState state)
	{
		super(Metallurgic.HEATSTONE_TILE.get(), Temperature.CONCRETE_MELTING, new Direction[]
		{ Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST }, true, pos, state);
	}
}
