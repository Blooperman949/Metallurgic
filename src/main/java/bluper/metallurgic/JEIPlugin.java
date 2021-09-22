package bluper.metallurgic;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class JEIPlugin implements IModPlugin
{
	@Override
	public ResourceLocation getPluginUid()
	{
		return null;
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration)
	{
		registration.addRecipeCatalyst(new ItemStack(Metallurgic.HOTPLATE_ITEM.get()),
				VanillaRecipeCategoryUid.CAMPFIRE);
	}
}
