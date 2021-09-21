package bluper.metallurgic.client;

import com.mojang.blaze3d.matrix.MatrixStack;

import bluper.metallurgic.blocks.HotplateTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;

public class HotplateTileRenderer extends TileEntityRenderer<HotplateTile>
{
	Minecraft mc = Minecraft.getInstance();

	public HotplateTileRenderer(TileEntityRendererDispatcher rendererDispatcherIn)
	{
		super(rendererDispatcherIn);
	}

	@Override
	public void render(HotplateTile te, float partialTicks, MatrixStack ms, IRenderTypeBuffer b, int cl, int co)
	{
		ItemStack is = te.getItem();
		if (!is.equals(ItemStack.EMPTY))
		{
			ms.push();
			ms.translate(0.5d, 1.0d, 0.5d);
			ms.rotate(Vector3f.XP.rotationDegrees(90.0f));
			ms.scale(0.7f, 0.7f, 0.7f);
			IBakedModel m = mc.getItemRenderer().getItemModelWithOverrides(is, te.getWorld(), null);
			mc.getItemRenderer().renderItem(is, ItemCameraTransforms.TransformType.FIXED, false, ms, b,
					WorldRenderer.getCombinedLight(te.getWorld(), te.getPos().up()), co, m);
			ms.pop();
		}
	}

	public int getLightLevel(World world, BlockPos pos)
	{
		return WorldRenderer.getCombinedLight(world, pos);
	}
}
