package bluper.metallurgic.blocks.tiles;

import java.util.Optional;

import bluper.metallurgic.Metallurgic;
import bluper.metallurgic.blocks.HotplateBlock;
import bluper.metallurgic.util.Temperature;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemStackHandler;

public class HotplateTile extends MachineTile
{
	ItemStackHandler inventory = new ItemStackHandler(1);
	int cookingTime;
	int totalCookingTime;

	public HotplateTile(BlockPos pos, BlockState state)
	{
		super(Metallurgic.HOTPLATE_TILE.get(), Temperature.CONCRETE_MELTING, new Direction[]
		{ Direction.UP }, false, pos, state);
	}

	@Override
	public void tick()
	{
		super.tick();
		ItemStack itemstack = inventory.getStackInSlot(0);
		boolean hot = heatStorage.getTemp() > 449;
		if (hot)
			level.setBlockAndUpdate(worldPosition, getBlockState().setValue(HotplateBlock.LIT, true));
		else
			level.setBlockAndUpdate(worldPosition, getBlockState().setValue(HotplateBlock.LIT, false));
		if (!itemstack.isEmpty())
		{
			if (hot)
				cookingTime += Math.floor(heatStorage.getTemp() / 200);
			if (cookingTime >= totalCookingTime)
			{
				Container inv = new SimpleContainer(itemstack);
				ItemStack is = this.level.getRecipeManager().getRecipeFor(RecipeType.CAMPFIRE_COOKING, inv, level)
						.map((p) ->
						{
							return p.assemble(inv);
						}).orElse(itemstack);
				is.setCount(itemstack.getCount());

				BlockPos blockpos = this.getBlockPos();
				Containers.dropItemStack(level, (double) blockpos.getX(), (double) blockpos.getY() + 0.5,
						(double) blockpos.getZ(), is);
				inventory.setStackInSlot(0, ItemStack.EMPTY);
			}
		}
	}

	@Override
	public void load(CompoundTag nbt)
	{
		totalCookingTime = nbt.getInt("TotalCookingTime");
		cookingTime = nbt.getInt("CookingTime");
		inventory.deserializeNBT(nbt.getCompound("Inventory"));
		super.load(nbt);
	}

	@Override
	public CompoundTag save(CompoundTag nbt)
	{
		nbt.putInt("TotalCookingTime", totalCookingTime);
		nbt.putInt("CookingTime", cookingTime);
		nbt.put("Inventory", inventory.serializeNBT());
		return super.save(nbt);
	}

	private void inventoryChanged()
	{
		this.setChanged();
		this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(),
				Constants.BlockFlags.NOTIFY_NEIGHBORS + Constants.BlockFlags.BLOCK_UPDATE);
	}

	public boolean addItem(ItemStack itemStackIn, int cookTime)
	{
		ItemStack itemstack = this.inventory.getStackInSlot(0);
		if (itemstack.isEmpty())
		{
			totalCookingTime = cookTime;
			cookingTime = 0;
			inventory.setStackInSlot(0, itemStackIn);
			inventoryChanged();
			return true;
		}

		return false;
	}

	public ItemStack getItem()
	{
		return inventory.getStackInSlot(0);
	}

	@Override
	public CompoundTag getUpdateTag()
	{
		return save(new CompoundTag());
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket()
	{
		CompoundTag nbt = new CompoundTag();
		save(nbt);
		return new ClientboundBlockEntityDataPacket(this.getBlockPos(), -1, nbt);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt)
	{
		this.load(pkt.getTag());
	}

	public Optional<CampfireCookingRecipe> findMatchingRecipe(ItemStack itemStackIn)
	{
		return !inventory.getStackInSlot(0).equals(ItemStack.EMPTY) ? Optional.empty()
				: level.getRecipeManager().getRecipeFor(RecipeType.CAMPFIRE_COOKING, new SimpleContainer(itemStackIn),
						this.level);
	}

	public void dropContents()
	{
		Containers.dropItemStack(level, (double) worldPosition.getX(), (double) worldPosition.getY() + 1, (double) worldPosition.getZ(),
				inventory.getStackInSlot(0));
		inventory.setStackInSlot(0, ItemStack.EMPTY);
		inventoryChanged();
	}
}
