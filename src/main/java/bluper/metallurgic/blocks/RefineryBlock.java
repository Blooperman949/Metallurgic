package bluper.metallurgic.blocks;

import javax.annotation.Nullable;

import bluper.metallurgic.blocks.tiles.RefineryTile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class RefineryBlock extends AbstractMachineBlock
{

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new RefineryTile(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
			BlockEntityType<T> type)
	{
		if (!level.isClientSide)
			return (level1, blockPos, blockState, t) ->
			{
				if (t instanceof RefineryTile)
				{
					((RefineryTile)t).tick();
				}
			};
		return null;
	}
}
