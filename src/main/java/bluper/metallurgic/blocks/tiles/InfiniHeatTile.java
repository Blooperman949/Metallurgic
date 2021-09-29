package bluper.metallurgic.blocks.tiles;

import bluper.metallurgic.Metallurgic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class InfiniHeatTile extends MachineTile
{

	public InfiniHeatTile(BlockPos pos, BlockState state)
	{
		super(Metallurgic.INFINIHEAT_TILE.get(), 2000.0, new Direction[]
		{ Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST }, true, pos, state);
	}

	@Override
	public void tick()
	{
		heatStorage.setTemp(heatStorage.getMax() - 10);
		super.tick();
	}

}
