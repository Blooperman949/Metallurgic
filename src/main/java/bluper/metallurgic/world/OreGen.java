package bluper.metallurgic.world;

import bluper.metallurgic.Metallurgic;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class OreGen
{
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void gen(final BiomeLoadingEvent event)
	{
		if (event.getCategory().equals(Biome.BiomeCategory.NETHER))
		{
			event.getGeneration().addFeature(Decoration.UNDERGROUND_ORES,
					Feature.ORE.configured(new OreConfiguration(new BlockMatchTest(Blocks.END_STONE),
							Metallurgic.ALKALINE_ORE_BLOCK.get().defaultBlockState(), 4))); //110 130
		}
		if (event.getCategory().equals(Biome.BiomeCategory.THEEND))
		{
			event.getGeneration().addFeature(Decoration.UNDERGROUND_ORES,
					Feature.ORE.configured(new OreConfiguration(new BlockMatchTest(Blocks.END_STONE),
							Metallurgic.MALLEABLE_ORE_BLOCK.get().defaultBlockState(), 4))); //30 70
		}
	}
}