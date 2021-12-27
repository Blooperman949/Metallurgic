package bluper.metallurgic.blocks.tiles;

import bluper.metallurgic.Metallurgic;
import bluper.metallurgic.util.HeatStorage;
import bluper.metallurgic.util.Temperature;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.extensions.IForgeBlockEntity;

public class MachineTile extends BlockEntity implements IForgeBlockEntity
{
	protected HeatStorage heatStorage;
	private Direction[] transferDirs;
	private double baseTemp;
	private boolean insulated;

	public MachineTile(BlockEntityType<?> teType, double maxTemp, Direction[] transferDirs, boolean insulated,
			BlockPos pos, BlockState state) {
		super(teType, pos, state);
		this.transferDirs = transferDirs;
		heatStorage = new HeatStorage(maxTemp) {
			@Override
			protected void onChanged()
			{
				setChanged();
			}
		};
		this.insulated = insulated;
	}
	
	@Override
	public void setLevel(Level level)
	{
		super.setLevel(level);
		baseTemp = level.dimensionType().ultraWarm() ? Temperature.WATER_BOILING : Temperature.ROOM;
		if (heatStorage.getTemp() < 1)
			heatStorage.setTemp(baseTemp);
		heatStorage.setBaseTemp(baseTemp);
	}

	@Override
	public void load(CompoundTag nbt)
	{
		heatStorage.setTemp(nbt.getDouble("Temp"));
		super.load(nbt);
	}

	@Override
	public CompoundTag save(CompoundTag nbt)
	{
		nbt.putDouble("Temp", heatStorage.getTemp());
		return super.save(nbt);
	}

	public void tick()
	{
		if (!level.isClientSide)
		{
			double loss = heatStorage.getTemp() / (baseTemp * 10);
			if (transferDirs.length > 0 && level.getDirectSignalTo(this.worldPosition) == 0)
				for (Direction d : transferDirs)
					sendPower(d, loss);
			if (!insulated)
				heatStorage.removeHeat(loss);
			if (heatStorage.getTemp() > heatStorage.getMax())
			{
				level.explode(null, Metallurgic.MACHINE_EXPLODE_DAMAGE, null, this.worldPosition.getX(),
						this.worldPosition.getY(), this.worldPosition.getZ(), 2.0f, true, Explosion.BlockInteraction.DESTROY);
				level.setBlock(this.worldPosition, Blocks.AIR.defaultBlockState(), 0);
			}
		}
	}

	private void sendPower(Direction dir, double transfer)
	{
		BlockEntity te = level.getBlockEntity(worldPosition.relative(dir));
		if (te instanceof MachineTile)
			if (((MachineTile) te).getHeatStorage().getTemp() <= heatStorage.getTemp())
				((MachineTile) te).getHeatStorage().addHeat(heatStorage.removeHeat(transfer));
	}

	public final HeatStorage getHeatStorage()
	{
		return heatStorage;
	}
}
