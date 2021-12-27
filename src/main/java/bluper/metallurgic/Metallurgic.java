package bluper.metallurgic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bluper.metallurgic.blocks.HeatstoneBlock;
import bluper.metallurgic.blocks.HotplateBlock;
import bluper.metallurgic.blocks.InfiniHeatBlock;
import bluper.metallurgic.blocks.LithiumFireBlock;
import bluper.metallurgic.blocks.MeatBlock;
import bluper.metallurgic.blocks.RefineryBlock;
import bluper.metallurgic.blocks.SodiumBlock;
import bluper.metallurgic.blocks.tiles.HeatstoneTile;
import bluper.metallurgic.blocks.tiles.HotplateTile;
import bluper.metallurgic.blocks.tiles.InfiniHeatTile;
import bluper.metallurgic.blocks.tiles.RefineryTile;
import bluper.metallurgic.items.ThermometerItem;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(Metallurgic.MOD_ID)
public class Metallurgic
{
	public static final String MOD_ID = "metallurgic";

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
	public static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister
			.create(ForgeRegistries.BLOCK_ENTITIES, MOD_ID);
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	public static final DamageSource HOTPLATE_DAMAGE = new DamageSource("hotplate").setIsFire();
	public static final DamageSource MACHINE_EXPLODE_DAMAGE = new DamageSource("machine_explosion").setExplosion();
	public static final DamageSource SODIUM_EXPLODE_DAMAGE = new DamageSource("sodium_explosion").setExplosion();
	public static final Block.Properties MACHINE_STONE_PROPERTIES = Properties.of(Material.STONE).strength(3.0f)
			.requiresCorrectToolForDrops();
	private static final CreativeModeTab MACHINES = new CreativeModeTab("machines")
	{
		@Override
		public ItemStack makeIcon()
		{
			return new ItemStack(MACHINE_FRAME_ITEM.get());
		}
	};

	public Metallurgic()
	{
		init();
	}

