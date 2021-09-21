package bluper.metallurgic.blocks;

import java.util.Optional;

import bluper.metallurgic.Metallurgic;
import bluper.metallurgic.util.Temperature;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CampfireCookingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemStackHandler;

public class HotplateTile extends MachineTile
{
	ItemStackHandler inventory = new ItemStackHandler(1);
	int cookingTime;
	int totalCookingTime;

	public HotplateTile()
	{
		super(Metallurgic.HOTPLATE_TILE.get(), Temperature.CONCRETE_MELTING, new Direction[]
		{ Direction.UP }, false);
	}

	@Override
	public void tick()
	{
		super.tick();
		ItemStack itemstack = inventory.getStackInSlot(0);
		boolean hot = heatStorage.getTemp() > 449;
		if (hot)
			world.setBlockState(pos, getBlockState().with(HotplateBlock.LIT, true));
		else
			world.setBlockState(pos, getBlockState().with(HotplateBlock.LIT, false));
		if (!itemstack.isEmpty())
		{
			if (hot)
				cookingTime += Math.floor(heatStorage.getTemp() / 200);
			if (cookingTime >= totalCookingTime)
			{
				IInventory inv = new Inventory(itemstack);
				ItemStack is = this.world.getRecipeManager().getRecipe(IRecipeType.CAMPFIRE_COOKING, inv, this.world)
						.map((campfireRecipe) ->
						{
							return campfireRecipe.getCraftingResult(inv);
						}).orElse(itemstack);
				is.setCount(itemstack.getCount());

				BlockPos blockpos = this.getPos();
				InventoryHelper.spawnItemStack(world, (double) blockpos.getX(), (double) blockpos.getY() + 0.5,
						(double) blockpos.getZ(), is);
				inventory.setStackInSlot(0, ItemStack.EMPTY);
			}
		}
	}

	@Override
	public void read(BlockState state, CompoundNBT nbt)
	{
		totalCookingTime = nbt.getInt("TotalCookingTime");
		cookingTime = nbt.getInt("CookingTime");
		inventory.deserializeNBT(nbt.getCompound("Inventory"));
		super.read(state, nbt);
	}

	@Override
	public CompoundNBT write(CompoundNBT nbt)
	{
		nbt.putInt("TotalCookingTime", totalCookingTime);
		nbt.putInt("CookingTime", cookingTime);
		nbt.put("Inventory", inventory.serializeNBT());
		return super.write(nbt);
	}

	private void inventoryChanged()
	{
		markDirty();
		getWorld().notifyBlockUpdate(this.getPos(), this.getBlockState(), this.getBlockState(),
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
	public CompoundNBT getUpdateTag()
	{
		return write(new CompoundNBT());
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		CompoundNBT nbt = new CompoundNBT();
		write(nbt);
		return new SUpdateTileEntityPacket(getPos(), -1, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		read(getBlockState(), pkt.getNbtCompound());
	}

	public Optional<CampfireCookingRecipe> findMatchingRecipe(ItemStack itemStackIn)
	{
		return !inventory.getStackInSlot(0).equals(ItemStack.EMPTY) ? Optional.empty()
				: world.getRecipeManager().getRecipe(IRecipeType.CAMPFIRE_COOKING, new Inventory(itemStackIn),
						this.world);
	}

	public void dropContents()
	{
		InventoryHelper.spawnItemStack(world, (double) pos.getX(), (double) pos.getY() + 1, (double) pos.getZ(),
				inventory.getStackInSlot(0));
		inventory.setStackInSlot(0, ItemStack.EMPTY);
		inventoryChanged();
	}
}
