package bluper.metallurgic.blocks;

import bluper.metallurgic.Metallurgic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome.Precipitation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class SodiumBlock extends Block
{
	Level w;

	public SodiumBlock(Properties properties)
	{
		super(properties);
	}

	@Override
	public void handlePrecipitation(BlockState state, Level Level, BlockPos pos, Precipitation precip)
	{
		explode(Level, pos);
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor LevelIn,
			BlockPos currentPos, BlockPos facingPos)
	{
		if (isTouchingLiquid(LevelIn, currentPos))
			explode(w, currentPos);
		return super.updateShape(stateIn, facing, facingState, LevelIn, currentPos, facingPos);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		w = context.getLevel();
		boolean flag = isTouchingLiquid(w, context.getClickedPos());
		if (flag)
			explode(w, context.getClickedPos());
		return flag ? Blocks.AIR.defaultBlockState() : super.getStateForPlacement(context);
	}

	private void explode(Level Level, BlockPos pos)
	{
		if (!Level.isClientSide)
		{
			Level.explode(null, Metallurgic.SODIUM_EXPLODE_DAMAGE, null, pos.getX(), pos.getY(), pos.getZ(),
					1.5f, false, Explosion.BlockInteraction.DESTROY);
		}
	}

	// stolen from net.minecraft.world.level.block.ConcretePowderBlock
	private static boolean isTouchingLiquid(BlockGetter reader, BlockPos pos)
	{
		boolean flag = false;
		BlockPos.MutableBlockPos mutable = pos.mutable();

		for (Direction direction : Direction.values())
		{
			BlockState blockstate = reader.getBlockState(mutable);
			if (direction != Direction.DOWN || isWater(blockstate))
			{
				mutable.setWithOffset(pos, direction);
				blockstate = reader.getBlockState(mutable);
				if (isWater(blockstate) && !blockstate.isSolidRender(reader, pos));
				{
					flag = true;
					break;
				}
			}
		}

		return flag;
	}

	private static boolean isWater(BlockState state)
	{
		return state.getFluidState().is(FluidTags.WATER);
	}

	@Override
	public boolean dropFromExplosion(Explosion explosionIn)
	{
		return false;
	}
}
