package bluper.metallurgic.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import bluper.metallurgic.blocks.tiles.HotplateTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.model.animation.AnimationBlockEntityRenderer;

public class HotplateTileRenderer extends AnimationBlockEntityRenderer<HotplateTile>
{
	private static Minecraft mc = Minecraft.getInstance();

	public HotplateTileRenderer(BlockEntityRenderDispatcher rendererDispatcherIn)
	{
		super();
	}

	@Override
	public void render(HotplateTile te, float partialTicks, PoseStack ms, MultiBufferSource b, int cl, int co)
	{
		ItemStack is = te.getItem();
		if (!is.equals(ItemStack.EMPTY))
		{
			ms.pushPose();
			ms.translate(0.5d, 1.0d, 0.5d);
			ms.mulPose(Vector3f.XP.rotationDegrees(90.0f));
			ms.scale(0.7f, 0.7f, 0.7f);
			mc.getItemRenderer().renderAndDecorateItem(is, LevelRenderer.getLightColor(te.getLevel(), te.getBlockPos().above()), co);
			ms.popPose();
		}
	}

	public int getLightLevel(Level world, BlockPos pos)
	{
		return LevelRenderer.getLightColor(world, pos);
	}
}