	private void init()
	{
		// Registry
		BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
		ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
		TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
		
		// Config Setup
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_CONFIG);
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);
		Config.loadConfig(Config.SERVER_CONFIG, FMLPaths.CONFIGDIR.get().resolve("metallurgic-server.toml").toString());
		Config.loadConfig(Config.CLIENT_CONFIG, FMLPaths.CONFIGDIR.get().resolve("metallurgic-client.toml").toString());
	}

	// Registry
	// Misc. blocks
	public static final RegistryObject<Block> BEEF_BLOCK = BLOCKS.register("beef_block", () -> new MeatBlock(Items.BEEF,
			Block.Properties.of(Material.SPONGE, MaterialColor.COLOR_RED).strength(0.5f).sound(SoundType.SLIME_BLOCK)));
	public static final RegistryObject<Item> BEEF_BLOCK_ITEM = ITEMS.register("beef_block",
			() -> new BlockItem(BEEF_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
	public static final RegistryObject<Block> COOKED_BEEF_BLOCK = BLOCKS.register("cooked_beef_block",
			() -> new MeatBlock(Items.COOKED_BEEF, Block.Properties.of(Material.SPONGE, MaterialColor.COLOR_BROWN)
					.strength(0.5f).sound(SoundType.SHROOMLIGHT)));
	public static final RegistryObject<Item> COOKED_BEEF_BLOCK_ITEM = ITEMS.register("cooked_beef_block",
			() -> new BlockItem(COOKED_BEEF_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
	public static final RegistryObject<Block> ALKALINE_ORE_BLOCK = BLOCKS.register("alkaline_ore",
			() -> new Block(Block.Properties.of(Material.STONE, MaterialColor.NETHER).strength(2.0f)
					.sound(SoundType.NETHERRACK).requiresCorrectToolForDrops()));
	public static final RegistryObject<Item> ALKALINE_ORE_ITEM = ITEMS.register("alkaline_ore",
			() -> new BlockItem(ALKALINE_ORE_BLOCK.get(),
					new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Block> MALLEABLE_ORE_BLOCK = BLOCKS.register("malleable_ore",
			() -> new Block(Block.Properties.of(Material.STONE, MaterialColor.SAND).strength(2.0f)
					.sound(SoundType.GILDED_BLACKSTONE).requiresCorrectToolForDrops()));
	public static final RegistryObject<Item> MALLEABLE_ORE_ITEM = ITEMS.register("malleable_ore",
			() -> new BlockItem(MALLEABLE_ORE_BLOCK.get(),
					new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Block> LITHIUM_FIRE = BLOCKS.register("lithium_fire",
			() -> new LithiumFireBlock(Block.Properties.of(Material.FIRE, MaterialColor.COLOR_PINK).noCollission()
					.strength(0.0f).lightLevel((state) ->
					{
						return 10;
					}).sound(SoundType.WOOL)));

	// Machines
	public static final RegistryObject<Block> MACHINE_FRAME_BLOCK = BLOCKS.register("machine_frame",
			() -> new Block(MACHINE_STONE_PROPERTIES));
	public static final RegistryObject<Item> MACHINE_FRAME_ITEM = ITEMS.register("machine_frame",
			() -> new BlockItem(MACHINE_FRAME_BLOCK.get(), new Item.Properties().tab(MACHINES)));
	public static final RegistryObject<Block> HEATSTONE_BLOCK = BLOCKS.register("heatstone", HeatstoneBlock::new);
	public static final RegistryObject<Item> HEATSTONE_ITEM = ITEMS.register("heatstone",
			() -> new BlockItem(HEATSTONE_BLOCK.get(), new Item.Properties().tab(MACHINES)));
	public static final RegistryObject<BlockEntityType<HeatstoneTile>> HEATSTONE_TILE = TILES.register("heatstone",
			() -> BlockEntityType.Builder.of(HeatstoneTile::new, HEATSTONE_BLOCK.get()).build(null));
	public static final RegistryObject<Block> INFINIHEAT_BLOCK = BLOCKS.register("infiniheat", InfiniHeatBlock::new);
	public static final RegistryObject<Item> INFINIHEAT_ITEM = ITEMS.register("infiniheat",
			() -> new BlockItem(INFINIHEAT_BLOCK.get(), new Item.Properties().tab(MACHINES)));
	public static final RegistryObject<BlockEntityType<InfiniHeatTile>> INFINIHEAT_TILE = TILES.register("infiniheat",
			() -> BlockEntityType.Builder.of(InfiniHeatTile::new, INFINIHEAT_BLOCK.get()).build(null));
	public static final RegistryObject<Block> HOTPLATE_BLOCK = BLOCKS.register("hotplate", HotplateBlock::new);
	public static final RegistryObject<Item> HOTPLATE_ITEM = ITEMS.register("hotplate",
			() -> new BlockItem(HOTPLATE_BLOCK.get(), new Item.Properties().tab(MACHINES)));
	public static final RegistryObject<BlockEntityType<HotplateTile>> HOTPLATE_TILE = TILES.register("hotplate",
			() -> BlockEntityType.Builder.of(HotplateTile::new, HOTPLATE_BLOCK.get()).build(null));
	public static final RegistryObject<Block> REFINERY_BLOCK = BLOCKS.register("refinery", RefineryBlock::new);
	public static final RegistryObject<Item> REFINERY_ITEM = ITEMS.register("refinery",
			() -> new BlockItem(REFINERY_BLOCK.get(), new Item.Properties().tab(MACHINES)));
	public static final RegistryObject<BlockEntityType<RefineryTile>> REFINERY_TILE = TILES.register("refinery",
			() -> BlockEntityType.Builder.of(RefineryTile::new, REFINERY_BLOCK.get()).build(null));

	// Miscellaneous items
	public static final RegistryObject<Item> INSULATOR_ITEM = ITEMS.register("insulator",
			() -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
	public static final RegistryObject<Item> FAN_BLADES_ITEM = ITEMS.register("fan_blades",
			() -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
	public static final RegistryObject<Item> NODE_ITEM = ITEMS.register("node",
			() -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
	public static final RegistryObject<Item> SILICON_ITEM = ITEMS.register("silicon",
			() -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
	public static final RegistryObject<Item> THERMOMETER = ITEMS.register("thermometer", () -> new ThermometerItem());

	// Base metals
	public static final RegistryObject<Item> LITHIUM_INGOT_ITEM = ITEMS.register("lithium_ingot",
			() -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
	public static final RegistryObject<Item> MAGNESIUM_INGOT_ITEM = ITEMS.register("magnesium_ingot",
			() -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
	public static final RegistryObject<Item> SODIUM_INGOT_ITEM = ITEMS.register("sodium_ingot",
			() -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
	public static final RegistryObject<Item> TITANIUM_INGOT_ITEM = ITEMS.register("titanium_ingot",
			() -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
	public static final RegistryObject<Item> NICKEL_INGOT_ITEM = ITEMS.register("nickel_ingot",
			() -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
	public static final RegistryObject<Item> TIN_INGOT_ITEM = ITEMS.register("tin_ingot",
			() -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
	public static final RegistryObject<Item> LEAD_NUGGET_ITEM = ITEMS.register("lead_nugget",
			() -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
	public static final RegistryObject<Item> ALUMINIUM_NUGGET_ITEM = ITEMS.register("aluminium_nugget",
			() -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
	public static final RegistryObject<Item> SILVER_INGOT_ITEM = ITEMS.register("silver_ingot",
			() -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
	public static final RegistryObject<Item> PLATINUM_INGOT_ITEM = ITEMS.register("platinum_ingot",
			() -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
	public static final RegistryObject<Item> ZINC_INGOT_ITEM = ITEMS.register("zinc_ingot",
			() -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
	public static final RegistryObject<Item> MOLYBDENUM_INGOT_ITEM = ITEMS.register("molybdenum_ingot",
			() -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
	public static final RegistryObject<Item> COBALT_INGOT_ITEM = ITEMS.register("cobalt_ingot",
			() -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
	public static final RegistryObject<Item> CHROMIUM_INGOT_ITEM = ITEMS.register("chromium_ingot",
			() -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
	public static final RegistryObject<Item> BISMUTH_INGOT_ITEM = ITEMS.register("bismuth_ingot",
			() -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
	public static final RegistryObject<Item> MERCURY_ITEM = ITEMS.register("mercury",
			() -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
	public static final RegistryObject<Item> GALLIUM_ITEM = ITEMS.register("gallium",
			() -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));

	public static final RegistryObject<Item> LITHIUM_NUGGET_ITEM = ITEMS.register("lithium_nugget",
			() -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
	public static final RegistryObject<Item> MAGNESIUM_NUGGET_ITEM = ITEMS.register("magnesium_nugget",
			() -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
	public static final RegistryObject<Item> SODIUM_NUGGET_ITEM = ITEMS.register("sodium_nugget",
			() -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
	public static final RegistryObject<Item> TITANIUM_NUGGET_ITEM = ITEMS.register("titanium_nugget",
			() -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
	public static final RegistryObject<Item> NICKEL_NUGGET_ITEM = ITEMS.register("nickel_nugget",
			() -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
	public static final RegistryObject<Item> TIN_NUGGET_ITEM = ITEMS.register("tin_nugget",
			() -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
	public static final RegistryObject<Item> LEAD_INGOT_ITEM = ITEMS.register("lead_ingot",
			() -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
	public static final RegistryObject<Item> ALUMINIUM_INGOT_ITEM = ITEMS.register("aluminium_ingot",
			() -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
	public static final RegistryObject<Item> SILVER_NUGGET_ITEM = ITEMS.register("silver_nugget",
			() -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
	public static final RegistryObject<Item> PLATINUM_NUGGET_ITEM = ITEMS.register("platinum_nugget",
			() -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
	public static final RegistryObject<Item> ZINC_NUGGET_ITEM = ITEMS.register("zinc_nugget",
			() -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
	public static final RegistryObject<Item> MOLYBDENUM_NUGGET_ITEM = ITEMS.register("molybdenum_nugget",
			() -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
	public static final RegistryObject<Item> COBALT_NUGGET_ITEM = ITEMS.register("cobalt_nugget",
			() -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
	public static final RegistryObject<Item> CHROMIUM_NUGGET_ITEM = ITEMS.register("chromium_nugget",
			() -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
	public static final RegistryObject<Item> BISMUTH_NUGGET_ITEM = ITEMS.register("bismuth_nugget",
			() -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));

	public static final RegistryObject<Block> LITHIUM_BLOCK = BLOCKS.register("lithium_block",
			() -> new Block(Block.Properties.of(Material.METAL, MaterialColor.COLOR_PINK).strength(4.0f, 3.0f)
					.requiresCorrectToolForDrops().sound(SoundType.METAL)));
	public static final RegistryObject<Item> LITHIUM_BLOCK_ITEM = ITEMS.register("lithium_block",
			() -> new BlockItem(LITHIUM_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Block> MAGNESIUM_BLOCK = BLOCKS.register("magnesium_block",
			() -> new Block(Block.Properties.of(Material.METAL, MaterialColor.COLOR_BLACK).strength(5.0f)
					.requiresCorrectToolForDrops().sound(SoundType.METAL)));
	public static final RegistryObject<Item> MAGNESIUM_BLOCK_ITEM = ITEMS.register("magnesium_block",
			() -> new BlockItem(MAGNESIUM_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Block> SODIUM_BLOCK = BLOCKS.register("sodium_block",
			() -> new SodiumBlock(Block.Properties.of(Material.METAL, MaterialColor.QUARTZ).strength(3.0f, 0.0f)
					.randomTicks().sound(SoundType.BASALT)));
	public static final RegistryObject<Item> SODIUM_BLOCK_ITEM = ITEMS.register("sodium_block",
			() -> new BlockItem(SODIUM_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Block> TITANIUM_BLOCK = BLOCKS.register("titanium_block",
			() -> new Block(Block.Properties.of(Material.METAL, MaterialColor.COLOR_BLACK).strength(10.0f, 40.0f)
					.requiresCorrectToolForDrops().sound(SoundType.METAL)));
	public static final RegistryObject<Item> TITANIUM_BLOCK_ITEM = ITEMS.register("titanium_block",
			() -> new BlockItem(TITANIUM_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Block> NICKEL_BLOCK = BLOCKS.register("nickel_block",
			() -> new Block(Block.Properties.of(Material.METAL, MaterialColor.GOLD).strength(5.0f, 6.0f)
					.requiresCorrectToolForDrops().sound(SoundType.METAL)));
	public static final RegistryObject<Item> NICKEL_BLOCK_ITEM = ITEMS.register("nickel_block",
			() -> new BlockItem(NICKEL_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Block> TIN_BLOCK = BLOCKS.register("tin_block", () -> new Block(Block.Properties
			.of(Material.METAL).strength(5.0f, 6.0f).requiresCorrectToolForDrops().sound(SoundType.METAL)));
	public static final RegistryObject<Item> TIN_BLOCK_ITEM = ITEMS.register("tin_block",
			() -> new BlockItem(TIN_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Block> LEAD_BLOCK = BLOCKS.register("lead_block",
			() -> new Block(Block.Properties.of(Material.METAL, MaterialColor.COLOR_BLACK).strength(6.0f, 8.0f)
					.requiresCorrectToolForDrops().sound(SoundType.METAL)));
	public static final RegistryObject<Item> LEAD_BLOCK_ITEM = ITEMS.register("lead_block",
			() -> new BlockItem(LEAD_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Block> ALUMINIUM_BLOCK = BLOCKS.register("aluminium_block",
			() -> new Block(Block.Properties.of(Material.METAL).strength(5.0f, 6.0f).requiresCorrectToolForDrops()
					.sound(SoundType.BONE_BLOCK)));
	public static final RegistryObject<Item> ALUMINIUM_BLOCK_ITEM = ITEMS.register("aluminium_block",
			() -> new BlockItem(ALUMINIUM_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Block> SILVER_BLOCK = BLOCKS.register("silver_block",
			() -> new Block(Block.Properties.of(Material.METAL).strength(3.0f, 6.0f).requiresCorrectToolForDrops()
					.sound(SoundType.METAL)));
	public static final RegistryObject<Item> SILVER_BLOCK_ITEM = ITEMS.register("silver_block",
			() -> new BlockItem(SILVER_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Block> PLATINUM_BLOCK = BLOCKS.register("platinum_block",
			() -> new Block(Block.Properties.of(Material.METAL).strength(3.0f, 6.0f).requiresCorrectToolForDrops()
					.sound(SoundType.METAL)));
	public static final RegistryObject<Item> PLATINUM_BLOCK_ITEM = ITEMS.register("platinum_block",
			() -> new BlockItem(PLATINUM_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Block> ZINC_BLOCK = BLOCKS.register("zinc_block",
			() -> new Block(Block.Properties.of(Material.METAL).strength(5.0f, 6.0f).requiresCorrectToolForDrops()
					.sound(SoundType.METAL)));
	public static final RegistryObject<Item> ZINC_BLOCK_ITEM = ITEMS.register("zinc_block",
			() -> new BlockItem(ZINC_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Block> MOLYBDENUM_BLOCK = BLOCKS.register("molybdenum_block",
			() -> new Block(Block.Properties.of(Material.METAL, MaterialColor.TERRACOTTA_GREEN).strength(5.0f, 6.0f)
					.requiresCorrectToolForDrops().sound(SoundType.METAL)));
	public static final RegistryObject<Item> MOLYBDENUM_BLOCK_ITEM = ITEMS.register("molybdenum_block",
			() -> new BlockItem(MOLYBDENUM_BLOCK.get(),
					new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Block> COBALT_BLOCK = BLOCKS.register("cobalt_block",
			() -> new Block(Block.Properties.of(Material.METAL, MaterialColor.COLOR_BLUE).strength(5.0f, 7.0f)
					.requiresCorrectToolForDrops().sound(SoundType.METAL)));
	public static final RegistryObject<Item> COBALT_BLOCK_ITEM = ITEMS.register("cobalt_block",
			() -> new BlockItem(COBALT_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Block> CHROMIUM_BLOCK = BLOCKS.register("chromium_block",
			() -> new Block(Block.Properties.of(Material.METAL, MaterialColor.WARPED_WART_BLOCK).strength(5.0f, 6.0f)
					.requiresCorrectToolForDrops().lightLevel((state) ->
					{
						return 5;
					}).sound(SoundType.BONE_BLOCK)));
	public static final RegistryObject<Item> CHROMIUM_BLOCK_ITEM = ITEMS.register("chromium_block",
			() -> new BlockItem(CHROMIUM_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Block> BISMUTH_BLOCK = BLOCKS.register("bismuth_block",
			() -> new Block(Block.Properties.of(Material.METAL, MaterialColor.COLOR_PINK).strength(3.0f, 4.0f)
					.requiresCorrectToolForDrops().lightLevel((state) ->
					{
						return 3;
					}).sound(SoundType.METAL)));
	public static final RegistryObject<Item> BISMUTH_BLOCK_ITEM = ITEMS.register("bismuth_block",
			() -> new BlockItem(BISMUTH_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
}
