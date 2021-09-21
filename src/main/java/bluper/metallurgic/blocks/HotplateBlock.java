package bluper.metallurgic.blocks;

import java.util.Optional;

import bluper.metallurgic.Metallurgic;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CampfireCookingRecipe;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class HotplateBlock extends Block
{
	public static final BooleanProperty LIT = BlockStateProperties.LIT;

	public HotplateBlock()
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
		return new HotplateTile();
	}

	@Override
	public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn)
	{
		double temp = ((HotplateTile) worldIn.getTileEntity(pos)).getHeatStorage().getTemp();
		if (!entityIn.isImmuneToFire() && entityIn instanceof LivingEntity
				&& !EnchantmentHelper.hasFrostWalker((LivingEntity) entityIn) && temp > 420)
		{
			if (!(entityIn.hurtResistantTime > 0))
				entityIn.attackEntityFrom(Metallurgic.HOTPLATE_DAMAGE, (float) (temp / 100));
		}
		super.onEntityWalk(worldIn, pos, entityIn);
	}

	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player,
			Hand handIn, BlockRayTraceResult hit)
	{
		TileEntity te = worldIn.getTileEntity(pos);
		if (te instanceof HotplateTile)
		{
			HotplateTile hte = (HotplateTile) te;
			ItemStack is = player.getHeldItem(handIn);
			Optional<CampfireCookingRecipe> optional = hte.findMatchingRecipe(is);
			if (optional.isPresent())
			{
				if (!worldIn.isRemote
						&& hte.addItem(player.abilities.isCreativeMode ? is.copy() : is, optional.get().getCookTime()))
					return ActionResultType.SUCCESS;
				return ActionResultType.CONSUME;
			}
			hte.dropContents();
			return ActionResultType.FAIL;
		}
		return ActionResultType.PASS;
	}

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player)
	{
		super.onBlockHarvested(worldIn, pos, state, player);
		((HotplateTile) worldIn.getTileEntity(pos)).dropContents();
	}

	@Override
	public void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(LIT);
	}
}
