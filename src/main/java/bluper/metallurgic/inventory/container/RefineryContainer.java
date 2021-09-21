package bluper.metallurgic.inventory.container;

import bluper.metallurgic.blocks.RefineryTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;

public class RefineryContainer extends Container
{

	protected RefineryContainer(int id, ContainerType<?> type, RefineryTile tile)
	{
		super(type, id);
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn)
	{
		return false;
	}

}
