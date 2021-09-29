package bluper.metallurgic.blocks;

import bluper.metallurgic.blocks.tiles.HeatstoneTile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class HeatstoneBlock extends AbstractMachineBlock
{
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new HeatstoneTile(pos, state);
	}
}
