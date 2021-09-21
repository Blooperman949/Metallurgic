package bluper.metallurgic.blocks;

import bluper.metallurgic.Metallurgic;
import bluper.metallurgic.util.Temperature;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.items.ItemStackHandler;

public class RefineryTile extends MachineTile
{
	ItemStackHandler inputSlots = new ItemStackHandler(3);
	ItemStackHandler outputSlots = new ItemStackHandler(9);

	public RefineryTile()
	{
		super(Metallurgic.REFINERY_TILE.get(), Temperature.CONCRETE_MELTING, new Direction[]
		{ Direction.UP }, false);
	}

	@Override
	public void tick()
	{
		super.tick();
	}

//	protected Container createMenu(int id, PlayerInventory player)
//	{
//		return new RefineryContainer(id, player, this);
//	}

	protected ITextComponent getDefaultName()
	{
		return new TranslationTextComponent("container.metallurgic.refinery");
	}
}
