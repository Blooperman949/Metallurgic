package bluper.metallurgic.blocks;

import bluper.metallurgic.Metallurgic;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class SodiumBlock extends Block
{
	World w;

	public SodiumBlock(Properties properties)
	{
		super(properties);
	}

	@Override
	public void fillWithRain(World world, BlockPos pos)
	{
		explode(world, pos);
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn,
			BlockPos currentPos, BlockPos facingPos)
	{
		if (isTouchingLiquid(worldIn, currentPos))
			explode(w, currentPos);
		return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		w = context.getWorld();
		boolean flag = isTouchingLiquid(w, context.getPos());
		if (flag)
			explode(w, context.getPos());
		return flag ? Blocks.AIR.getDefaultState() : super.getStateForPlacement(context);
	}

	private void explode(World world, BlockPos pos)
	{
		if (!world.isRemote)
		{
			world.createExplosion(null, Metallurgic.SODIUM_EXPLODE_DAMAGE, null, pos.getX(), pos.getY(), pos.getZ(),
					1.5f, false, Explosion.Mode.DESTROY);
		}
	}

	// stolen from net.minecraft.block.ConcretePowderBlock
	private static boolean isTouchingLiquid(IBlockReader reader, BlockPos pos)
	{
		boolean flag = false;
		BlockPos.Mutable blockpos$mutable = pos.toMutable();

		for (Direction direction : Direction.values())
		{
			BlockState blockstate = reader.getBlockState(blockpos$mutable);
			if (direction != Direction.DOWN || isWater(blockstate))
			{
				blockpos$mutable.setAndMove(pos, direction);
				blockstate = reader.getBlockState(blockpos$mutable);
				if (isWater(blockstate) && !blockstate.isSolidSide(reader, pos, direction.getOpposite()))
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
		return state.getFluidState().isTagged(FluidTags.WATER);
	}

	@Override
	public boolean canDropFromExplosion(Explosion explosionIn)
	{
		return false;
	}
}
