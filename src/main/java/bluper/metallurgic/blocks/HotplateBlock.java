package bluper.metallurgic.blocks;

import java.util.Optional;

import javax.annotation.Nullable;

import bluper.metallurgic.Metallurgic;
import bluper.metallurgic.blocks.tiles.HotplateTile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

public class HotplateBlock extends AbstractMachineBlock
{
	public static final BooleanProperty LIT = BlockStateProperties.LIT;

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new HotplateTile(pos, state);
	}

	@Override
	public void stepOn(Level worldIn, BlockPos pos, BlockState state, Entity entityIn)
	{
		double temp = ((HotplateTile) worldIn.getBlockEntity(pos)).getHeatStorage().getTemp();
		if (!entityIn.fireImmune() && entityIn instanceof LivingEntity
				&& !EnchantmentHelper.hasFrostWalker((LivingEntity) entityIn) && temp > 420)
		{
			if (!(entityIn.invulnerableTime > 0))
				entityIn.hurt(Metallurgic.HOTPLATE_DAMAGE, (float) (temp / 100));
		}
		super.stepOn(worldIn, pos, state, entityIn);
	}

	@Override
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player,
			InteractionHand handIn, BlockHitResult hit)
	{
		BlockEntity te = worldIn.getBlockEntity(pos);
		if (te instanceof HotplateTile)
		{
			HotplateTile hte = (HotplateTile) te;
			ItemStack is = player.getItemInHand(handIn);
			Optional<CampfireCookingRecipe> optional = hte.findMatchingRecipe(is);
			if (optional.isPresent())
			{
				if (!worldIn.isClientSide
						&& hte.addItem(is.split(1), optional.get().getCookingTime()))
					return InteractionResult.SUCCESS;
				return InteractionResult.CONSUME;
			}
			hte.dropContents();
			return InteractionResult.FAIL;
		}
		return InteractionResult.PASS;
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
			BlockEntityType<T> type)
	{
		if (!level.isClientSide)
			return (level1, blockPos, blockState, t) ->
			{
				if (t instanceof HotplateTile tile)
				{
					tile.tick();
				}
			};
		return null;
	}

	@Override
	public void playerWillDestroy(Level worldIn, BlockPos pos, BlockState state, Player player)
	{
		super.playerWillDestroy(worldIn, pos, state, player);
		((HotplateTile) worldIn.getBlockEntity(pos)).dropContents();
	}

	@Override
	public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(LIT);
	}
}
