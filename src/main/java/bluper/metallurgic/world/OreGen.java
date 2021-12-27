package bluper.metallurgic.world;

import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
//import net.minecraft.world.level.levelgen.placement.PlacedFeature;


@Mod.EventBusSubscriber
public class OreGen
{
//	public static final List<PlacedFeature> NETHER_ORES = new ArrayList<>();
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void gen(final BiomeLoadingEvent event)
	{
		
		
		
//		if (event.getCategory().equals(Biome.BiomeCategory.NETHER))
//		{
//			event.getGeneration().addFeature(Decoration.UNDERGROUND_ORES,
//					Feature.ORE.configured(new OreConfiguration(new BlockMatchTest(Blocks.END_STONE),
//							Metallurgic.ALKALINE_ORE_BLOCK.get().defaultBlockState(), 4))); //110 130, size 20
//		}
//		if (event.getCategory().equals(Biome.BiomeCategory.THEEND))
//		{
//			event.getGeneration().addFeature(Decoration.UNDERGROUND_ORES,
//					Feature.ORE.configured(new OreConfiguration(new BlockMatchTest(Blocks.END_STONE),
//							Metallurgic.MALLEABLE_ORE_BLOCK.get().defaultBlockState(), 4))); //30 70, size 2
//		}
	}
}