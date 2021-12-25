package bluper.metallurgic.blocks;

import javax.annotation.Nullable;

import bluper.metallurgic.Metallurgic;
import bluper.metallurgic.blocks.tiles.MachineTile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractMachineBlock extends Block implements EntityBlock
{	
	public AbstractMachineBlock()
	{
		super(Metallurgic.MACHINE_STONE_PROPERTIES);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
			BlockEntityType<T> type)
	{
		if (!level.isClientSide)
			return (level1, blockPos, blockState, t) ->
			{
				if (t instanceof MachineTile)
				{
					((MachineTile)t).tick();
				}
			};
		return null;
	}
}
