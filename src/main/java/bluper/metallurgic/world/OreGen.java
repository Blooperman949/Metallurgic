package bluper.metallurgic.world;

import bluper.metallurgic.Metallurgic;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig.FillerBlockType;
import net.minecraft.world.gen.feature.template.BlockMatchRuleTest;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
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
		if(event.getCategory().equals(Biome.Category.NETHER))
		{
			event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(
					new OreFeatureConfig(FillerBlockType.NETHERRACK, Metallurgic.ALKALINE_ORE_BLOCK.get().getDefaultState(), 20))
					.withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(110, 0, 130))).square().func_242731_b(1)); 
		}
		if(event.getCategory().equals(Biome.Category.THEEND))
		{
			event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(
					new OreFeatureConfig(new BlockMatchRuleTest(Blocks.END_STONE), Metallurgic.MALLEABLE_ORE_BLOCK.get().getDefaultState(), 4))
					.withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(0, 0, 100))).square().func_242731_b(20)); 
		}
	}
}