package bluper.metallurgic.blocks;

import bluper.metallurgic.Metallurgic;
import net.minecraft.util.Direction;

public class InfiniHeatTile extends MachineTile
{

	public InfiniHeatTile()
	{
		super(Metallurgic.INFINIHEAT_TILE.get(), 2000.0, new Direction[]
		{ Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST }, true);
	}

	@Override
	public void tick()
	{
		heatStorage.setTemp(heatStorage.getMax() - 10);
		super.tick();
	}

}
