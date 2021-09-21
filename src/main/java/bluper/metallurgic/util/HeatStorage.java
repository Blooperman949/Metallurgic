package bluper.metallurgic.util;

public class HeatStorage
{
	private double temp = 0;
	private double base;
	private double max;

	public HeatStorage(double max)
	{
		this.max = max;
	}

	public void setBaseTemp(double base)
	{
		this.base = base;
	}

	public double addHeat(double added)
	{
		temp += added;
		onChanged();
		return added;
	}

	public double removeHeat(double removed)
	{
		double pTemp = temp;
		temp = Math.max(temp - removed, base);
		onChanged();
		return pTemp - temp;
	}

	public void setTemp(double temp)
	{
		this.temp = temp;
	}

	public double getTemp()
	{
		return temp;
	}

	public double getMax()
	{
		return max;
	}

	protected void onChanged()
	{
	}
}
