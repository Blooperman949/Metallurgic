package bluper.metallurgic.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class InfiniHeatBlock extends Block
{
	public InfiniHeatBlock()
	{
		super(Properties.create(Material.ROCK).hardnessAndResistance(3.0f));
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new InfiniHeatTile();
	}
}
