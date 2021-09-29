package bluper.metallurgic.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MeatBlock extends Block
{
	public static final IntegerProperty LAYERS = IntegerProperty.create("layers", 1, 9);
	protected SoundEvent eatSound;
	protected FoodProperties food;
	protected static final VoxelShape[] SHAPES = new VoxelShape[]
	{ Shapes.empty(), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.7D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 3.5D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 5.3D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 7.1D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.8D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 10.6D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.4D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.2D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D) };

	public MeatBlock(Item foodItem, Properties properties)
	{
		super(properties);
		if (foodItem.isEdible())
		{
			this.food = foodItem.getFoodProperties();
			this.eatSound = foodItem.getEatingSound();
		} else
			throw new IllegalArgumentException("MeatBlock requires a food item");
		this.registerDefaultState(this.stateDefinition.any().setValue(LAYERS, Integer.valueOf(9)));
	}

	@Override
	public InteractionResult use(BlockState state, Level levelIn, BlockPos pos, Player player, InteractionHand handIn,
			BlockHitResult hit)
	{
		if (player.getFoodData().needsFood())
		{
			player.getFoodData().eat(food.getNutrition(), food.getSaturationModifier());
			player.playSound(eatSound, 1, 1);
			if (state.getValue(LAYERS) > 1)
				levelIn.setBlockAndUpdate(pos, state.setValue(LAYERS, state.getValue(LAYERS) - 1));
			else
				levelIn.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.FAIL;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context)
	{
		return SHAPES[state.getValue(LAYERS)];
	}

	@Override
	public VoxelShape getVisualShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context)
	{
		return SHAPES[state.getValue(LAYERS)];
	}

	@Override
	public VoxelShape getOcclusionShape(BlockState state, BlockGetter reader, BlockPos pos)
	{
		return SHAPES[state.getValue(LAYERS)];
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context)
	{
		return SHAPES[state.getValue(LAYERS)];
	}

	@Override
	public boolean useShapeForLightOcclusion(BlockState state)
	{
		return state.getValue(LAYERS) < 9;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(LAYERS);
	}
}
