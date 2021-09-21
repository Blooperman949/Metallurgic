package bluper.metallurgic.blocks;

import bluper.metallurgic.Metallurgic;
import bluper.metallurgic.util.HeatStorage;
import bluper.metallurgic.util.Temperature;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class MachineTile extends TileEntity implements ITickableTileEntity
{
	protected HeatStorage heatStorage;
	private Direction[] transferDirs;
	private double baseTemp;
	private boolean insulated;

	public MachineTile(TileEntityType<?> teType, double maxTemp, Direction[] transferDirs, boolean insulated)
	{
		super(teType);
		this.transferDirs = transferDirs;
		heatStorage = new HeatStorage(maxTemp)
		{
			@Override
			protected void onChanged()
			{
				markDirty();
			}
		};
		this.insulated = insulated;
	}

	@Override
	public void setWorldAndPos(World world, BlockPos pos)
	{
		super.setWorldAndPos(world, pos);
		baseTemp = world.getDimensionType().isUltrawarm() ? Temperature.WATER_BOILING : Temperature.ROOM;
		if (heatStorage.getTemp() < 1)
			heatStorage.setTemp(baseTemp);
		heatStorage.setBaseTemp(baseTemp);
	}

	@Override
	public void read(BlockState state, CompoundNBT nbt)
	{
		heatStorage.setTemp(nbt.getDouble("Temp"));
		super.read(state, nbt);
	}

	@Override
	public CompoundNBT write(CompoundNBT nbt)
	{
		nbt.putDouble("Temp", heatStorage.getTemp());
		return super.write(nbt);
	}

	@Override
	public void tick()
	{
		if (!world.isRemote)
		{
			double loss = heatStorage.getTemp() / (baseTemp * 10);
			if (transferDirs.length > 0 && !world.isBlockPowered(pos))
				for (Direction d : transferDirs)
					sendPower(d, loss);
			if (!insulated)
				heatStorage.removeHeat(loss);
			if (heatStorage.getTemp() > heatStorage.getMax())
				world.createExplosion(null, Metallurgic.MACHINE_EXPLODE_DAMAGE, null, (double) this.pos.getX(),
						(double) this.pos.getY(), (double) this.pos.getZ(), 2.0f, true, Explosion.Mode.DESTROY);
		}
	}

	private void sendPower(Direction dir, double transfer)
	{
		TileEntity te = world.getTileEntity(pos.offset(dir));
		if (te instanceof MachineTile)
			if (((MachineTile) te).getHeatStorage().getTemp() <= heatStorage.getTemp())
				((MachineTile) te).getHeatStorage().addHeat(heatStorage.removeHeat(transfer));
	}

	public HeatStorage getHeatStorage()
	{
		return heatStorage;
	}
}
