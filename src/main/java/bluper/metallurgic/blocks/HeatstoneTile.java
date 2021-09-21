package bluper.metallurgic.blocks;

import bluper.metallurgic.Metallurgic;
import bluper.metallurgic.util.Temperature;
import net.minecraft.util.Direction;

public class HeatstoneTile extends MachineTile
{
	public HeatstoneTile()
	{
		super(Metallurgic.HEATSTONE_TILE.get(), Temperature.CONCRETE_MELTING, new Direction[]
		{ Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST }, true);
	}
}
