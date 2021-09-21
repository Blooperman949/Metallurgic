package bluper.metallurgic.blocks;

import bluper.metallurgic.Metallurgic;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class HeatstoneBlock extends Block
{

	public HeatstoneBlock()
	{
		super(Metallurgic.MACHINE_STONE_PROPERTIES);
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new HeatstoneTile();
	}

}
