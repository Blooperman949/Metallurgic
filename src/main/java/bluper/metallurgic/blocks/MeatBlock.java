package bluper.metallurgic.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class MeatBlock extends Block
{
	public static final IntegerProperty LAYERS = IntegerProperty.create("layers", 1, 9);
	protected SoundEvent eatSound;
	protected Food food;
	protected static final VoxelShape[] SHAPES = new VoxelShape[]
	{ VoxelShapes.empty(), Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.7D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 3.5D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 5.3D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 7.1D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.8D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 10.6D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 12.4D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 14.2D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D) };

	public MeatBlock(Item foodItem, Properties properties)
	{
		super(properties);
		if (foodItem.isFood())
		{
			this.food = foodItem.getFood();
			this.eatSound = foodItem.getEatSound();
		} else
			throw new IllegalArgumentException("MeatBlock requires a food item");
		this.setDefaultState(this.stateContainer.getBaseState().with(LAYERS, Integer.valueOf(9)));
	}

	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player,
			Hand handIn, BlockRayTraceResult hit)
	{
		if (player.getFoodStats().needFood())
		{
			player.getFoodStats().addStats(food.getHealing(), food.getSaturation());
			player.playSound(eatSound, 1, 1);
			if (state.get(LAYERS) > 1)
				worldIn.setBlockState(pos, state.with(LAYERS, state.get(LAYERS) - 1));
			else
				worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.FAIL;
	}

	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return SHAPES[state.get(LAYERS)];
	}

	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return SHAPES[state.get(LAYERS)];
	}

	public VoxelShape getCollisionShape(BlockState state, IBlockReader reader, BlockPos pos)
	{
		return SHAPES[state.get(LAYERS)];
	}

	public VoxelShape getRayTraceShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context)
	{
		return SHAPES[state.get(LAYERS)];
	}

	public boolean isTransparent(BlockState state)
	{
		return state.get(LAYERS) < 9;
	}

	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(LAYERS);
	}
}
